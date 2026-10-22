package com.sxkj.edu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsSubjectExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectGradeTreeVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;
import com.sxkj.edu.module.afterschool.mapper.AsSubjectMapper;
import com.sxkj.edu.module.afterschool.service.EduclGradeService;
import com.sxkj.edu.service.AsSubjectService;
import com.sxkj.edu.service.AsTeacherSubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学科管理 服务实现类
 *
 * @author system
 * @date 2026-08-26
 */
@Service
@Slf4j
public class AsSubjectServiceImpl extends ServiceImpl<AsSubjectMapper, AsSubjectEntity>
        implements AsSubjectService {

    @javax.annotation.Resource
    private AsTeacherSubjectService asTeacherSubjectService;

    @javax.annotation.Resource
    private EduclGradeService educlGradeService;

    @Override
    public JsonResult<String> addOrUpdateSubject(AsSubjectAddAO ao) {
        // 唯一性校验：编码+学校 或 年级ID+学科名+学校
        LambdaQueryWrapper<AsSubjectEntity> uniqueWrapper = new LambdaQueryWrapper<>();
        uniqueWrapper.eq(AsSubjectEntity::getIsValid, 1)
                .eq(AsSubjectEntity::getSchoolId, ao.getSchoolId())
                .and(w -> w.eq(AsSubjectEntity::getSubjectCode, ao.getSubjectCode())
                        .or(o -> o.eq(AsSubjectEntity::getGradeId, ao.getGradeId())
                                .eq(AsSubjectEntity::getSubjectName, ao.getSubjectName())));
        if(ObjectUtil.isNotNull(ao.getId())) {
            uniqueWrapper.ne(AsSubjectEntity::getId, ao.getId());
        }
        AsSubjectEntity existing = this.getOne(uniqueWrapper, false);
        if (existing != null) {
            return JsonResult.buildError("该学校下已存在相同编码或年级下存在学科名称相同的学科");
        }
        if (ao.getId() == null) {
            // 新增
            AsSubjectEntity entity = new AsSubjectEntity();
            BeanUtils.copyProperties(ao, entity);
            entity.setCreateTime(new Date());
            entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            this.save(entity);
        } else {
            // 编辑
            AsSubjectEntity entity = this.getById(ao.getId());
            if (entity == null) {
                return JsonResult.buildError("学科不存在");
            }
            BeanUtils.copyProperties(ao, entity);
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            this.updateById(entity);
        }
        return JsonResult.buildSuccess();
    }

    @Override
    public JsonResult<String> updateEnabledStatus(Long id, Integer enabled) {
        JsonResult<String> result = JsonResult.buildError("操作失败");
        AsSubjectEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("学科不存在");
        }
        entity.setEnabled(enabled);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if (flag) {
            return JsonResult.buildSuccess("操作成功");
        }
        return result;
    }

    @Override
    public PageResultDTO<AsSubjectVO> querySubjectPage(AsSubjectQueryAO qo) {
        Page<AsSubjectVO> page = new Page<>(qo.getPageIndex(), qo.getPageSize());
        baseMapper.pageList(page, qo);
        PageResultDTO<AsSubjectVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public List<AsSubjectGradeTreeVO> listSubjectTreeBySchoolId(Long schoolId) {
        List<AsSubjectVO> allSubjects = baseMapper.listEnabledGroupByGrade(schoolId);
        if (CollectionUtils.isEmpty(allSubjects)) {
            return new ArrayList<>();
        }
        // 将年级名称+学科名称拼接为学科名称返回
        return allSubjects.stream().map(s -> {
            AsSubjectGradeTreeVO vo = new AsSubjectGradeTreeVO();
            vo.setSubjectId(s.getId());
            String gradeName = s.getGradeName() != null ? s.getGradeName() : "";
            String subjectName = s.getSubjectName() != null ? s.getSubjectName() : "";
            vo.setSubjectName(gradeName + subjectName);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public JsonResult<AsSubjectVO> getDetailById(Long id) {
        AsSubjectEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("学科不存在");
        }
        AsSubjectVO vo = new AsSubjectVO();
        BeanUtils.copyProperties(entity, vo);
        return JsonResult.buildSuccess(vo);
    }

    @Override
    public JsonResult<String> deleteSubject(Long id) {
        AsSubjectEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("学科不存在");
        }
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if(flag){
            // 批量逻辑删除t_teacher_subject该学科的关联数据
            LambdaUpdateWrapper<AsTeacherSubjectEntity> tsDeleteWrapper = new LambdaUpdateWrapper<>();
            tsDeleteWrapper.eq(AsTeacherSubjectEntity::getSubjectId, id)
                    .eq(AsTeacherSubjectEntity::getIsValid, 1)
                    .set(AsTeacherSubjectEntity::getIsValid, 0)
                    .set(AsTeacherSubjectEntity::getUpdateTime, new Date())
                    .set(AsTeacherSubjectEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
            asTeacherSubjectService.update(tsDeleteWrapper);
        }
        return JsonResult.buildSuccess("删除成功");
    }

    @Override
    public JsonResult<String> importSubject(Long schoolId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<AsSubjectExcel> list = ExcelUtils.customerReadExcel(is, AsSubjectExcel.class, 2);
            if (CollectionUtils.isEmpty(list)) {
                return JsonResult.buildError("导入数据为空");
            }
    
            // 1. 收集年级编码，用于批量查询
            Set<String> gradeCodes = new HashSet<>();
            for (AsSubjectExcel excel : list) {
                if (StrUtil.isNotBlank(excel.getGradeCode())) {
                    gradeCodes.add(excel.getGradeCode());
                }
            }
    
            // 2. 批量查询年级编码映射
            Map<String, Long> gradeCodeToIdMap = new HashMap<>();
            Map<Long, String> gradeIdToNameMap = new HashMap<>();
            if (!gradeCodes.isEmpty()) {
                LambdaQueryWrapper<EduclGradeEntity> gradeWrapper = new LambdaQueryWrapper<>();
                gradeWrapper.eq(EduclGradeEntity::getSchoolId, schoolId)
                        .eq(EduclGradeEntity::getIsValid, 1)
                        .in(EduclGradeEntity::getGradeCode, gradeCodes);
                List<EduclGradeEntity> gradeList = educlGradeService.list(gradeWrapper);
                if (!CollectionUtils.isEmpty(gradeList)) {
                    for (EduclGradeEntity g : gradeList) {
                        gradeCodeToIdMap.put(g.getGradeCode(), g.getId());
                        gradeIdToNameMap.put(g.getId(), g.getGradeName());
                    }
                }
            }

            List<String> errors = new ArrayList<>();
            Set<String> gradeSubjectSet = new HashSet<>();
            Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
            Date now = new Date();
            int row = 3;
            int successCount = 0;
    
            for (AsSubjectExcel excel : list) {
                // 基础校验
                if (StrUtil.isBlank(excel.getSubjectName())) {
                    errors.add("第" + row + "行: 学科名称不能为空");
                    row++;
                    continue;
                }
                if (StrUtil.isBlank(excel.getSubjectCode())) {
                    errors.add("第" + row + "行: 学科编码不能为空");
                    row++;
                    continue;
                }
                if (StrUtil.isBlank(excel.getGradeCode())) {
                    errors.add("第" + row + "行: 年级编码不能为空");
                    row++;
                    continue;
                }
                if (excel.getEnabled() == null) {
                    errors.add("第" + row + "行: 启用状态不能为空");
                    row++;
                    continue;
                }
    
                // 年级编码转换
                Long gradeId = gradeCodeToIdMap.get(excel.getGradeCode());
                if (gradeId == null) {
                    errors.add("第" + row + "行: 年级编码[" + excel.getGradeCode() + "]不存在");
                    row++;
                    continue;
                }
    
                // 批次内年级ID+学科名称去重
                String key = gradeId + "_" + excel.getSubjectName();
                if (!gradeSubjectSet.add(key)) {
                    String gradeName = gradeIdToNameMap.getOrDefault(gradeId, excel.getGradeCode());
                    errors.add("第" + row + "行: 年级[" + gradeName + "]下学科名称[" + excel.getSubjectName() + "]重复");
                    row++;
                    continue;
                }
    
                // 学校ID+学科编码判断新增/更新
                LambdaQueryWrapper<AsSubjectEntity> codeWrapper = new LambdaQueryWrapper<>();
                codeWrapper.eq(AsSubjectEntity::getSchoolId, schoolId)
                        .eq(AsSubjectEntity::getSubjectCode, excel.getSubjectCode())
                        .eq(AsSubjectEntity::getIsValid, 1);
                AsSubjectEntity existing = this.getOne(codeWrapper, false);
    
                if (existing != null) {
                    SmartBeanUtil.copyProperties(excel, existing);
                    existing.setGradeId(gradeId);
                    existing.setUpdateTime(now);
                    existing.setUpdateUser(currentUserId);
                    this.updateById(existing);
                } else {
                    AsSubjectEntity entity = new AsSubjectEntity();
                    SmartBeanUtil.copyProperties(excel, entity);
                    entity.setSchoolId(schoolId);
                    entity.setGradeId(gradeId);
                    entity.setCreateTime(now);
                    entity.setCreateUser(currentUserId);
                    entity.setIsValid(1);
                    this.save(entity);
                }
                successCount++;
                row++;
            }
    
            if (!errors.isEmpty()) {
                return JsonResult.buildError("导入完成，成功" + successCount + "条，失败" + errors.size() + "条：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功，共" + successCount + "条");
        } catch (Exception e) {
            log.error("学科导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    /**
     * 根据code查询详情
     * @param subjectCode 学科编号
     * @return
     */
    public AsSubjectEntity getDetailByCode(String subjectCode){
        LambdaQueryWrapper<AsSubjectEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsSubjectEntity::getIsValid, 1)
                .eq(AsSubjectEntity::getSubjectCode, subjectCode)
                .last("LIMIT 1");
        return getOne(queryWrapper);
    }
}
