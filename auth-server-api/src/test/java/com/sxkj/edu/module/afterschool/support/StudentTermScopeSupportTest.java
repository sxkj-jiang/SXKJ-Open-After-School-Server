package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 学生学期范围统一解析组件测试。
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentTermScopeSupportTest {

    @Mock
    private AsServiceMapper asServiceMapper;
    @Mock
    private AsCourseClassMapper asCourseClassMapper;
    @Mock
    private AsCourseSessionMapper asCourseSessionMapper;
    @Mock
    private AsStudentMapper asStudentMapper;
    @Mock
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Mock
    private EduclTermMapper educlTermMapper;

    private StudentTermScopeSupport support;

    /**
     * 初始化组件和 MyBatis-Plus 元数据。
     */
    @Before
    public void setUp() {
        initTableInfo(EduclTermEntity.class);
        initTableInfo(AsGuardianStudentEntity.class);
        initTableInfo(AsStudentEntity.class);
        support = new StudentTermScopeSupport();
        ReflectionTestUtils.setField(support, "asServiceMapper", asServiceMapper);
        ReflectionTestUtils.setField(support, "asCourseClassMapper", asCourseClassMapper);
        ReflectionTestUtils.setField(support, "asCourseSessionMapper", asCourseSessionMapper);
        ReflectionTestUtils.setField(support, "asStudentMapper", asStudentMapper);
        ReflectionTestUtils.setField(support, "asGuardianStudentMapper", asGuardianStudentMapper);
        ReflectionTestUtils.setField(support, "educlTermMapper", educlTermMapper);
    }

    /**
     * 课程班应沿课后服务取得学校和学期。
     */
    @Test
    public void courseClassShouldResolveTermFromService() {
        when(asCourseClassMapper.selectById(20L)).thenReturn(courseClass(20L, 10L, 1L));
        when(asServiceMapper.selectById(10L)).thenReturn(service(10L, 1L, 100L));
        when(educlTermMapper.selectById(100L)).thenReturn(term(100L, 1L));

        StudentTermScope scope = support.requireCourseClassScope(20L);

        assertEquals(Long.valueOf(1L), scope.getSchoolId());
        assertEquals(Long.valueOf(100L), scope.getTermId());
        assertEquals(Long.valueOf(10L), scope.getServiceId());
        assertEquals(Long.valueOf(20L), scope.getCourseClassId());
    }

    /**
     * 学生快照学期与业务学期不一致时必须拒绝。
     */
    @Test
    public void studentFromAnotherTermShouldBeRejected() {
        AsStudentEntity student = new AsStudentEntity();
        student.setId(30L);
        student.setSchoolId(1L);
        student.setTermId(99L);
        student.setIsValid(1);
        when(asStudentMapper.selectById(30L)).thenReturn(student);

        try {
            support.requireStudentInScope(30L,
                    new StudentTermScope(1L, 100L, 10L, null, null));
            fail("跨学期学生请求应被拒绝");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("同一学期"));
        }
    }

    /**
     * 学校当前日期存在多个学期时不能随机选取。
     */
    @Test
    public void multipleDateCurrentTermsShouldBeRejected() {
        when(educlTermMapper.selectList(any())).thenReturn(Arrays.asList(
                term(100L, 1L), term(101L, 1L)));

        try {
            support.requireDefaultTerm(1L);
            fail("多个日期当前学期应被拒绝");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("唯一有效学期"));
        }
    }

    /**
     * 日期当前学期不存在时写业务必须阻断。
     */
    @Test
    public void writeBusinessShouldBeRejectedWhenDateCurrentTermMissing() {
        when(educlTermMapper.selectList(any())).thenReturn(Collections.emptyList());

        try {
            support.requireDefaultTerm(1L);
            fail("日期当前学期不存在时写业务应被拒绝");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("唯一有效学期"));
        }
    }

    /**
     * 日期当前学期不存在时只读业务应回退最近已结束学期。
     */
    @Test
    public void readBusinessShouldFallbackToLatestEndedTerm() {
        EduclTermEntity latestEndedTerm = term(99L, 1L);
        when(educlTermMapper.selectList(any()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.singletonList(latestEndedTerm));

        EduclTermEntity result = support.resolveReadableTerm(1L);

        assertEquals(Long.valueOf(99L), result.getId());
    }

    /**
     * 课次应沿课程班和课后服务取得业务学期。
     */
    @Test
    public void sessionShouldResolveBusinessTerm() {
        AsCourseSessionEntity session = new AsCourseSessionEntity();
        session.setId(40L);
        session.setSchoolId(1L);
        session.setCourseClassId(20L);
        session.setIsValid(1);
        when(asCourseSessionMapper.selectById(40L)).thenReturn(session);
        when(asCourseClassMapper.selectById(20L)).thenReturn(courseClass(20L, 10L, 1L));
        when(asServiceMapper.selectById(10L)).thenReturn(service(10L, 1L, 100L));
        when(educlTermMapper.selectById(100L)).thenReturn(term(100L, 1L));

        StudentTermScope scope = support.requireSessionScope(40L);

        assertEquals(Long.valueOf(100L), scope.getTermId());
        assertEquals(Long.valueOf(40L), scope.getSessionId());
    }

    /**
     * 家长绑定多个孩子且未指定稳定身份时必须提示先选择学生。
     */
    @Test
    public void guardianMultipleStudentsShouldRequireIdentitySelection() {
        when(asGuardianStudentMapper.selectList(any())).thenReturn(Arrays.asList(
                guardianRelation(1001L), guardianRelation(1002L)));

        try {
            support.resolveGuardianStudent(900L, 1L, 100L);
            fail("多孩子场景未指定稳定身份时应拒绝随机选择");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("请先选择学生"));
        }
    }

    /**
     * 家长指定稳定身份后应映射到目标学期唯一学生快照。
     */
    @Test
    public void guardianShouldResolveRequestedIdentityInTargetTerm() {
        when(asGuardianStudentMapper.selectList(any()))
                .thenReturn(Collections.singletonList(guardianRelation(1002L)));
        AsStudentEntity student = new AsStudentEntity();
        student.setId(32L);
        student.setSchoolId(1L);
        student.setTermId(100L);
        student.setStudentIdentityId(1002L);
        student.setIsValid(1);
        when(asStudentMapper.selectList(any()))
                .thenReturn(Collections.singletonList(student));

        AsStudentEntity result =
                support.resolveGuardianStudent(900L, 1L, 100L, 1002L);

        assertEquals(Long.valueOf(32L), result.getId());
        assertEquals(Long.valueOf(1002L), result.getStudentIdentityId());
    }

    private void initTableInfo(Class<?> entityClass) {
        if (TableInfoHelper.getTableInfo(entityClass) == null) {
            TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), entityClass);
        }
    }

    private AsServiceEntity service(Long id, Long schoolId, Long termId) {
        AsServiceEntity entity = new AsServiceEntity();
        entity.setId(id);
        entity.setSchoolId(schoolId);
        entity.setTermId(termId);
        entity.setIsValid(1);
        return entity;
    }

    private AsCourseClassEntity courseClass(Long id, Long serviceId, Long schoolId) {
        AsCourseClassEntity entity = new AsCourseClassEntity();
        entity.setId(id);
        entity.setServiceId(serviceId);
        entity.setSchoolId(schoolId);
        entity.setIsValid(1);
        return entity;
    }

    private EduclTermEntity term(Long id, Long schoolId) {
        EduclTermEntity entity = new EduclTermEntity();
        entity.setId(id);
        entity.setSchoolId(schoolId);
        entity.setTermName("测试学期");
        entity.setIsValid(1);
        return entity;
    }

    /**
     * 创建稳定身份家长关系。
     */
    private AsGuardianStudentEntity guardianRelation(Long studentIdentityId) {
        AsGuardianStudentEntity relation = new AsGuardianStudentEntity();
        relation.setSchoolId(1L);
        relation.setGuardianUserId(900L);
        relation.setStudentIdentityId(studentIdentityId);
        relation.setBindStatus(1);
        relation.setIsValid(1);
        return relation;
    }
}
