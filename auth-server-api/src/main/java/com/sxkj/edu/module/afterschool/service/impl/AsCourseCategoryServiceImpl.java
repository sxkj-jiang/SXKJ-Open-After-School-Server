package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryListQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsCourseCategoryExportExcel;
import com.sxkj.edu.module.afterschool.mapper.AsCourseCategoryMapper;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseCategoryEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsCourseCategoryExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseCategoryVO;
import com.sxkj.edu.module.afterschool.service.AsCourseCategoryService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * 课后类别 Service 实现类
 *
 * @author system
 * @date 2026-05-19
 */
@Service
@Slf4j
public class AsCourseCategoryServiceImpl extends ServiceImpl<AsCourseCategoryMapper, AsCourseCategoryEntity> implements AsCourseCategoryService {

    @Resource
    private RedisCache redisCache;

    // ======================== 通用私有方法 ========================

    /**
     * 校验同一学校下类别名称是否已存在
     */
    private boolean existsByName(Long schoolId, String categoryName, Long excludeId) {
        LambdaQueryWrapper<AsCourseCategoryEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsCourseCategoryEntity::getIsValid, 1)
         .eq(AsCourseCategoryEntity::getSchoolId, schoolId)
         .eq(AsCourseCategoryEntity::getCategoryName, categoryName);
        if (excludeId != null) {
            w.ne(AsCourseCategoryEntity::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * Entity -> VO
     */
    private AsCourseCategoryVO toVO(AsCourseCategoryEntity entity) {
        if (entity == null) return null;
        AsCourseCategoryVO vo = new AsCourseCategoryVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<AsCourseCategoryVO> queryAsCourseCategoryPage(AsCourseCategoryQueryAO qo) {
        Page<AsCourseCategoryVO> page = new Page<>(qo.getPageIndex(), qo.getPageSize());
        baseMapper.pageList(page, qo);
        PageResultDTO<AsCourseCategoryVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public JsonResult<String> addAsCourseCategory(AsCourseCategoryAddAO ao) {
        if (existsByName(ao.getSchoolId(), ao.getCategoryName(), null)) {
            return JsonResult.buildError("类别名称在该学校下已存在");
        }
        AsCourseCategoryEntity entity = new AsCourseCategoryEntity();
        BeanUtils.copyProperties(ao, entity);
        //生成类别编码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        StringBuilder stringBuilder = new StringBuilder().append(ao.getSchoolId()).append(dateStr)
                .append(String.format("%03d", redisCache.increment(String.format("%s:%s", CoreConstant.edu_base_khlbgl, "EDUCL_CATEGORY_CODE"), 1L)));
        entity.setCategoryCode(stringBuilder.toString());
        entity.setIsValid(1);
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setCreateTime(new Date());
        boolean save = this.save(entity);
        if(!save)return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> updateAsCourseCategory(AsCourseCategoryAddAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("类别ID不能为空");
        AsCourseCategoryEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("类别记录不存在");
        if (existsByName(ao.getSchoolId(), ao.getCategoryName(), ao.getId())) {
            return JsonResult.buildError("类别名称在该学校下已存在");
        }
        AsCourseCategoryEntity entity = new AsCourseCategoryEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setUpdateTime(new Date());
        boolean flag = this.updateById(entity);
        if(!flag)return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> deleteAsCourseCategory(Long id) {
        AsCourseCategoryEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("类别记录不存在");
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        this.updateById(entity);
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> importAsCourseCategory(Long schoolId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<AsCourseCategoryExcel> list = ExcelUtils.customerReadExcel(is, AsCourseCategoryExcel.class, 2);
            if (list == null || list.isEmpty()) return JsonResult.buildError("导入数据为空");

            // 查询所有有效的学校和年级，构建 id->名称 的映射
            Map<Long, String> parentMap = baseMapper.selectList(
                            new LambdaQueryWrapper<AsCourseCategoryEntity>().eq(AsCourseCategoryEntity::getIsValid, 1).eq(AsCourseCategoryEntity::getSchoolId, schoolId)
                                    .isNull(AsCourseCategoryEntity::getParentId))
                    .stream().collect(Collectors.toMap(AsCourseCategoryEntity::getId, AsCourseCategoryEntity::getCategoryName));
            // 名称->id 的反向映射，用于导入时按名称查找
            Map<String, Long> parentNameIdMap = parentMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (v1, v2) -> v1));

            List<String> errors = new ArrayList<>();
            int row = 3;
            for (AsCourseCategoryExcel excel : list) {
                if (StrUtil.isBlank(excel.getCategoryName())) {
                    errors.add("第" + row + "行: 类别名称不能为空");
                    row++;
                    continue;
                }
                if (existsByName(schoolId, excel.getCategoryName(), null)) {
                    errors.add("第" + row + "行: 类别名称[" + excel.getCategoryName() + "]已存在，跳过");
                    row++;
                    continue;
                }
                AsCourseCategoryEntity entity = new AsCourseCategoryEntity();
                entity.setSchoolId(schoolId);
                entity.setCategoryName(excel.getCategoryName());
                if(StringUtil.isNotBlank(excel.getParentCategoryName())){
                    Long parentId = parentNameIdMap.get(excel.getParentCategoryName());
                    if (parentId != null) {
                        entity.setParentId(parentId);
                    } else {
                        errors.add("第" + row + "行: 父类别名称[" + excel.getParentCategoryName() + "]不存在，跳过");
                        row++;
                        continue;
                    }
                }
                //生成类别编码
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateStr = sdf.format(new Date());
                StringBuilder stringBuilder = new StringBuilder().append(schoolId).append(dateStr)
                        .append(String.format("%03d", redisCache.increment(String.format("%s:%s", CoreConstant.edu_base_khlbgl, "EDUCL_CATEGORY_CODE"), 1L)));
                entity.setCategoryCode(stringBuilder.toString());
                entity.setEnabledStatus(excel.getEnabledStatus() != null ? excel.getEnabledStatus() : 1);
                entity.setSortNo(excel.getSortNo());
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
            log.error("课后类别导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    @Override
    public void exportAsCourseCategory(AsCourseCategoryQueryAO qo, HttpServletResponse response) {
        // 查询所有数据（不分页）
        qo.setPageIndex(1);
        qo.setPageSize(5000);
        PageResultDTO<AsCourseCategoryVO> page = this.queryAsCourseCategoryPage(qo);
        List<AsCourseCategoryVO> records = page.getList();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        List<AsCourseCategoryExportExcel> excelList = new ArrayList<>();
        int index = 1;
        for (AsCourseCategoryVO vo : records) {
            AsCourseCategoryExportExcel excel = new AsCourseCategoryExportExcel();
            BeanUtils.copyProperties(vo, excel);
            excel.setSequence(String.valueOf(index++));
            if (vo.getEnabledStatus() != null) {
                excel.setEnabledStatus(vo.getEnabledStatus() == 1 ? "启用" : "停用");
            }
            excelList.add(excel);
        }
        ExcelUtils.exportExcel(response, excelList, AsCourseCategoryExportExcel.class, "课后类别导出", "课后类别导出");
    }

    @Override
    public JsonResult<String> updateEnabledStatus(Long id, Integer enabledStatus) {
        if (id == null) return JsonResult.buildError("类别ID不能为空");
        AsCourseCategoryEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("类别记录不存在");
        entity.setEnabledStatus(enabledStatus);
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setUpdateTime(new Date());
        boolean flag = this.updateById(entity);
        if(!flag)return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<AsCourseCategoryVO> getAsCourseCategoryDetail(Long id) {
        if (id == null) return JsonResult.buildError("类别ID不能为空");
        AsCourseCategoryVO vo = baseMapper.selectDetail(id);
        if (vo == null) return JsonResult.buildError("类别记录不存在");
        return JsonResult.buildSuccess(vo);
    }

    @Override
    public JsonResult<List<AsCourseCategoryVO>> listDropDown(AsCourseCategoryListQueryAO qo) {
        List<AsCourseCategoryVO> list = baseMapper.selectDropList(qo);
        return JsonResult.buildSuccess(list);
    }

    @Override
    public JsonResult<List<AsCourseCategoryVO>> listCategoryTree(AsCourseCategoryQueryAO qo) {
        if (qo.getSchoolId() == null) {
            qo.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        }
        // 通过XML SQL查询，关联t_base_user获取createUser姓名
        List<AsCourseCategoryVO> voList = baseMapper.selectCategoryTree(qo);
        // 构建树：parentId为null或0的是根节点
        Map<Long, List<AsCourseCategoryVO>> childrenMap = voList.stream()
                .filter(v -> v.getParentId() != null && v.getParentId() > 0)
                .collect(Collectors.groupingBy(AsCourseCategoryVO::getParentId));
        // 递归设置children
        voList.forEach(v -> v.setChildren(childrenMap.getOrDefault(v.getId(), new ArrayList<>())));
        // 返回根节点列表
        List<AsCourseCategoryVO> tree = voList.stream()
                .filter(v -> v.getParentId() == null || v.getParentId() == 0)
                .collect(Collectors.toList());
        return JsonResult.buildSuccess(tree);
    }
}
