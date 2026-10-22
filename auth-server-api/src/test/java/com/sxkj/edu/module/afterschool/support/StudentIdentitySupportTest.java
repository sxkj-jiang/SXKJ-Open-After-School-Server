package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentIdentityEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentNoChangeLogEntity;
import com.sxkj.edu.module.afterschool.mapper.AsStudentIdentityMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentNoChangeLogMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 学生稳定身份与学号纠错组件测试。
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentIdentitySupportTest {

    @Mock
    private AsStudentIdentityMapper asStudentIdentityMapper;
    @Mock
    private AsStudentNoChangeLogMapper asStudentNoChangeLogMapper;
    @Mock
    private AsStudentMapper asStudentMapper;

    private StudentIdentitySupport support;

    @Before
    public void setUp() {
        initTableInfo(AsStudentEntity.class);
        initTableInfo(AsStudentIdentityEntity.class);
        initTableInfo(AsStudentNoChangeLogEntity.class);
        support = new StudentIdentitySupport();
        ReflectionTestUtils.setField(
                support, "asStudentIdentityMapper", asStudentIdentityMapper);
        ReflectionTestUtils.setField(
                support, "asStudentNoChangeLogMapper", asStudentNoChangeLogMapper);
        ReflectionTestUtils.setField(support, "asStudentMapper", asStudentMapper);
    }

    /**
     * 学号纠错必须保持稳定身份不变，并同步全部有效学期快照与审计记录。
     */
    @Test
    public void correctStudentNoShouldKeepIdentityAndUpdateSnapshots() {
        AsStudentEntity selected = student();
        AsStudentIdentityEntity identity = identity();
        when(asStudentIdentityMapper.selectById(1001L)).thenReturn(identity);
        when(asStudentIdentityMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asStudentNoChangeLogMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asStudentMapper.selectCount(any(Wrapper.class))).thenReturn(0);
        when(asStudentIdentityMapper.updateById(any(AsStudentIdentityEntity.class))).thenReturn(1);
        when(asStudentMapper.update(any(AsStudentEntity.class), any(Wrapper.class))).thenReturn(1);
        when(asStudentNoChangeLogMapper.insert(any(AsStudentNoChangeLogEntity.class))).thenReturn(1);

        support.correctStudentNo(selected, " S009 ", "学校录入错误", 99L);

        assertEquals(Long.valueOf(1001L), identity.getId());
        assertEquals("S009", identity.getStudentNo());
        verify(asStudentIdentityMapper).updateById(identity);

        ArgumentCaptor<AsStudentEntity> snapshotCaptor =
                ArgumentCaptor.forClass(AsStudentEntity.class);
        verify(asStudentMapper).update(
                snapshotCaptor.capture(), any(Wrapper.class));
        assertEquals("S009", snapshotCaptor.getValue().getStudentNo());

        ArgumentCaptor<AsStudentNoChangeLogEntity> logCaptor =
                ArgumentCaptor.forClass(AsStudentNoChangeLogEntity.class);
        verify(asStudentNoChangeLogMapper).insert(logCaptor.capture());
        assertEquals(Long.valueOf(1001L),
                logCaptor.getValue().getStudentIdentityId());
        assertEquals("S001", logCaptor.getValue().getOldStudentNo());
        assertEquals("S009", logCaptor.getValue().getNewStudentNo());
        assertEquals("学校录入错误", logCaptor.getValue().getChangeReason());
    }

    /**
     * 学期快照更新失败时必须抛出异常，交由事务回滚稳定身份修改。
     */
    @Test
    public void correctStudentNoShouldFailWhenSnapshotUpdateFails() {
        AsStudentEntity selected = student();
        when(asStudentIdentityMapper.selectById(1001L)).thenReturn(identity());
        when(asStudentIdentityMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asStudentNoChangeLogMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asStudentMapper.selectCount(any(Wrapper.class))).thenReturn(0);
        when(asStudentIdentityMapper.updateById(any(AsStudentIdentityEntity.class))).thenReturn(1);
        when(asStudentMapper.update(any(AsStudentEntity.class), any(Wrapper.class))).thenReturn(0);

        try {
            support.correctStudentNo(selected, "S009", "学校录入错误", 99L);
            fail("学期快照更新失败时应抛出异常");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("学期快照"));
        }
    }

    /**
     * 稳定身份资料更新失败时必须抛出异常，避免学期快照与稳定身份资料不一致。
     */
    @Test
    public void updateIdentityProfileShouldFailWhenDatabaseUpdateFails() {
        when(asStudentIdentityMapper.selectById(1001L)).thenReturn(identity());
        when(asStudentIdentityMapper.updateById(
                any(AsStudentIdentityEntity.class))).thenReturn(0);

        try {
            support.updateIdentityProfile(student(), 99L);
            fail("稳定身份资料更新失败时应抛出异常");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("稳定身份资料"));
        }
    }

    /**
     * 旧学号只能归原稳定身份使用，不允许分配给其他学生。
     */
    @Test(expected = BaseBusinessException.class)
    public void historicalStudentNoShouldNotBeReusedByAnotherIdentity() {
        AsStudentNoChangeLogEntity history = new AsStudentNoChangeLogEntity();
        history.setSchoolId(1L);
        history.setStudentIdentityId(1001L);
        history.setOldStudentNo("S001");
        history.setIsValid(1);
        when(asStudentIdentityMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asStudentNoChangeLogMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(history));

        support.assertStudentNoAvailable(1L, "S001", 2002L);
    }

    /**
     * 旧学号审计记录不阻断原稳定身份自身的合法校验。
     */
    @Test
    public void historicalStudentNoShouldRemainAvailableForOriginalIdentity() {
        AsStudentNoChangeLogEntity history = new AsStudentNoChangeLogEntity();
        history.setSchoolId(1L);
        history.setStudentIdentityId(1001L);
        history.setOldStudentNo("S001");
        history.setIsValid(1);
        when(asStudentIdentityMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());
        when(asStudentNoChangeLogMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(history));

        support.assertStudentNoAvailable(1L, "S001", 1001L);

        verify(asStudentNoChangeLogMapper).selectList(any(Wrapper.class));
    }

    /**
     * 入学年份为空时，应只初始化稳定身份，不修改学生学期快照。
     */
    @Test
    public void initializeEnrollmentYearShouldUpdateEmptyIdentity() {
        AsStudentIdentityEntity identity = identity();
        when(asStudentIdentityMapper.update(
                any(), any(Wrapper.class))).thenReturn(1);

        AsStudentIdentityEntity result =
                support.initializeEnrollmentYear(identity, 2026, 99L);

        assertEquals(Integer.valueOf(2026), result.getEnrollmentYear());
        verify(asStudentIdentityMapper).update(
                any(), any(Wrapper.class));
    }

    /**
     * 已有入学年份与请求不一致时必须阻断，避免自动升班基准被静默改写。
     */
    @Test(expected = BaseBusinessException.class)
    public void initializeEnrollmentYearShouldRejectConflict() {
        AsStudentIdentityEntity identity = identity();
        identity.setEnrollmentYear(2025);

        support.initializeEnrollmentYear(identity, 2026, 99L);
    }

    /**
     * 并发操作先写入不同年份时，应重读并阻断当前操作。
     */
    @Test(expected = BaseBusinessException.class)
    public void initializeEnrollmentYearShouldRejectConcurrentConflict() {
        AsStudentIdentityEntity identity = identity();
        AsStudentIdentityEntity concurrent = identity();
        concurrent.setEnrollmentYear(2025);
        when(asStudentIdentityMapper.update(
                any(), any(Wrapper.class))).thenReturn(0);
        when(asStudentIdentityMapper.selectById(1001L)).thenReturn(concurrent);

        support.initializeEnrollmentYear(identity, 2026, 99L);
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
     * 创建学生学期快照。
     */
    private AsStudentEntity student() {
        AsStudentEntity student = new AsStudentEntity();
        student.setId(11L);
        student.setSchoolId(1L);
        student.setTermId(2L);
        student.setStudentIdentityId(1001L);
        student.setStudentNo("S001");
        student.setStudentName("张三");
        student.setIsValid(1);
        return student;
    }

    /**
     * 创建学生稳定身份。
     */
    private AsStudentIdentityEntity identity() {
        AsStudentIdentityEntity identity = new AsStudentIdentityEntity();
        identity.setId(1001L);
        identity.setSchoolId(1L);
        identity.setStudentNo("S001");
        identity.setStudentName("张三");
        identity.setIsValid(1);
        return identity;
    }
}
