package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.AsHeadTeacherClassStudentInfoAO;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.vo.AsHeadTeacherClassStudentInfoVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherClassQueryDto;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.vo.AsHeadTeacherClassStudentListVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherBindClassVO;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.service.AsTeacherClassService;
import com.sxkj.edu.module.afterschool.service.EduclGradeService;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.mapper.base.TEduclClassMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsTeacherClassServiceImpl extends ServiceImpl<AsTeacherClassMapper, AsTeacherClassEntity> implements AsTeacherClassService {

    @Resource
    private AsTeacherMapper asTeacherMapper;

    @Resource
    private TEduclClassMapper tEduclClassMapper;

    @Resource
    private AsStudentMapper asStudentMapper;

    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;
    @Autowired
    private EduclGradeService educlGradeService;


    @Override
    public Boolean createTeacherBindClass(Long schoolId, List<Long> classIds, Long teacherId, Integer bindType) {
        // 先逻辑删除教师之前的任课班级绑定关系（bind_type=2）
        LambdaQueryWrapper<AsTeacherClassEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsTeacherClassEntity::getTeacherId, teacherId)
                .eq(AsTeacherClassEntity::getSchoolId, schoolId)
                .eq(AsTeacherClassEntity::getBindType, 2)
                .eq(AsTeacherClassEntity::getIsValid, 1);
        List<AsTeacherClassEntity> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            for (AsTeacherClassEntity entity : list) {
                entity.setIsValid(0);
                entity.setUpdateTime(new Date());
                entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            }
            boolean update = this.updateBatchById(list);
        }

        // 批量创建新的任课班级绑定关系
        if (CollectionUtil.isNotEmpty(classIds)) {
            for (Long classId : classIds) {
                AsTeacherClassEntity entity = new AsTeacherClassEntity();
                entity.setSchoolId(schoolId);
                entity.setClassId(classId);
                entity.setTeacherId(teacherId);
                entity.setBindType(bindType);
                entity.setCreateTime(new Date());
                entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                entity.setIsValid(1);
                this.save(entity);
            }
            log.info("call createTeacherBindClass schoolId:{},teacherId:{},classIds:{},bindType:{}", schoolId, teacherId, classIds, bindType);
        }
        return true;
    }

    @Override
    public AsTeacherClassQueryDto getTeacherBySchoolAndClass(Long schoolId, Long classId) {
        AsTeacherClassQueryDto teacherClassQueryDto = baseMapper.queryTeacherClassByTeacherName(schoolId, classId);
        return teacherClassQueryDto;
    }


    private AsTeacherClassEntity checkExistsByName(Long schoolId, Long classId, Long teacherId) {
        AsTeacherClassEntity entity = null;
        LambdaQueryWrapper<AsTeacherClassEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsTeacherClassEntity::getIsValid, 1)
                .eq(AsTeacherClassEntity::getSchoolId, schoolId)
                .eq(AsTeacherClassEntity::getTeacherId, teacherId)
                .eq(AsTeacherClassEntity::getClassId, classId);
        List<AsTeacherClassEntity> list = this.list(w);
        if(CollectionUtil.isNotEmpty(list)) entity = list.get(0);
        return entity;
    }

    @Override
    public List<AsHeadTeacherClassStudentListVO> queryHeadTeacherClassStudentList(
            Long userTeacherId, Long schoolId, Long termId) {
        EduclTermEntity term = termId == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, termId);
        // 1. 根据教师用户ID查询教师档案

        // 2. 查询班主任绑定的班级(bind_type=1)
        LambdaQueryWrapper<AsTeacherClassEntity> tcWrapper = new LambdaQueryWrapper<>();
        tcWrapper.eq(AsTeacherClassEntity::getTeacherId, userTeacherId)
                .eq(AsTeacherClassEntity::getSchoolId, schoolId)
                .eq(AsTeacherClassEntity::getBindType, 1)
                .eq(AsTeacherClassEntity::getIsValid, 1);
        List<AsTeacherClassEntity> teacherClasses = this.list(tcWrapper);
        if (CollectionUtil.isEmpty(teacherClasses)) {
            return Collections.emptyList();
        }

        // 3. 提取班级ID集合
        List<Long> classIds = teacherClasses.stream()
                .map(AsTeacherClassEntity::getClassId)
                .distinct()
                .collect(Collectors.toList());

        // 4. 批量查询班级信息
        LambdaQueryWrapper<TEduclClass> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.in(TEduclClass::getId, classIds)
                .eq(TEduclClass::getIsValid, 1);
        List<TEduclClass> eduClasses = tEduclClassMapper.selectList(classWrapper);

        // 5. 批量查询班级下的学生
        LambdaQueryWrapper<AsStudentEntity> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.in(AsStudentEntity::getClassId, classIds)
                .eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, term.getId())
                .eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getRegisterStatus, 1)
                .orderByAsc(AsStudentEntity::getStudentNo);
        List<AsStudentEntity> allStudents = asStudentMapper.selectList(studentWrapper);

        // 6. 按班级分组组装结果
        List<AsHeadTeacherClassStudentListVO> result = new ArrayList<>();
        for (TEduclClass clazz : eduClasses) {
            AsHeadTeacherClassStudentListVO vo = new AsHeadTeacherClassStudentListVO();
            vo.setTermId(term.getId());
            vo.setTermName(term.getTermName());
            vo.setClassId(clazz.getId());
            EduclGradeEntity educlGrade = educlGradeService.getById(clazz.getGradeId());
            if(null != educlGrade){
                vo.setClassName(educlGrade.getGradeName() + clazz.getClassName());
            }else {
                vo.setClassName(clazz.getClassName());
            }


            List<AsHeadTeacherClassStudentListVO.StudentItem> studentItems = allStudents.stream()
                    .filter(s -> clazz.getId().equals(s.getClassId()))
                    .map(s -> {
                        AsHeadTeacherClassStudentListVO.StudentItem item = new AsHeadTeacherClassStudentListVO.StudentItem();
                        item.setStudentId(s.getId());
                        item.setTermId(term.getId());
                        item.setTermName(term.getTermName());
                        item.setStudentName(s.getStudentName());
                        item.setStudentNo(s.getStudentNo());
                        item.setGender(s.getGender());
                        return item;
                    })
                    .collect(Collectors.toList());
            vo.setStudents(studentItems);
            result.add(vo);
        }
        return result;
    }

    @Override
    public void syncHeadTeacherClass(Long classId, Long headTeacherId, Long schoolId) {
        // 查询同一学校同一班级是否已存在班主任绑定记录
        LambdaQueryWrapper<AsTeacherClassEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsTeacherClassEntity::getSchoolId, schoolId)
                .eq(AsTeacherClassEntity::getClassId, classId)
                .eq(AsTeacherClassEntity::getBindType, 1)
                .eq(AsTeacherClassEntity::getIsValid, 1);
        AsTeacherClassEntity existing = this.getOne(queryWrapper, false);

        if (existing != null) {
            // 已存在则更新教师ID
            existing.setTeacherId(headTeacherId);
            existing.setUpdateTime(new Date());
            if(ObjectUtil.isNull(SmartRequestTokenUtil.getRequestUserId()) || 0L == SmartRequestTokenUtil.getRequestUserId()) {
                existing.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            }
            this.updateById(existing);
            log.info("syncHeadTeacherClass 更新 schoolId:{}, classId:{}, headTeacherId:{}", schoolId, classId, headTeacherId);
        } else {
            // 不存在则新增
            AsTeacherClassEntity entity = new AsTeacherClassEntity();
            entity.setSchoolId(schoolId);
            entity.setClassId(classId);
            entity.setTeacherId(headTeacherId);
            entity.setBindType(1);
            entity.setCreateTime(new Date());
            entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            entity.setIsValid(1);
            boolean uptRow = this.save(entity);
            log.info("syncHeadTeacherClass 新增 schoolId:{}, classId:{}, headTeacherId:{}, uptRow:{}", schoolId, classId, headTeacherId, uptRow);
        }
    }

    @Override
    public List<AsTeacherBindClassVO> queryTeacherBindClassList(Long teacherId, Long schoolId) {
        LambdaQueryWrapper<AsTeacherClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsTeacherClassEntity::getTeacherId, teacherId)
                .eq(AsTeacherClassEntity::getSchoolId, schoolId)
                .eq(AsTeacherClassEntity::getBindType, 2)
                .eq(AsTeacherClassEntity::getIsValid, 1);
        List<AsTeacherClassEntity> list = this.list(wrapper);
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<Long> classIdList = list.stream()
                .map(AsTeacherClassEntity::getClassId)
                .filter(CheckParamUtil::checkLongIdParam)
                .distinct()
                .collect(Collectors.toList());

        List<TEduclClass> tEduclClasses = tEduclClassMapper.selectList(new LambdaQueryWrapper<TEduclClass>()
                .in(TEduclClass::getId, classIdList)
                .eq(TEduclClass::getIsValid, 1));
        if (CollectionUtil.isEmpty(tEduclClasses)) {
            return Collections.emptyList();
        }

        // 查询年级名称，拼接为"年级名称+班级名称"格式
        List<Long> gradeIds = tEduclClasses.stream()
                .map(TEduclClass::getGradeId)
                .filter(CheckParamUtil::checkLongIdParam)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> gradeNameMap = Collections.emptyMap();
        if (CollectionUtil.isNotEmpty(gradeIds)) {
            List<EduclGradeEntity> gradeList = educlGradeService.list(new LambdaQueryWrapper<EduclGradeEntity>()
                    .in(EduclGradeEntity::getId, gradeIds)
                    .eq(EduclGradeEntity::getIsValid, 1));
            if (CollectionUtil.isNotEmpty(gradeList)) {
                gradeNameMap = gradeList.stream()
                        .collect(Collectors.toMap(EduclGradeEntity::getId, EduclGradeEntity::getGradeName, (a, b) -> a));
            }
        }
        Map<Long, String> finalGradeNameMap = gradeNameMap;
        return tEduclClasses.stream().map(c -> {
            AsTeacherBindClassVO vo = new AsTeacherBindClassVO();
            vo.setClassId(c.getId());
            String gradeName = finalGradeNameMap.getOrDefault(c.getGradeId(), "");
            vo.setClassName(gradeName + c.getClassName());
            return vo;
        }).collect(Collectors.toList());
    }
    @Override
    public List<AsHeadTeacherClassStudentInfoVO> queryClassStudentInfo(AsHeadTeacherClassStudentInfoAO ao) {
        if (ObjectUtil.isNull(ao.getTeacherId())) {
            return Collections.emptyList();
        }
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        ao.setSchoolId(schoolId);
        return baseMapper.listClassStudentInfo(ao);
    }
}

