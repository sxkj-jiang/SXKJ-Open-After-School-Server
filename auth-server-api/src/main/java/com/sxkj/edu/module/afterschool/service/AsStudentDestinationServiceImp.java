package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.AsHeadTeacherStudentDestinationPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentDestinationEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsHeadTeacherStudentDestinationVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentDestinationStatusVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherStudentDestinationClassVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherStudentDestinationTermVO;
import com.sxkj.edu.module.afterschool.mapper.AsStudentDestinationMapper;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.mapper.base.TEduclClassMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsStudentDestinationServiceImp extends ServiceImpl<AsStudentDestinationMapper, AsStudentDestinationEntity>
        implements AsStudentDestinationService{

    @Resource
    private TEduclClassMapper tEduclClassMapper;
    @Autowired
    private AsTeacherMapper asTeacherMapper;
    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;

    @Override
    public AsStudentDestinationStatusVO getBySchoolClassAndStudentIdByStatus(String schoolClass, String studentId) {
        return null;
    }

    @Override
    public AsStudentDestinationEntity getLatestDestinationByStudentId(Long studentId) {
        return this.baseMapper.queryLatestStudentDestination(studentId);
    }

    @Override
    public List<AsTeacherStudentDestinationTermVO> listHeadTeacherStudentDestination(AsHeadTeacherStudentDestinationPageQueryAO queryAO) {
        // 查询教师id与当前学期
        resolveHeadTeacherId(queryAO);
        EduclTermEntity term = resolveQueryTerm(queryAO.getSchoolId(), queryAO.getTermId());
        queryAO.setTermId(term.getId());

        List<AsTeacherStudentDestinationTermVO> result = new ArrayList<>();

        AsTeacherStudentDestinationTermVO termVO = new AsTeacherStudentDestinationTermVO();
        termVO.setTermId(term.getId());
        termVO.setTermName(term.getTermName());

        // 通过学期与班主任教师id查询班级信息与班级人数
        List<AsTeacherStudentDestinationClassVO> classVOList = tEduclClassMapper.listHeadTeacherClassWithStudentCount(queryAO.getHeadTeacherId(), queryAO.getSchoolId(), term.getId(),queryAO.getClassId());
        if (classVOList == null) classVOList = new ArrayList<>();

        //通过班级查询班级下的学生及每个学生的一个最新去向
        List<Long> classIds = classVOList.stream().map(AsTeacherStudentDestinationClassVO::getClassId).collect(Collectors.toList());
        List<AsHeadTeacherStudentDestinationVO> studentList = baseMapper.listHeadTeacherStudentDestination(classIds);

        if(CollectionUtil.isNotEmpty(studentList)){
            for (AsTeacherStudentDestinationClassVO classVO : classVOList) {
                List<AsHeadTeacherStudentDestinationVO> studentDestinationVOS = studentList.stream().filter(e -> e.getClassId().equals(classVO.getClassId())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(studentDestinationVOS)) classVO.setStudentList(studentDestinationVOS);
            }
        }

        termVO.setClassVOList(classVOList);
        result.add(termVO);
        return result;
    }

    /**
     * 兼容班主任端入参：前端可能传教师档案ID，也可能传登录用户ID。
     * 班级表实际存储的是教师档案ID；当入参未绑定班级时，再按 userId 查教师档案并回填。
     */
    private void resolveHeadTeacherId(AsHeadTeacherStudentDestinationPageQueryAO queryAO) {
        if (queryAO == null || queryAO.getHeadTeacherId() == null) {
            return;
        }
        queryAO.setHeadTeacherId(resolveHeadTeacherId(queryAO.getSchoolId(), queryAO.getHeadTeacherId()));
    }

    /**
     * 解析班主任教师档案ID：入参可能已经是教师档案ID，也可能是登录用户ID。
     * 班级表存储的是教师档案ID，已绑定班级时直接保留；否则再按 userId 查询教师档案ID。
     */
    private Long resolveHeadTeacherId(Long schoolId, Long headTeacherId) {
        if (headTeacherId == null) {
            return null;
        }
        if (hasHeadTeacherClass(schoolId, headTeacherId)) {
            return headTeacherId;
        }
        LambdaQueryWrapper<AsTeacherEntity> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.eq(AsTeacherEntity::getIsValid, 1)
                .eq(AsTeacherEntity::getUserId, headTeacherId);
        if (schoolId != null) {
            teacherWrapper.eq(AsTeacherEntity::getSchoolId, schoolId);
        }
        AsTeacherEntity teacher = asTeacherMapper.selectOne(teacherWrapper.last("limit 1"));
        if (teacher != null && teacher.getId() != null) {
            return teacher.getId();
        }

        // 教师多个角色通过号码匹配教师信息
        String phone = Objects.requireNonNull(SmartRequestTokenUtil.getRequestUser()).getEmployeeBO().getPhone();
        if(phone == null) return headTeacherId;
        QueryWrapper<AsTeacherEntity> pWrapper = new QueryWrapper<>();
        pWrapper.eq("is_valid", 1).eq("phone", phone).last("limit 1");
        AsTeacherEntity pTeacher = asTeacherMapper.selectOne(pWrapper);
        if (pTeacher != null) {
            return pTeacher.getId();
        }
        return headTeacherId;
    }

    /**
     * 判断入参是否已经是班级表中的班主任教师档案ID，避免误转换已有正确入参。
     */
    private boolean hasHeadTeacherClass(Long schoolId, Long headTeacherId) {
        LambdaQueryWrapper<TEduclClass> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.eq(TEduclClass::getIsValid, 1)
                .eq(TEduclClass::getHeadTeacherId, headTeacherId);
        if (schoolId != null) {
            classWrapper.eq(TEduclClass::getSchoolId, schoolId);
        }
        Integer count = tEduclClassMapper.selectCount(classWrapper);
        return count != null && count > 0;
    }

    /**
     * 解析跨学期查询范围，日期空档时允许只读回看最近学期。
     */
    private EduclTermEntity resolveQueryTerm(Long schoolId, Long termId) {
        return termId == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, termId);
    }

}
