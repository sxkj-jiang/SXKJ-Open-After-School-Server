package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceStudentNoticeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceStudentNoticeMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 报名开放期新增学生和新绑定家长提醒组件测试。
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceEnrollmentNoticeSupportTest {

    @Mock
    private AsServiceMapper asServiceMapper;
    @Mock
    private AsCourseClassMapper asCourseClassMapper;
    @Mock
    private AsEnrollmentMapper asEnrollmentMapper;
    @Mock
    private AsStudentMapper asStudentMapper;
    @Mock
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Mock
    private AsServiceStudentNoticeMapper asServiceStudentNoticeMapper;
    @Mock
    private EduclTermMapper educlTermMapper;
    @Mock
    private TBaseUserDao tBaseUserDao;
    @Mock
    private CourseClassScopeSupport courseClassScopeSupport;
    @Mock
    private PlatformTransactionManager transactionManager;
    @Mock
    private TransactionStatus transactionStatus;

    private ServiceEnrollmentNoticeSupport support;

    @Before
    public void setUp() {
        initTableInfo(AsServiceEntity.class);
        initTableInfo(AsCourseClassEntity.class);
        initTableInfo(AsEnrollmentEntity.class);
        initTableInfo(AsStudentEntity.class);
        initTableInfo(AsGuardianStudentEntity.class);
        initTableInfo(TBaseUserEntity.class);
        support = new ServiceEnrollmentNoticeSupport();
        ReflectionTestUtils.setField(support, "asServiceMapper", asServiceMapper);
        ReflectionTestUtils.setField(support, "asCourseClassMapper", asCourseClassMapper);
        ReflectionTestUtils.setField(support, "asEnrollmentMapper", asEnrollmentMapper);
        ReflectionTestUtils.setField(support, "asStudentMapper", asStudentMapper);
        ReflectionTestUtils.setField(support, "asGuardianStudentMapper", asGuardianStudentMapper);
        ReflectionTestUtils.setField(support, "asServiceStudentNoticeMapper",
                asServiceStudentNoticeMapper);
        ReflectionTestUtils.setField(support, "educlTermMapper", educlTermMapper);
        ReflectionTestUtils.setField(support, "tBaseUserDao", tBaseUserDao);
        ReflectionTestUtils.setField(support, "courseClassScopeSupport",
                courseClassScopeSupport);
    }

    /**
     * 验证在籍且注册通过的学生只收到一次包含学期和服务名称的提醒。
     */
    @Test
    public void notifyShouldSendOnceForApprovedActiveStudent() {
        AsStudentEntity student = student();
        AsServiceEntity service = service();
        AsCourseClassEntity course = publicCourse();
        when(asServiceMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(service));
        when(asCourseClassMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(course));
        when(courseClassScopeSupport.isStudentInCourseScope(student, course))
                .thenReturn(true);
        when(asGuardianStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianRelation()));
        when(asServiceStudentNoticeMapper.insert(any(AsServiceStudentNoticeEntity.class)))
                .thenAnswer(invocation -> {
                    AsServiceStudentNoticeEntity notice = invocation.getArgument(0);
                    notice.setId(9001L);
                    return 1;
                });
        when(tBaseUserDao.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianUser()));
        when(educlTermMapper.selectById(2L)).thenReturn(term());

        support.notifyOpenServicesForStudent(student);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        assertTrue(contentCaptor.getValue().contains("2026年秋季学期"));
        assertTrue(contentCaptor.getValue().contains("秋季课后服务"));
        verify(asServiceStudentNoticeMapper, never()).deleteById(any());
    }

    /**
     * 验证同一服务和稳定学生身份已占位时不会重复发送提醒。
     */
    @Test
    public void notifyShouldSkipDuplicateNotice() {
        prepareNoticeCandidates();
        when(asServiceStudentNoticeMapper.insert(any(AsServiceStudentNoticeEntity.class)))
                .thenThrow(new DuplicateKeyException("重复提醒"));

        support.notifyOpenServicesForStudent(student());

        verify(tBaseUserDao, never()).selectList(any(Wrapper.class));
    }

    /**
     * 验证消息发送失败时释放去重占位，便于后续重新补发。
     */
    @Test
    public void notifyShouldReleaseNoticeWhenMessageFails() {
        prepareNoticeCandidates();
        when(asServiceStudentNoticeMapper.insert(any(AsServiceStudentNoticeEntity.class)))
                .thenAnswer(invocation -> {
                    AsServiceStudentNoticeEntity notice = invocation.getArgument(0);
                    notice.setId(9001L);
                    return 1;
                });
        when(tBaseUserDao.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianUser()));
        when(educlTermMapper.selectById(2L)).thenReturn(term());

        support.notifyOpenServicesForStudent(student());

        verify(asServiceStudentNoticeMapper).deleteById(9001L);
    }

    /**
     * 消息服务明确返回失败时也必须释放去重占位。
     */
    @Test
    public void notifyShouldReleaseNoticeWhenMessageReturnsFalse() {
        prepareNoticeCandidates();
        when(asServiceStudentNoticeMapper.insert(any(AsServiceStudentNoticeEntity.class)))
                .thenAnswer(invocation -> {
                    AsServiceStudentNoticeEntity notice = invocation.getArgument(0);
                    notice.setId(9001L);
                    return 1;
                });
        when(tBaseUserDao.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianUser()));
        when(educlTermMapper.selectById(2L)).thenReturn(term());

        support.notifyOpenServicesForStudent(student());

        verify(asServiceStudentNoticeMapper).deleteById(9001L);
    }

    /**
     * 去重记录未成功写入时不能发送消息，避免产生无法防重的提醒。
     */
    @Test
    public void notifyShouldNotSendWhenNoticePlaceholderInsertFails() {
        prepareNoticeCandidates();
        when(asServiceStudentNoticeMapper.insert(any(AsServiceStudentNoticeEntity.class)))
                .thenReturn(0);

        support.notifyOpenServicesForStudent(student());

        verify(tBaseUserDao, never()).selectList(any(Wrapper.class));
    }

    /**
     * 没有公开课程但存在待确认邀请时，新绑定家长仍应收到一次提醒。
     */
    @Test
    public void notifyShouldSendForPendingInvite() {
        when(asServiceMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(service()));
        when(asCourseClassMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asEnrollmentMapper.selectCount(any(Wrapper.class))).thenReturn(1);
        when(asGuardianStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianRelation()));
        when(asServiceStudentNoticeMapper.insert(any(AsServiceStudentNoticeEntity.class)))
                .thenAnswer(invocation -> {
                    AsServiceStudentNoticeEntity notice = invocation.getArgument(0);
                    notice.setId(9001L);
                    return 1;
                });
        when(tBaseUserDao.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianUser()));
        when(educlTermMapper.selectById(2L)).thenReturn(term());

        support.notifyOpenServicesForStudent(student());

    }

    /**
     * 验证待审核或非在籍学生不会触发报名提醒。
     */
    @Test
    public void notifyShouldIgnoreUnavailableStudent() {
        AsStudentEntity pending = student();
        pending.setRegisterStatus(3);
        support.notifyOpenServicesForStudent(pending);

        AsStudentEntity inactive = student();
        inactive.setSchoolRollStatus(2);
        support.notifyOpenServicesForStudent(inactive);

        verify(asServiceMapper, never()).selectList(any(Wrapper.class));
    }

    /**
     * 稳定身份提醒只查询已有学生快照所属学期，不补建未来学期快照。
     */
    @Test
    public void notifyIdentityShouldOnlyUsePreparedTermSnapshots() {
        AsStudentEntity source = student();
        source.setTermId(1L);
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(new ArrayList<>(Collections.singletonList(source)));
        when(asServiceMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());

        support.notifyOpenServicesForIdentity(1L, 1001L);

        ArgumentCaptor<Wrapper> wrapperCaptor =
                ArgumentCaptor.forClass(Wrapper.class);
        verify(asServiceMapper).selectList(wrapperCaptor.capture());
        assertTrue(wrapperCaptor.getValue().getSqlSegment().contains("term_id"));
        verify(asServiceStudentNoticeMapper, never())
                .insert(any(AsServiceStudentNoticeEntity.class));
    }

    /**
     * 独立提醒事务提交失败时也不能向注册和绑定流程继续抛出。
     */
    @Test
    public void notifyShouldIgnoreIndependentTransactionFailure() {
        ReflectionTestUtils.setField(
                support, "transactionManager", transactionManager);
        when(transactionManager.getTransaction(
                any(TransactionDefinition.class))).thenReturn(transactionStatus);
        doThrow(new IllegalStateException("提醒事务提交失败"))
                .when(transactionManager).commit(transactionStatus);

        support.notifyOpenServicesForStudent(null);

        verify(transactionManager).commit(transactionStatus);
    }

    /**
     * 注册事务存在时应等待主事务提交，再启动独立提醒事务读取已提交数据。
     */
    @Test
    public void notifyShouldRunAfterCurrentTransactionCommit() {
        ReflectionTestUtils.setField(
                support, "transactionManager", transactionManager);
        when(transactionManager.getTransaction(
                any(TransactionDefinition.class))).thenReturn(transactionStatus);
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);
        try {
            support.notifyOpenServicesForStudent(null);

            verify(transactionManager, never())
                    .getTransaction(any(TransactionDefinition.class));
            TransactionSynchronizationUtils.triggerAfterCommit();
            verify(transactionManager)
                    .getTransaction(any(TransactionDefinition.class));
            verify(transactionManager).commit(transactionStatus);
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
            TransactionSynchronizationManager.setActualTransactionActive(false);
        }
    }

    /**
     * 准备可发送提醒的服务、课程和家长关系。
     */
    private void prepareNoticeCandidates() {
        AsStudentEntity student = student();
        AsCourseClassEntity course = publicCourse();
        when(asServiceMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(service()));
        when(asCourseClassMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(course));
        when(courseClassScopeSupport.isStudentInCourseScope(any(AsStudentEntity.class),
                any(AsCourseClassEntity.class))).thenReturn(true);
        when(asGuardianStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(guardianRelation()));
    }

    /**
     * 初始化测试所需的 MyBatis-Plus 表元数据。
     */
    private void initTableInfo(Class<?> entityClass) {
        if (TableInfoHelper.getTableInfo(entityClass) == null) {
            TableInfoHelper.initTableInfo(
                    new MapperBuilderAssistant(new MybatisConfiguration(), ""), entityClass);
        }
    }

    /**
     * 创建测试学生快照。
     */
    private AsStudentEntity student() {
        AsStudentEntity student = new AsStudentEntity();
        student.setId(101L);
        student.setSchoolId(1L);
        student.setTermId(2L);
        student.setStudentIdentityId(1001L);
        student.setRegisterStatus(1);
        student.setSchoolRollStatus(1);
        student.setIsValid(1);
        return student;
    }

    /**
     * 创建报名中的未来学期服务。
     */
    private AsServiceEntity service() {
        AsServiceEntity service = new AsServiceEntity();
        service.setId(201L);
        service.setSchoolId(1L);
        service.setTermId(2L);
        service.setServiceName("秋季课后服务");
        service.setServiceStatus(1);
        service.setEnrollStartTime(new Date(System.currentTimeMillis() - 60000));
        service.setEnrollEndTime(new Date(System.currentTimeMillis() + 60000));
        service.setIsValid(1);
        return service;
    }

    /**
     * 创建公开选课课程班。
     */
    private AsCourseClassEntity publicCourse() {
        AsCourseClassEntity course = new AsCourseClassEntity();
        course.setId(301L);
        course.setServiceId(201L);
        course.setEnrollType(1);
        course.setClassStatus(1);
        course.setIsValid(1);
        return course;
    }

    /**
     * 创建稳定身份家长关系。
     */
    private AsGuardianStudentEntity guardianRelation() {
        AsGuardianStudentEntity relation = new AsGuardianStudentEntity();
        relation.setSchoolId(1L);
        relation.setStudentIdentityId(1001L);
        relation.setGuardianUserId(401L);
        relation.setBindStatus(1);
        relation.setIsValid(1);
        return relation;
    }

    /**
     * 创建有效家长用户。
     */
    private TBaseUserEntity guardianUser() {
        TBaseUserEntity user = new TBaseUserEntity();
        user.setId(401L);
        user.setIsValid(1);
        return user;
    }

    /**
     * 创建目标学期。
     */
    private EduclTermEntity term() {
        EduclTermEntity term = new EduclTermEntity();
        term.setId(2L);
        term.setTermName("2026年秋季学期");
        return term;
    }

    /**
     * 创建带时间范围的学期。
     */
    private EduclTermEntity term(Long id, long startTime) {
        EduclTermEntity term = new EduclTermEntity();
        term.setId(id);
        term.setSchoolId(1L);
        term.setStartDate(new Date(startTime));
        term.setIsValid(1);
        return term;
    }
}
