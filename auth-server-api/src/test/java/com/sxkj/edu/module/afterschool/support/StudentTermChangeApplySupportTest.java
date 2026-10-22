package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentTermChangeLogEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentTermChangeLogMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 学生学期内资料变更生效组件测试。
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentTermChangeApplySupportTest {

    @Mock
    private AsStudentTermChangeLogMapper asStudentTermChangeLogMapper;
    @Mock
    private AsStudentMapper asStudentMapper;
    @Mock
    private EduclTermMapper educlTermMapper;
    @Mock
    private StudentBusinessGuard studentBusinessGuard;

    private StudentTermChangeApplySupport support;

    @Before
    public void setUp() {
        initTableInfo(AsStudentTermChangeLogEntity.class);
        initTableInfo(AsStudentEntity.class);
        support = new StudentTermChangeApplySupport();
        ReflectionTestUtils.setField(support, "asStudentTermChangeLogMapper",
                asStudentTermChangeLogMapper);
        ReflectionTestUtils.setField(support, "asStudentMapper", asStudentMapper);
        ReflectionTestUtils.setField(support, "educlTermMapper", educlTermMapper);
        ReflectionTestUtils.setField(support, "studentBusinessGuard",
                studentBusinessGuard);
    }

    /**
     * 到期查询只返回待生效记录编号。
     */
    @Test
    public void listDueChangeIdsShouldReturnPendingIds() {
        AsStudentTermChangeLogEntity first = new AsStudentTermChangeLogEntity();
        first.setId(11L);
        AsStudentTermChangeLogEntity second = new AsStudentTermChangeLogEntity();
        second.setId(12L);
        when(asStudentTermChangeLogMapper.selectList(any(Wrapper.class)))
                .thenReturn(Arrays.asList(first, second));

        List<Long> result = support.listDueChangeIds(new Date());

        assertEquals(Arrays.asList(11L, 12L), result);
    }

    /**
     * 到期资料变更应更新学生当前资料并完成生效状态。
     */
    @Test
    public void applyPendingChangeShouldUpdateStudentProfile() {
        AsStudentTermChangeLogEntity change = changeLog();
        AsStudentEntity student = student();
        when(asStudentTermChangeLogMapper.update(
                isNull(), any(Wrapper.class))).thenReturn(1);
        when(asStudentTermChangeLogMapper.selectById(11L)).thenReturn(change);
        when(asStudentMapper.selectById(30L)).thenReturn(student);
        when(asStudentMapper.update(isNull(), any(Wrapper.class))).thenReturn(1);

        boolean result = support.applyPendingChange(11L, new Date());

        assertTrue(result);
        ArgumentCaptor<LambdaUpdateWrapper> captor =
                ArgumentCaptor.forClass(LambdaUpdateWrapper.class);
        verify(asStudentMapper).update(isNull(), captor.capture());
        assertTrue(captor.getValue().getParamNameValuePairs()
                .containsValue(5L));
        assertTrue(captor.getValue().getParamNameValuePairs()
                .containsValue(7L));
    }

    /**
     * 已被其他操作修改的学生资料不能被待生效记录覆盖。
     */
    @Test(expected = BaseBusinessException.class)
    public void applyPendingChangeShouldRejectChangedProfile() {
        AsStudentTermChangeLogEntity change = changeLog();
        AsStudentEntity student = student();
        student.setClassId(99L);
        when(asStudentTermChangeLogMapper.update(
                isNull(), any(Wrapper.class))).thenReturn(1);
        when(asStudentTermChangeLogMapper.selectById(11L)).thenReturn(change);
        when(asStudentMapper.selectById(30L)).thenReturn(student);

        support.applyPendingChange(11L, new Date());
    }

    /**
     * 离校类变更到期时，未来学期已有业务必须阻断本次生效。
     */
    @Test(expected = BaseBusinessException.class)
    public void applyPendingChangeShouldRejectFutureSnapshotWithBusiness() {
        AsStudentTermChangeLogEntity change = changeLog();
        change.setNewSchoolRollStatus(2);
        AsStudentEntity student = student();
        AsStudentEntity future = student();
        future.setId(31L);
        future.setTermId(9L);
        when(asStudentTermChangeLogMapper.update(
                isNull(), any(Wrapper.class))).thenReturn(1);
        when(asStudentTermChangeLogMapper.selectById(11L)).thenReturn(change);
        when(asStudentMapper.selectById(30L)).thenReturn(student);
        when(educlTermMapper.selectById(8L)).thenReturn(term(8L, 1000L));
        when(educlTermMapper.selectById(9L)).thenReturn(term(9L, 2000L));
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(future));
        when(studentBusinessGuard.hasStudentBusiness(31L)).thenReturn(true);

        support.applyPendingChange(11L, new Date());
    }

    /**
     * 未成功领取待生效记录时不能更新学生。
     */
    @Test
    public void applyPendingChangeShouldIgnoreUnclaimedRecord() {
        when(asStudentTermChangeLogMapper.update(
                isNull(), any(Wrapper.class))).thenReturn(0);

        boolean result = support.applyPendingChange(11L, new Date());

        assertFalse(result);
        verify(asStudentMapper, never()).selectById(any());
    }

    /**
     * 条件更新未命中时应中止生效，不能把记录错误标记为已完成。
     */
    @Test(expected = BaseBusinessException.class)
    public void applyPendingChangeShouldRejectConcurrentProfileChange() {
        AsStudentTermChangeLogEntity change = changeLog();
        when(asStudentTermChangeLogMapper.update(
                isNull(), any(Wrapper.class))).thenReturn(1);
        when(asStudentTermChangeLogMapper.selectById(11L)).thenReturn(change);
        when(asStudentMapper.selectById(30L)).thenReturn(student());
        when(asStudentMapper.update(isNull(), any(Wrapper.class))).thenReturn(0);

        support.applyPendingChange(11L, new Date());
    }

    /**
     * 初始化纯单元测试所需的实体元数据。
     */
    private void initTableInfo(Class<?> entityClass) {
        if (TableInfoHelper.getTableInfo(entityClass) == null) {
            TableInfoHelper.initTableInfo(
                    new MapperBuilderAssistant(new MybatisConfiguration(), ""),
                    entityClass);
        }
    }

    /**
     * 构造待生效变更记录。
     */
    private AsStudentTermChangeLogEntity changeLog() {
        AsStudentTermChangeLogEntity change =
                new AsStudentTermChangeLogEntity();
        change.setId(11L);
        change.setSchoolId(3L);
        change.setTermId(8L);
        change.setStudentId(30L);
        change.setStudentIdentityId(1001L);
        change.setOldGradeId(4L);
        change.setNewGradeId(5L);
        change.setOldClassId(6L);
        change.setNewClassId(7L);
        change.setOldSchoolRollStatus(1);
        change.setNewSchoolRollStatus(1);
        change.setApplyStatus(CoreConstant.AS_STUDENT_TERM_CHANGE_PENDING);
        change.setIsValid(1);
        return change;
    }

    /**
     * 构造当前学生快照。
     */
    private AsStudentEntity student() {
        AsStudentEntity student = new AsStudentEntity();
        student.setId(30L);
        student.setSchoolId(3L);
        student.setTermId(8L);
        student.setStudentIdentityId(1001L);
        student.setGradeId(4L);
        student.setClassId(6L);
        student.setSchoolRollStatus(1);
        student.setIsValid(1);
        return student;
    }

    /**
     * 构造学期。
     */
    private EduclTermEntity term(Long termId, long startTime) {
        EduclTermEntity term = new EduclTermEntity();
        term.setId(termId);
        term.setSchoolId(3L);
        term.setStartDate(new Date(startTime));
        term.setIsValid(1);
        return term;
    }
}
