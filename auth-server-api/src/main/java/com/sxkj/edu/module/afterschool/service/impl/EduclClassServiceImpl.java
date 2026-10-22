package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.vo.khfw.EduclClassSimpleVO;
import com.sxkj.edu.module.afterschool.domain.ao.EduclClassListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.excel.EduclClassExcel;
import com.sxkj.edu.module.afterschool.domain.excel.EduclClassExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.EduclClassVO;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclClassMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.afterschool.service.AsTeacherClassService;
import com.sxkj.edu.module.afterschool.service.EduclClassService;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.mapper.SchoolManageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * 班级 Service 实现类
 *
 * @author system
 * @date 2026-05-19
 */
@Service
@Slf4j
public class EduclClassServiceImpl extends ServiceImpl<EduclClassMapper, TEduclClass> implements EduclClassService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private EduclGradeMapper educlGradeMapper;

    @Resource
    private SchoolManageMapper schoolManageMapper;
    @Autowired
    private AsTeacherMapper asTeacherMapper;

    @Resource
    private AsTeacherClassService asTeacherClassService;
    @Autowired
    private StudentTermScopeSupport studentTermScopeSupport;

    // ======================== 通用私有方法 ========================

    /**
     * 校验该学校班级名称是否已存在
     */
    private boolean existsByName(String className, Long schoolId,Long gradeId,Long excludeId, Long termId) {
        LambdaQueryWrapper<TEduclClass> w = new LambdaQueryWrapper<>();
        w.eq(TEduclClass::getIsValid, 1).eq(TEduclClass::getSchoolId, schoolId)
                .eq(TEduclClass::getGradeId, gradeId)
                .eq(TEduclClass::getTermId, termId)
         .eq(TEduclClass::getClassName, className);
        if (excludeId != null) {
            w.ne(TEduclClass::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * Entity -> VO
     */
    private EduclClassVO toVO(TEduclClass entity) {
        if (entity == null) return null;
        EduclClassVO vo = new EduclClassVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<EduclClassVO> queryEduclClassPage(EduclClassQueryAO qo) {
        EduclTermEntity term = qo.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(qo.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(qo.getSchoolId(), qo.getTermId());
        if(null != term) qo.setTermId(term.getId());
        Page<EduclClassVO> page = baseMapper.pageList(new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<EduclClassVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public JsonResult<String> addEduclClass(EduclClassAddAO ao) {
        if (existsByName(ao.getClassName(), ao.getSchoolId(), ao.getGradeId(), null, ao.getTermId())) {
            return JsonResult.buildError("该校同学期同年级下班级名称已存在");
        }
        TEduclClass entity = new TEduclClass();
        BeanUtils.copyProperties(ao, entity);
        //生成班级编码
        String classCode = getEduclClassCode(ao.getSchoolId());
        entity.setClassCode(classCode);
        entity.setIsValid(1);
        entity.setCreateTime(new Date());
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean save = this.save(entity);
        if (!save) return JsonResult.buildError("操作失败");
        asTeacherClassService.syncHeadTeacherClass(entity.getId(),ao.getHeadTeacherId(),entity.getSchoolId());
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> updateEduclClass(EduclClassAddAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("班级ID不能为空");
        TEduclClass exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("班级记录不存在");
        if (existsByName(ao.getClassName(), ao.getSchoolId(), ao.getGradeId(), ao.getId(), ao.getTermId())) {
            return JsonResult.buildError("该校同学期同年级下班级名称已存在");
        }
        TEduclClass entity = new TEduclClass();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        asTeacherClassService.syncHeadTeacherClass(entity.getId(),ao.getHeadTeacherId(),entity.getSchoolId());
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> deleteEduclClass(Long id) {
        TEduclClass entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("班级记录不存在");
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<EduclClassVO> getEduclClassDetail(Long id) {
        EduclClassVO vo = baseMapper.selectDetail(id);
        if (vo == null) return JsonResult.buildError("班级记录不存在");
        return JsonResult.buildSuccess(vo);
    }

    @Override
    public JsonResult<String> importEduclClass(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<EduclClassExcel> list = ExcelUtils.customerReadExcel(is, EduclClassExcel.class, 2);
            if (list == null || list.isEmpty()) return JsonResult.buildError("导入数据为空");

            // 查询所有有效的学校和年级，构建 id->名称 的映射
            Map<Long, String> schoolMap = schoolManageMapper.selectList(
                            new LambdaQueryWrapper<SchoolManagePo>().eq(SchoolManagePo::getIsValid, 1))
                    .stream().collect(Collectors.toMap(SchoolManagePo::getId, SchoolManagePo::getSchoolName));
            List<EduclGradeEntity> gradeList = educlGradeMapper.selectList(new LambdaQueryWrapper<EduclGradeEntity>().eq(EduclGradeEntity::getIsValid, 1));
           List<AsTeacherEntity> teacherList = asTeacherMapper.selectList(new LambdaQueryWrapper<AsTeacherEntity>().eq(AsTeacherEntity::getIsValid, 1)
                    .eq(AsTeacherEntity::getIsHeadTeacher,1));
            // 名称->id 的反向映射，用于导入时按名称查找
            Map<String, Long> schoolNameIdMap = schoolMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (v1, v2) -> v1));

            List<String> errors = new ArrayList<>();
            int row = 3;
            for (EduclClassExcel excel : list) {
                // 通过学校名称查找学校ID
                Long currentSchoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
                if (StrUtil.isNotBlank(excel.getSchoolName())) {
                    currentSchoolId = schoolNameIdMap.get(excel.getSchoolName());
                    if (currentSchoolId == null) {
                        errors.add("第" + row + "行: 学校名称[" + excel.getSchoolName() + "]不存在，跳过");
                        row++;
                        continue;
                    }
                }
                if (StrUtil.isBlank(excel.getClassName())) {
                    errors.add("第" + row + "行: 班级名称不能为空");
                    row++;
                    continue;
                }
                // 使用 final 变量供 lambda 引用
                final Long finalSchoolId = currentSchoolId;
                Long gradeId = gradeList.stream()
                        .filter(grade -> grade.getSchoolId().equals(finalSchoolId))
                        .filter(grade -> grade.getGradeName().equals(excel.getGradeName()))
                        .map(EduclGradeEntity::getId)
                        .findFirst().orElse(null);
                if (gradeId == null) {
                    errors.add("第" + row + "行: 年级名称[" + excel.getGradeName() + "]不存在，跳过");
                    row++;
                    continue;
                }
                EduclTermEntity educlTermEntity = studentTermScopeSupport.resolveReadableTerm(currentSchoolId);
                if(educlTermEntity == null) errors.add("第" + row + "行: 学期不存在，跳过");
                if (existsByName(excel.getClassName(), currentSchoolId, gradeId, null, educlTermEntity.getId())) {
                    errors.add("第" + row + "行: 班级名称[" + excel.getClassName() + "]已存在，跳过");
                    row++;
                    continue;
                }
                TEduclClass entity = new TEduclClass();
                entity.setTermId(educlTermEntity.getId());
                entity.setClassName(excel.getClassName());
                entity.setGradeId(gradeId);
                entity.setSchoolId(currentSchoolId);
                //生成班级编码
                String classCode = getEduclClassCode(currentSchoolId);
                log.info("call createStudentClassImport schoolId:{},gradeId:{},className:{},classCode:{}", currentSchoolId, gradeId, excel.getClassName(), classCode);
                entity.setClassCode(classCode);
                if (StringUtils.isNotBlank(excel.getHeadTeacher())) {
                    Long headTeacherId = teacherList.stream()
                            .filter(teacher -> teacher.getSchoolId().equals(finalSchoolId))
                            .filter(teacher -> teacher.getTeacherName().equals(excel.getHeadTeacher()))
                            .map(AsTeacherEntity::getId)
                            .findFirst().orElse(null);
                    entity.setHeadTeacherId(headTeacherId);
                }

                entity.setEnrollmentYear(excel.getEnrollmentYear());
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
            log.error("班级导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    @Override
    public void exportEduclClass(EduclClassQueryAO qo, HttpServletResponse response) {
        PageResultDTO<EduclClassVO> page = this.queryEduclClassPage(qo);
        List<EduclClassVO> records = page.getList();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        
        List<EduclClassExportExcel> excelList = new ArrayList<>();
        int index = 1;
        for (EduclClassVO vo : records) {
            EduclClassExportExcel excel = new EduclClassExportExcel();
            BeanUtils.copyProperties(vo, excel);
            excel.setSequence(String.valueOf(index++));
            excelList.add(excel);
        }
        
        try {
            ExcelUtils.exportExcel(response, excelList, EduclClassExportExcel.class, "班级数据导出", "班级数据");
        } catch (Exception e) {
            log.error("班级导出异常", e);
        }
    }

    @Override
    public Long createStudentClassImport(Long schoolId, Long gradeId, String className,Long termId) {
        TEduclClass entity = checkExistsByNameAndGrade(schoolId, className,gradeId,termId);
        if (entity == null) {
            entity = new TEduclClass();
            entity.setTermId(termId);
            entity.setSchoolId(schoolId);
            entity.setGradeId(gradeId);
            entity.setClassName(className);
            entity.setClassCode(getEduclClassCode(schoolId));
            entity.setIsValid(1);
            entity.setCreateTime(new Date());
            entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            Boolean uptRow = this.save(entity);
            log.info("call createStudentClassImport schoolId:{},gradeId:{},className:{},result:{}", schoolId, gradeId, className, uptRow);
            return entity.getId();
        }
        return entity.getId();
    }

    @Override
    public Map<Long, String> queryClassIdAndNameByIds(List<Long> ids) {
        List<TEduclClass> list = this.listByIds(ids);
        if (CollectionUtil.isEmpty(list)) return null;
        Map<Long, String> result = new HashMap<>();
        for (TEduclClass entity : list) {
            result.put(entity.getId(), entity.getClassName());
        }
        return result;
    }

    @Override
    public void updateClassTeacherId(Long classId, Long teacherId,Long schoolId) {
        LambdaUpdateWrapper<TEduclClass> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TEduclClass::getId, classId)
                .eq(TEduclClass::getIsValid, 1)
                .eq(TEduclClass::getSchoolId, schoolId)
                .set(TEduclClass::getHeadTeacherId, teacherId);
        Boolean uptRow = this.update(updateWrapper);
        log.info("call updateClassTeacherId classId:{},teacherId:{},result:{}", classId, teacherId, uptRow);
    }

    @Override
    public List<EduclClassSimpleVO> queryClassSimpleList(EduclClassListQueryAO qo) {
        LambdaQueryWrapper<EduclGradeEntity> gradeWrapper = new LambdaQueryWrapper<>();
        gradeWrapper.eq(EduclGradeEntity::getIsValid, 1)
                .eq(EduclGradeEntity::getSchoolId, qo.getSchoolId())
                .eq(EduclGradeEntity::getId, qo.getGradeId());
        EduclGradeEntity grade = educlGradeMapper.selectOne(gradeWrapper);
        if (grade == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<TEduclClass> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.eq(TEduclClass::getIsValid, 1)
                .eq(TEduclClass::getSchoolId, qo.getSchoolId())
                .eq(TEduclClass::getGradeId, grade.getId());
        if (StringUtils.isNotBlank(qo.getClassName())) {
            classWrapper.like(TEduclClass::getClassName, qo.getClassName());
        }
        classWrapper.last("LIMIT 50");
        List<TEduclClass> classList = this.list(classWrapper);

        List<EduclClassSimpleVO> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(classList)) {
            for (TEduclClass clazz : classList) {
                EduclClassSimpleVO vo = new EduclClassSimpleVO();
                vo.setClassName(clazz.getClassName());
                vo.setClassCode(clazz.getClassCode());
                result.add(vo);
            }
        }
        return result;
    }

    // 获取班级编码
    private String getEduclClassCode(Long schoolid){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        StringBuilder stringBuilder = new StringBuilder().append(schoolid).append(dateStr)
                .append(String.format("%03d", redisCache.increment(String.format("%s:%s", CoreConstant.edu_base_njgl, "EDUCL_CLASS_CODE"), 1L)));
        return stringBuilder.toString();
    }

    private TEduclClass checkExistsByNameAndGrade(Long schoolId, String className,Long gradeId,Long termId) {
        TEduclClass entity = null;
        LambdaQueryWrapper<TEduclClass> w = new LambdaQueryWrapper<>();
        w.eq(TEduclClass::getIsValid, 1)
                .eq(TEduclClass::getSchoolId, schoolId)
                .eq(TEduclClass::getTermId,termId)
                .eq(TEduclClass::getGradeId, gradeId)
                .eq(TEduclClass::getClassName, className.trim());
        List<TEduclClass> list = this.list(w);
        if(CollectionUtil.isNotEmpty(list)) entity = list.get(0);
        return entity;
    }

}
