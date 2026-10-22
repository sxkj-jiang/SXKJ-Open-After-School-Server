package com.sxkj.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherSubjectMapper;
import com.sxkj.edu.service.AsTeacherSubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学科教师关联 服务实现类
 *
 * @author system
 * @date 2026-08-26
 */
@Service
@Slf4j
public class AsTeacherSubjectServiceImpl extends ServiceImpl<AsTeacherSubjectMapper, AsTeacherSubjectEntity>
        implements AsTeacherSubjectService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> saveTeacherSubjects(Long schoolId, Long teacherId, List<Long> subjectIds) {
        Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
        Date now = new Date();

        // 1. 逻辑删除该教师已有的有效学科关联
        LambdaUpdateWrapper<AsTeacherSubjectEntity> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(AsTeacherSubjectEntity::getTeacherId, teacherId)
                .eq(AsTeacherSubjectEntity::getIsValid, 1)
                .set(AsTeacherSubjectEntity::getIsValid, 0)
                .set(AsTeacherSubjectEntity::getUpdateTime, now)
                .set(AsTeacherSubjectEntity::getUpdateUser, currentUserId);
        this.update(deleteWrapper);

        // 2. 批量插入新的学科关联（即使之前无关联记录也要正常插入）
        if (!CollectionUtils.isEmpty(subjectIds)) {
            List<AsTeacherSubjectEntity> entities = new ArrayList<>(subjectIds.size());
            for (Long subjectId : subjectIds) {
                AsTeacherSubjectEntity entity = new AsTeacherSubjectEntity();
                entity.setSchoolId(schoolId);
                entity.setTeacherId(teacherId);
                entity.setSubjectId(subjectId);
                entity.setCreateTime(now);
                entity.setCreateUser(currentUserId);
                entity.setIsValid(1);
                entities.add(entity);
            }
            this.saveBatch(entities);
        }

        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public List<AsSubjectVO> listSubjectByTeacherId(Long teacherId) {
        if (!CheckParamUtil.checkLongIdParam(teacherId)) {
            return Collections.emptyList();
        }
        return baseMapper.listSubjectByTeacherId(teacherId);
    }

    @Override
    public Map<Long, List<AsSubjectVO>> listSubjectsByTeacherIds(Collection<Long> teacherIds) {
        if (teacherIds == null || teacherIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<AsSubjectVO> allSubjects = baseMapper.listSubjectsByTeacherIds(teacherIds);
        if (allSubjects == null || allSubjects.isEmpty()) {
            return Collections.emptyMap();
        }
        return allSubjects.stream().collect(Collectors.groupingBy(AsSubjectVO::getTeacherId));
    }
}
