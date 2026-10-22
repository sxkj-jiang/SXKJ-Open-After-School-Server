package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.domain.vo.khfw.EduclGradeSimpleVO;
import com.sxkj.edu.module.afterschool.domain.ao.EduclGradeListQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclClassMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.excel.EduclGradeExcel;
import com.sxkj.edu.module.afterschool.domain.excel.EduclGradeExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.EduclGradeVO;
import com.sxkj.edu.module.afterschool.service.EduclGradeService;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.mapper.SchoolManageMapper;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 年级 Service 实现类
 *
 * @author jiangjj
 * @date 2026-05-19
 */
@Service
@Slf4j
public class EduclGradeServiceImpl extends ServiceImpl<EduclGradeMapper, EduclGradeEntity> implements EduclGradeService {



    @Resource
    private RedisCache redisCache;
    @Resource
    private EduclClassMapper educlClassMapper;
    @Resource
    private SchoolManageMapper schoolManageMapper;
    @Resource
    private TBaseUserDao tBaseUserDao;
    @Autowired
    private AsTeacherMapper asTeacherMapper;

    // ======================== 通用私有方法 ========================

    /**
     * 校验同一学校同一阶段下年级名称是否已存在
     */
    private boolean existsByName(Long schoolId, String gradeName, Integer schoolStage, Long excludeId) {
        LambdaQueryWrapper<EduclGradeEntity> w = new LambdaQueryWrapper<>();
        w.eq(EduclGradeEntity::getIsValid, 1)
         .eq(EduclGradeEntity::getSchoolId, schoolId).eq(EduclGradeEntity::getSchoolStage, schoolStage)
         .eq(EduclGradeEntity::getGradeName, gradeName);
        if (excludeId != null) {
            w.ne(EduclGradeEntity::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * Entity -> VO
     */
    private EduclGradeVO toVO(EduclGradeEntity entity) {
        if (entity == null) return null;
        EduclGradeVO vo = new EduclGradeVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<EduclGradeVO> queryEduclGradePage(EduclGradeQueryAO qo) {
        Page<EduclGradeVO> page = baseMapper.pageList(new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<EduclGradeVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public JsonResult<EduclGradeVO> getEduclGradeDetail(Long id) {
        EduclGradeEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("年级记录不存在");
        return JsonResult.buildSuccess(toVO(entity));
    }

    @Override
    public JsonResult<String> addEduclGrade(EduclGradeAddAO ao) {
        if (existsByName(ao.getSchoolId(), ao.getGradeName(),ao.getSchoolStage(), null)) {
            return JsonResult.buildError("年级名称在该学校同阶段下已存在");
        }
        EduclGradeEntity entity = new EduclGradeEntity();
        BeanUtils.copyProperties(ao, entity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());

        //生成年级编码
        entity.setGradeCode(createGradeCode(ao.getSchoolId()));
        entity.setIsValid(1);
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setCreateTime(new Date());
        boolean save = this.save(entity);
        if (!save) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> updateEduclGrade(EduclGradeAddAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("年级ID不能为空");
        EduclGradeEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("年级记录不存在");
        if (existsByName(ao.getSchoolId(), ao.getGradeName(), ao.getSchoolStage(), ao.getId())) {
            return JsonResult.buildError("年级名称在该学校下已存在");
        }
        BeanUtils.copyProperties(ao, exist);
        exist.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        exist.setUpdateTime(new Date());
        boolean flag = this.updateById(exist);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> deleteEduclGrade(Long id) {
        EduclGradeEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("年级记录不存在");

        LambdaQueryWrapper<TEduclClass> w = new LambdaQueryWrapper<>();
        w.eq(TEduclClass::getGradeId, entity.getId());
        w.eq(TEduclClass::getIsValid, 1);
        if (educlClassMapper.selectCount(w) > 0) return JsonResult.buildError("存在班级记录，不允许删除");
        entity.setIsValid(0);
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setUpdateTime(new Date());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> importEduclGrade( MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<EduclGradeExcel> list = ExcelUtils.customerReadExcel(is, EduclGradeExcel.class,2);
            if (list == null || list.isEmpty()) return JsonResult.buildError("导入数据为空");

            Map<Long, String> schoolMap = schoolManageMapper.selectList(
                            new LambdaQueryWrapper<SchoolManagePo>().eq(SchoolManagePo::getIsValid, 1))
                    .stream().collect(Collectors.toMap(SchoolManagePo::getId, SchoolManagePo::getSchoolName));

            Map<Long, String> teacherMap = asTeacherMapper.selectList(
                            new LambdaQueryWrapper<AsTeacherEntity>().eq(AsTeacherEntity::getIsValid, 1))
                    .stream().collect(Collectors.toMap(AsTeacherEntity::getId, AsTeacherEntity::getTeacherName));

            Map<String, Long> schoolNameIdMap = schoolMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (v1, v2) -> v1));

            Map<String, Long> teacherNameIdMap = teacherMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (v1, v2) -> v1));

            List<String> errors = new ArrayList<>();
            int row = 3;
            for (EduclGradeExcel excel : list) {
                Long schoolId = schoolNameIdMap.get(excel.getSchoolName()) != null ? schoolNameIdMap.get(excel.getSchoolName()) : null;
                if (null == schoolId) {
                    errors.add("第" + row + "行: 学校名称[" + excel.getSchoolName() + "]不存在");
                    row++;
                    continue;
                }
                if (StrUtil.isBlank(excel.getGradeName())) {
                    errors.add("第" + row + "行: 年级名称不能为空");
                    row++;
                    continue;
                }
                if (existsByName(schoolId, excel.getGradeName(), AssembleUtil.operStudentStage(excel.getSchoolStage()), null)) {
                    errors.add("第" + row + "行: 年级名称[" + excel.getGradeName() + "]已存在，跳过");
                    row++;
                    continue;
                }
                EduclGradeEntity entity = new EduclGradeEntity();
                entity.setSchoolId(schoolId);
                entity.setGradeName(excel.getGradeName());
                //生成年级编码
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateStr = sdf.format(new Date());
                StringBuilder stringBuilder = new StringBuilder().append(dateStr).
                        append(String.format("%02d", redisCache.increment(String.format("%s:%s", CoreConstant.edu_base_njgl, "EDUCL_GRADE_CODE"), 1L)));
                entity.setGradeCode(stringBuilder.toString());
                entity.setSchoolStage(AssembleUtil.operStudentStage(excel.getSchoolStage()));
                entity.setDirectorUserId(teacherNameIdMap.get(excel.getDirectorName()) != null ? teacherNameIdMap.get(excel.getDirectorName()) : null);
                entity.setDirectorName(excel.getDirectorName());
                entity.setRemark(excel.getRemark());
                entity.setIsValid(1);
                entity.setCreateTime(new Date());
                entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                this.save(entity);
                row++;
            }
            if (!errors.isEmpty()) {
                return JsonResult.buildError("部分数据导入失败：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功");
        } catch (Exception e) {
            log.error("年级导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    @Override
    public void exportEduclGrade(EduclGradeQueryAO qo, HttpServletResponse response) {
        PageResultDTO<EduclGradeVO> page = this.queryEduclGradePage(qo);
        List<EduclGradeVO> records = page.getList();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        
        List<EduclGradeExportExcel> excelList = new ArrayList<>();
        int index = 1;
        for (EduclGradeVO vo : records) {
            EduclGradeExportExcel excel = new EduclGradeExportExcel();
            BeanUtils.copyProperties(vo, excel);
            excel.setSequence(String.valueOf(index++));
            excelList.add(excel);
        }
        
        try {
            ExcelUtils.exportExcel(response, excelList, EduclGradeExportExcel.class, "年级数据导出", "年级数据");
        } catch (Exception e) {
            log.error("年级导出异常", e);
        }
    }

    @Override
    public Long createEduclGrade(Long schoolId, Integer schoolStage, String gradeName) {
        Long gradeId = null;
        EduclGradeEntity gradeEntity = checkExistsByName(schoolId, gradeName);
        if (null != gradeEntity) {
            return gradeEntity.getId();
        }
        EduclGradeEntity entity = new EduclGradeEntity();
        entity.setSchoolId(schoolId);
        entity.setGradeCode(createGradeCode(schoolId));
        entity.setGradeName(gradeName);
        entity.setSchoolStage(schoolStage);
        entity.setIsValid(1);
        entity.setCreateTime(new Date());
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        Boolean uptRow = this.save(entity);
        if(uptRow){
            gradeId = entity.getId();
        }
        log.info("call createEduclGrade schoolId:{},schoolStage:{},gradeName:{},result:{}", schoolId, schoolStage, gradeName, uptRow);
        return gradeId;
    }

    @Override
    public String getGradeNameByClassId(Long classId,Long schoolId) {
        String gradeName = this.baseMapper.getGradeNameById(classId,schoolId);
        return gradeName;
    }

    private String createGradeCode(Long schoolId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        StringBuilder stringBuilder = new StringBuilder().append(dateStr).
                append(String.format("%02d", redisCache.increment(String.format("%s:%s", CoreConstant.edu_base_njgl, "EDUCL_GRADE_CODE"), 1L)));
        return stringBuilder.toString();
    }

    private EduclGradeEntity checkExistsByName(Long schoolId, String gradeName) {
        EduclGradeEntity entity = null;
        LambdaQueryWrapper<EduclGradeEntity> w = new LambdaQueryWrapper<>();
        w.eq(EduclGradeEntity::getIsValid, 1)
                .eq(EduclGradeEntity::getSchoolId, schoolId)
                .eq(EduclGradeEntity::getGradeName, gradeName.trim());
        List<EduclGradeEntity> list = this.list(w);
        if(CollectionUtil.isNotEmpty(list)) entity = list.get(0);
        return entity;
    }

    @Override
    public List<EduclGradeVO> queryGradeList(EduclGradeQueryAO qo) {
        List<EduclGradeVO> list = new ArrayList<>();
        LambdaQueryWrapper<EduclGradeEntity> w = new LambdaQueryWrapper<>();
        w.eq(EduclGradeEntity::getIsValid, 1)
                .eq(EduclGradeEntity::getSchoolId, qo.getSchoolId());
        List<EduclGradeEntity> entities = this.list(w);
        if (CollectionUtil.isNotEmpty(entities)) {
            list = entities.stream().map(e -> {
                EduclGradeVO vo = new EduclGradeVO();
                BeanUtils.copyProperties(e, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public Map<Long, String> queryGradeIdAndNameByIds(List<Long> ids) {
        List<EduclGradeEntity> list = this.listByIds(ids);
        if (CollectionUtil.isEmpty(list)) return null;
        Map<Long, String> result = new HashMap<>();
        for (EduclGradeEntity entity : list) {
            result.put(entity.getId(), entity.getGradeName());
        }
        return result;
    }

    @Override
    public List<EduclGradeSimpleVO> queryGradeSimpleList(EduclGradeListQueryAO qo) {
        LambdaQueryWrapper<EduclGradeEntity> w = new LambdaQueryWrapper<>();
        w.eq(EduclGradeEntity::getIsValid, 1)
                .eq(EduclGradeEntity::getSchoolId, qo.getSchoolId())
                .orderByAsc(EduclGradeEntity::getGradeCode)
                .last("LIMIT 10");
        List<EduclGradeEntity> entities = this.list(w);
        List<EduclGradeSimpleVO> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(entities)) {
            for (EduclGradeEntity entity : entities) {
                EduclGradeSimpleVO vo = new EduclGradeSimpleVO();
                vo.setGradeName(entity.getGradeName());
                vo.setGradeCode(entity.getGradeCode());
                vo.setGradeId(entity.getId());
                result.add(vo);
            }
        }
        return result;
    }
}
