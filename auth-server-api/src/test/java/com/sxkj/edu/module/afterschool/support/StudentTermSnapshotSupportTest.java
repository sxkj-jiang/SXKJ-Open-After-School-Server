package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentIdentityEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.afterschool.service.EduclClassService;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 学生学期快照准备组件测试。
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentTermSnapshotSupportTest {

    @Mock
    private AsStudentMapper asStudentMapper;
    @Mock
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Mock
    private EduclTermMapper educlTermMapper;
    @Mock
    private EduclGradeMapper educlGradeMapper;
    @Mock
    private EduclClassService educlClassService;
    @Mock
    private StudentIdentitySupport studentIdentitySupport;

    private StudentTermSnapshotSupport support;

    @Before
    public void setUp() {
        initTableInfo(AsStudentEntity.class);
        initTableInfo(AsGuardianStudentEntity.class);
        initTableInfo(EduclGradeEntity.class);
        initTableInfo(EduclTermEntity.class);
        initTableInfo(TEduclClass.class);
        support = new StudentTermSnapshotSupport();
        ReflectionTestUtils.setField(support, "asStudentMapper", asStudentMapper);
        ReflectionTestUtils.setField(support, "asGuardianStudentMapper", asGuardianStudentMapper);
        ReflectionTestUtils.setField(support, "educlTermMapper", educlTermMapper);
        ReflectionTestUtils.setField(support, "educlGradeMapper", educlGradeMapper);
        ReflectionTestUtils.setField(support, "educlClassService", educlClassService);
        ReflectionTestUtils.setField(support, "studentIdentitySupport", studentIdentitySupport);
        EduclGradeEntity defaultGrade = new EduclGradeEntity();
        defaultGrade.setId(101L);
        defaultGrade.setSchoolId(1L);
        defaultGrade.setGradeName("一年级");
        defaultGrade.setIsValid(1);
        AsStudentIdentityEntity defaultIdentity = identity(2025);
        lenient().when(studentIdentitySupport.resolveOrCreateIdentity(
                        any(AsStudentEntity.class), any()))
                .thenReturn(defaultIdentity);
        lenient().when(studentIdentitySupport.requireIdentity(any(), any()))
                .thenReturn(defaultIdentity);
        lenient().when(educlGradeMapper.selectOne(any(Wrapper.class)))
                .thenReturn(defaultGrade);
    }

    /**
     * 验证批量准备目标学期快照具有幂等性，且不复制稳定家长关系。
     */
    @Test
    public void prepareShouldBeIdempotentAndNotCopyGuardianRelation() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        AsStudentEntity target = student(null, 2L, "S001");

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(sourceTerm),
                        Collections.singletonList(sourceTerm));
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList(), Collections.singletonList(source),
                        Collections.singletonList(target), Collections.singletonList(source));
        when(asStudentMapper.insert(any(AsStudentEntity.class))).thenAnswer(invocation -> {
            AsStudentEntity inserted = invocation.getArgument(0);
            inserted.setId(12L);
            target.setId(12L);
            return 1;
        });

        List<AsStudentEntity> first = support.prepareSchoolTermSnapshots(1L, 2L);
        List<AsStudentEntity> second = support.prepareSchoolTermSnapshots(1L, 2L);

        assertEquals(1, first.size());
        assertEquals(Long.valueOf(12L), first.get(0).getId());
        assertEquals(1, second.size());
        verify(asStudentMapper, times(1)).insert(any(AsStudentEntity.class));
        verify(asGuardianStudentMapper, never()).insert(any(AsGuardianStudentEntity.class));
        verify(educlTermMapper, never()).updateById(any(EduclTermEntity.class));
    }

    /**
     * 只读查询应直接返回已经准备完成的目标学期快照。
     */
    @Test
    public void findPreparedSnapshotShouldReturnExistingTargetWithoutInsert()
            throws Exception {
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        AsStudentEntity target = student(12L, 2L, "S001");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(target);

        AsStudentEntity result =
                support.findPreparedSnapshot(source, 1L, 2L);

        assertSame(target, result);
        verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
        verify(studentIdentitySupport, never())
                .resolveOrCreateIdentity(any(AsStudentEntity.class), any());
    }

    /**
     * 目标学期快照不存在时只读查询应返回空，不能按需创建。
     */
    @Test
    public void findPreparedSnapshotShouldReturnNullWithoutInsert()
            throws Exception {
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);

        AsStudentEntity result =
                support.findPreparedSnapshot(source, 1L, 2L);

        assertNull(result);
        verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
        verify(studentIdentitySupport, never())
                .resolveOrCreateIdentity(any(AsStudentEntity.class), any());
    }

    /**
     * 候选列表查询遇到非在籍学生时应返回空，不能影响其他学生展示。
     */
    @Test
    public void findEligiblePreparedSnapshotShouldSkipInactiveStudent()
            throws Exception {
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        AsStudentEntity inactiveTarget = student(12L, 2L, "S001");
        inactiveTarget.setSchoolRollStatus(2);
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(inactiveTarget);

        AsStudentEntity result = support.findEligiblePreparedSnapshot(
                source, 1L, 2L);

        assertNull(result);
        verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
    }

    /**
     * 严格查询遇到非在籍学生时仍应报错，不能因候选列表兼容而放宽业务校验。
     */
    @Test
    public void findPreparedSnapshotShouldStillRejectInactiveStudent()
            throws Exception {
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity inactiveStudent = student(12L, 2L, "S001");
        inactiveStudent.setSchoolRollStatus(2);
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);

        try {
            support.findPreparedSnapshot(inactiveStudent, 1L, 2L);
        } catch (BaseBusinessException exception) {
            assertEquals("学生当前不在籍，不能参与目标学期业务",
                    exception.getErrorMsg());
            return;
        }
        throw new AssertionError("非在籍学生不应通过严格业务校验");
    }

    /**
     * 紧邻上一学期没有学生时，应继续使用更早学期的最新有效快照。
     */
    @Test
    public void prepareShouldUseLatestAvailableOlderSnapshot() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-01-10");
        EduclTermEntity emptyTerm = term(2L, "2026-02-10");
        EduclTermEntity targetTerm = term(3L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");

        when(educlTermMapper.selectById(3L)).thenReturn(targetTerm);
        when(educlTermMapper.selectList(any(Wrapper.class)))
                .thenReturn(Arrays.asList(emptyTerm, sourceTerm));
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList(), Collections.singletonList(source));
        when(asStudentMapper.insert(any(AsStudentEntity.class))).thenAnswer(invocation -> {
            AsStudentEntity inserted = invocation.getArgument(0);
            inserted.setId(12L);
            return 1;
        });

        List<AsStudentEntity> targets =
                support.prepareSchoolTermSnapshots(1L, 3L);

        assertEquals(1, targets.size());
        assertEquals(Long.valueOf(3L), targets.get(0).getTermId());
        verify(asStudentMapper).insert(any(AsStudentEntity.class));
    }

    /**
     * 最新快照已经休学或转学时，不能回退到更早的在籍快照生成新学期学生。
     */
    @Test
    public void prepareShouldNotFallbackWhenLatestSnapshotIsInactive() throws Exception {
        EduclTermEntity oldTerm = term(1L, "2025-09-01");
        EduclTermEntity latestTerm = term(2L, "2026-03-01");
        EduclTermEntity targetTerm = term(3L, "2026-09-01");
        AsStudentEntity oldActive = student(11L, 1L, "S001");
        AsStudentEntity latestInactive = student(12L, 2L, "S001");
        latestInactive.setSchoolRollStatus(2);

        when(educlTermMapper.selectById(3L)).thenReturn(targetTerm);
        when(educlTermMapper.selectList(any(Wrapper.class)))
                .thenReturn(Arrays.asList(latestTerm, oldTerm));
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Collections.emptyList(),
                        Arrays.asList(oldActive, latestInactive));

        List<AsStudentEntity> targets =
                support.prepareSchoolTermSnapshots(1L, 3L);

        assertTrue(targets.isEmpty());
        verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
    }

    /**
     * 单学生兜底准备也必须先取最新快照，不能绕过最新学期的非在籍状态。
     */
    @Test
    public void resolveByStudentNoShouldNotFallbackWhenLatestSnapshotIsInactive()
            throws Exception {
        EduclTermEntity oldTerm = term(1L, "2025-09-01");
        EduclTermEntity latestTerm = term(2L, "2026-03-01");
        EduclTermEntity targetTerm = term(3L, "2026-09-01");
        AsStudentEntity oldActive = student(11L, 1L, "S001");
        AsStudentEntity latestInactive = student(12L, 2L, "S001");
        latestInactive.setSchoolRollStatus(2);

        when(educlTermMapper.selectById(3L)).thenReturn(targetTerm);
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Arrays.asList(oldActive, latestInactive));
        when(educlTermMapper.selectBatchIds(any()))
                .thenReturn(Arrays.asList(oldTerm, latestTerm));

        try {
            support.resolveOrPrepare(1L, 3L, "S001");
        } catch (BaseBusinessException exception) {
            assertEquals("学生当前不在籍，不能参与目标学期业务",
                    exception.getErrorMsg());
            verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
            return;
        }
        throw new AssertionError("最新快照非在籍时不应回退到更早学期");
    }

    /**
     * 验证服务发布后新增学生可按来源快照准备目标学期记录。
     */
    @Test
    public void resolveOrPrepareShouldCreateTargetSnapshot() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L)).thenReturn(null);
        when(asStudentMapper.insert(any(AsStudentEntity.class))).thenAnswer(invocation -> {
            AsStudentEntity inserted = invocation.getArgument(0);
            inserted.setId(12L);
            return 1;
        });

        AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

        assertNotNull(target);
        assertEquals(Long.valueOf(12L), target.getId());
        assertEquals(Long.valueOf(2L), target.getTermId());
        assertEquals(Long.valueOf(1001L), target.getStudentIdentityId());
        assertEquals(Long.valueOf(1L), source.getTermId());
    }

    /**
     * 目标学期快照写入失败时必须抛出异常，阻止服务发布继续提交。
     */
    @Test(expected = BaseBusinessException.class)
    public void resolveOrPrepareShouldFailWhenSnapshotInsertFails() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);
        when(asStudentMapper.insert(any(AsStudentEntity.class))).thenReturn(0);

        support.resolveOrPrepare(source, 1L, 2L);
    }

    /**
     * 验证目标学期已存在同一稳定身份快照时直接复用。
     */
    @Test
    public void resolveOrPrepareShouldReturnExistingTargetSnapshot() throws Exception {
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        AsStudentEntity existing = student(12L, 2L, "S001");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L)).thenReturn(existing);

        AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

        assertSame(existing, target);
        verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
    }

    /**
     * 验证稳定身份已有家长关系时只做存在性判断，不创建学期关系副本。
     */
    @Test
    public void copyPreviousApprovedGuardianRelationsShouldUseStableIdentity() {
        AsStudentEntity target = student(12L, 2L, "S001");
        when(asGuardianStudentMapper.selectCount(any(Wrapper.class))).thenReturn(1);

        boolean exists = support.copyPreviousApprovedGuardianRelations(target);

        assertTrue(exists);
        verify(asGuardianStudentMapper, never()).insert(any(AsGuardianStudentEntity.class));
    }

    /**
     * 验证已驳回学生不会进入新学期快照。
     */
    @Test
    public void resolveOrPrepareShouldRejectRejectedStudent() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setRegisterStatus(2);
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);

        try {
            support.resolveOrPrepare(source, 1L, 2L);
        } catch (BaseBusinessException exception) {
            assertEquals("学生注册状态不允许生成目标学期档案",
                    exception.getErrorMsg());
            verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
            return;
        }
        throw new AssertionError("已驳回学生不应生成目标学期快照");
    }

    /**
     * 空值或未知注册状态不能绕过未来学期快照状态白名单。
     */
    @Test
    public void resolveOrPrepareShouldRejectUnsupportedRegisterStatus()
            throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        Integer[] statuses = {null, 4};
        for (Integer status : statuses) {
            AsStudentEntity source = student(11L, 1L, "S001");
            source.setRegisterStatus(status);
            try {
                support.resolveOrPrepare(source, 1L, 2L);
            } catch (BaseBusinessException exception) {
                assertEquals("学生注册状态不允许生成目标学期档案",
                        exception.getErrorMsg());
                continue;
            }
            throw new AssertionError("异常注册状态应被拒绝");
        }
    }

    /**
     * 客户端传入较早学期 studentId 时，必须以稳定身份的最新学籍状态为准。
     */
    @Test
    public void resolveOrPrepareShouldRejectLatestInactiveIdentitySnapshot()
            throws Exception {
        EduclTermEntity oldTerm = term(1L, "2025-09-01");
        EduclTermEntity latestTerm = term(2L, "2026-03-01");
        EduclTermEntity targetTerm = term(3L, "2026-09-01");
        AsStudentEntity requestedOld = student(11L, 1L, "S001");
        AsStudentEntity latestInactive = student(12L, 2L, "S001");
        latestInactive.setSchoolRollStatus(2);

        when(educlTermMapper.selectById(3L)).thenReturn(targetTerm);
        when(studentIdentitySupport.findSnapshot(1L, 3L, 1001L))
                .thenReturn(null);
        when(asStudentMapper.selectList(any(Wrapper.class)))
                .thenReturn(Arrays.asList(requestedOld, latestInactive));
        when(educlTermMapper.selectBatchIds(any()))
                .thenReturn(Arrays.asList(oldTerm, latestTerm));

        try {
            support.resolveOrPrepare(requestedOld, 1L, 3L);
        } catch (BaseBusinessException exception) {
            assertEquals("学生当前不在籍，不能参与目标学期业务",
                    exception.getErrorMsg());
            verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
            return;
        }
        throw new AssertionError("旧学期 studentId 不应绕过最新非在籍状态");
    }

    /**
     * 目标学期已有快照时，也必须校验目标快照当前是否允许参与业务。
     */
    @Test
    public void resolveOrPrepareShouldRejectInactiveExistingTargetSnapshot()
            throws Exception {
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        AsStudentEntity existingInactive = student(12L, 2L, "S001");
        existingInactive.setSchoolRollStatus(2);
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(existingInactive);

        try {
            support.resolveOrPrepare(source, 1L, 2L);
        } catch (BaseBusinessException exception) {
            assertEquals("学生当前不在籍，不能参与目标学期业务",
                    exception.getErrorMsg());
            verify(asStudentMapper, never()).insert(any(AsStudentEntity.class));
            return;
        }
        throw new AssertionError("非在籍的目标学期快照不应参与报名业务");
    }

    /**
     * 待注册、待审核和已通过状态生成新学期快照时均保持原状态。
     */
    @Test
    public void resolveOrPrepareShouldKeepSupportedRegisterStatuses()
            throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);
        when(asStudentMapper.insert(any(AsStudentEntity.class)))
                .thenReturn(1);

        Integer[] statuses = {0, 3, 1};
        for (Integer status : statuses) {
            AsStudentEntity source = student(11L, 1L, "S001");
            source.setRegisterStatus(status);

            AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

            assertEquals(status, target.getRegisterStatus());
        }
    }

    /**
     * 春季新学期不升级年级和班级。
     */
    @Test
    public void springTargetShouldKeepGradeAndClass() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-02-20");
        EduclTermEntity targetTerm = term(2L, "2026-03-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(201L);
        AsStudentIdentityEntity identity = identity(2025);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(101L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("一年级");
        targetGrade.setIsValid(1);
        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L)).thenReturn(identity);
        when(educlGradeMapper.selectOne(any(Wrapper.class))).thenReturn(targetGrade);
        when(asStudentMapper.insert(any(AsStudentEntity.class)))
                .thenReturn(1);

        AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

        assertEquals(Long.valueOf(101L), target.getGradeId());
        assertEquals(Long.valueOf(201L), target.getClassId());
        verify(educlGradeMapper, never()).selectById(any());
        verify(educlClassService, never()).getById(any());
    }

    /**
     * 秋季新学期升级年级，并映射目标年级下的同名班级。
     */
    @Test
    public void autumnTargetShouldPromoteGradeAndMapClass() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-03-01");
        EduclTermEntity targetTerm = term(2L, "2026-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(201L);

        AsStudentIdentityEntity identity = identity(2025);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(102L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("二年级");
        targetGrade.setIsValid(1);
        TEduclClass sourceClass = new TEduclClass();
        sourceClass.setId(201L);
        sourceClass.setSchoolId(1L);
        sourceClass.setGradeId(101L);
        sourceClass.setClassName("二班");
        sourceClass.setIsValid(1);
        TEduclClass targetClass = new TEduclClass();
        targetClass.setId(202L);
        targetClass.setSchoolId(1L);
        targetClass.setGradeId(102L);
        targetClass.setClassName("二班");
        targetClass.setIsValid(1);

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L)).thenReturn(identity);
        when(educlGradeMapper.selectOne(any(Wrapper.class)))
                .thenReturn(targetGrade);
        when(educlClassService.getById(201L)).thenReturn(sourceClass);
        when(educlClassService.list(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(targetClass));
        when(asStudentMapper.insert(any(AsStudentEntity.class)))
                .thenReturn(1);

        AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

        assertEquals(Long.valueOf(102L), target.getGradeId());
        assertEquals(Long.valueOf(202L), target.getClassId());
        assertEquals(Integer.valueOf(1), target.getRegisterStatus());
    }

    /**
     * 毕业年级学生跨入下一学年时不能生成目标学期快照。
     */
    @Test(expected = BaseBusinessException.class)
    public void autumnTargetShouldSkipGraduatedStudent() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-03-01");
        EduclTermEntity targetTerm = term(2L, "2026-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(109L);
        source.setClassId(209L);

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L))
                .thenReturn(identity(2017));

        support.resolveOrPrepare(source, 1L, 2L);
    }

    /**
     * 秋季升级后找不到同名目标班级时必须阻断快照生成。
     */
    @Test(expected = BaseBusinessException.class)
    public void autumnTargetShouldFailWhenClassMappingMissing() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-03-01");
        EduclTermEntity targetTerm = term(2L, "2026-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(201L);
        AsStudentIdentityEntity identity = identity(2025);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(102L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("二年级");
        targetGrade.setIsValid(1);
        TEduclClass sourceClass = new TEduclClass();
        sourceClass.setId(201L);
        sourceClass.setSchoolId(1L);
        sourceClass.setGradeId(101L);
        sourceClass.setClassName("二班");
        sourceClass.setIsValid(1);

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L))
                .thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L)).thenReturn(identity);
        when(educlGradeMapper.selectOne(any(Wrapper.class)))
                .thenReturn(targetGrade);
        when(educlClassService.getById(201L)).thenReturn(sourceClass);
        when(educlClassService.list(any(Wrapper.class)))
                .thenReturn(Collections.emptyList());

        support.resolveOrPrepare(source, 1L, 2L);
    }

    /**
     * 应按入学年份和目标学期计算年级，而不是在来源年级上简单累加。
     */
    @Test
    public void targetGradeShouldBeCalculatedFromEnrollmentYear() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-09-01");
        EduclTermEntity targetTerm = term(2L, "2027-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(201L);

        AsStudentIdentityEntity identity = new AsStudentIdentityEntity();
        identity.setId(1001L);
        identity.setSchoolId(1L);
        identity.setEnrollmentYear(2026);
        identity.setIsValid(1);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(102L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("二年级");
        targetGrade.setIsValid(1);
        TEduclClass sourceClass = new TEduclClass();
        sourceClass.setId(201L);
        sourceClass.setSchoolId(1L);
        sourceClass.setGradeId(101L);
        sourceClass.setClassName("2601班");
        sourceClass.setIsValid(1);
        TEduclClass targetClass = new TEduclClass();
        targetClass.setId(202L);
        targetClass.setSchoolId(1L);
        targetClass.setGradeId(102L);
        targetClass.setClassName("2601班");
        targetClass.setIsValid(1);

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L)).thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L)).thenReturn(identity);
        when(educlGradeMapper.selectOne(any(Wrapper.class))).thenReturn(targetGrade);
        when(educlClassService.getById(201L)).thenReturn(sourceClass);
        when(educlClassService.list(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(targetClass));
        when(asStudentMapper.insert(any(AsStudentEntity.class))).thenReturn(1);

        AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

        assertEquals(Long.valueOf(102L), target.getGradeId());
        assertEquals(Long.valueOf(202L), target.getClassId());
        assertEquals("2601班", targetClass.getClassName());
        verify(educlGradeMapper, never()).selectById(any());
    }

    /**
     * 旧稳定身份缺少入学年份时，只允许从来源班级初始化后再计算年级。
     */
    @Test
    public void missingEnrollmentYearShouldFallbackToSourceClass() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-03-01");
        EduclTermEntity targetTerm = term(2L, "2026-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(201L);
        AsStudentIdentityEntity identity = identity(null);
        TEduclClass sourceClass = new TEduclClass();
        sourceClass.setId(201L);
        sourceClass.setSchoolId(1L);
        sourceClass.setGradeId(101L);
        sourceClass.setClassName("2601班");
        sourceClass.setEnrollmentYear(2025);
        sourceClass.setIsValid(1);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(102L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("二年级");
        targetGrade.setIsValid(1);
        TEduclClass targetClass = new TEduclClass();
        targetClass.setId(202L);
        targetClass.setSchoolId(1L);
        targetClass.setGradeId(102L);
        targetClass.setClassName("2601班");
        targetClass.setIsValid(1);

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L)).thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L)).thenReturn(identity);
        when(educlClassService.getById(201L)).thenReturn(sourceClass);
        when(educlGradeMapper.selectOne(any(Wrapper.class))).thenReturn(targetGrade);
        when(educlClassService.list(any(Wrapper.class)))
                .thenReturn(Collections.singletonList(targetClass));
        when(asStudentMapper.insert(any(AsStudentEntity.class))).thenReturn(1);

        AsStudentEntity target = support.resolveOrPrepare(source, 1L, 2L);

        assertEquals(Long.valueOf(102L), target.getGradeId());
        verify(studentIdentitySupport).initializeEnrollmentYear(
                identity, 2025, 1L);
    }

    /**
     * 自动升班时来源班级为空必须失败，不能生成无班级的目标快照。
     */
    @Test
    public void promotionShouldFailWhenSourceClassIsMissing() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-03-01");
        EduclTermEntity targetTerm = term(2L, "2026-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(null);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(102L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("二年级");
        targetGrade.setIsValid(1);

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L)).thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L))
                .thenReturn(identity(2025));
        when(educlGradeMapper.selectOne(any(Wrapper.class))).thenReturn(targetGrade);

        try {
            support.resolveOrPrepare(source, 1L, 2L);
            fail("来源班级为空时应阻断自动升班");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("来源班级为空"));
        }
    }

    /**
     * 目标年级存在多个同名班时必须失败，不能任取一个班级。
     */
    @Test
    public void promotionShouldFailWhenTargetClassIsAmbiguous() throws Exception {
        EduclTermEntity sourceTerm = term(1L, "2026-03-01");
        EduclTermEntity targetTerm = term(2L, "2026-09-01");
        AsStudentEntity source = student(11L, 1L, "S001");
        source.setGradeId(101L);
        source.setClassId(201L);
        EduclGradeEntity targetGrade = new EduclGradeEntity();
        targetGrade.setId(102L);
        targetGrade.setSchoolId(1L);
        targetGrade.setGradeName("二年级");
        targetGrade.setIsValid(1);
        TEduclClass sourceClass = new TEduclClass();
        sourceClass.setId(201L);
        sourceClass.setSchoolId(1L);
        sourceClass.setClassName("2601班");
        sourceClass.setIsValid(1);
        TEduclClass first = new TEduclClass();
        first.setId(202L);
        first.setClassName("2601班");
        TEduclClass second = new TEduclClass();
        second.setId(203L);
        second.setClassName("2601班");

        when(educlTermMapper.selectById(2L)).thenReturn(targetTerm);
        when(educlTermMapper.selectById(1L)).thenReturn(sourceTerm);
        when(studentIdentitySupport.findSnapshot(1L, 2L, 1001L)).thenReturn(null);
        when(studentIdentitySupport.requireIdentity(1001L, 1L))
                .thenReturn(identity(2025));
        when(educlGradeMapper.selectOne(any(Wrapper.class))).thenReturn(targetGrade);
        when(educlClassService.getById(201L)).thenReturn(sourceClass);
        when(educlClassService.list(any(Wrapper.class)))
                .thenReturn(Arrays.asList(first, second));

        try {
            support.resolveOrPrepare(source, 1L, 2L);
            fail("目标年级同名班不唯一时应阻断自动升班");
        } catch (BaseBusinessException exception) {
            assertTrue(exception.getErrorMsg().contains("多个同名班级"));
        }
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
     * 创建测试学期。
     */
    private EduclTermEntity term(Long id, String startDate) throws ParseException {
        EduclTermEntity term = new EduclTermEntity();
        term.setId(id);
        term.setSchoolId(1L);
        term.setTermStatus(0);
        term.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
        term.setIsValid(1);
        return term;
    }

    /**
     * 创建带稳定身份的测试学生快照。
     */
    private AsStudentEntity student(Long id, Long termId, String studentNo) {
        AsStudentEntity student = new AsStudentEntity();
        student.setId(id);
        student.setSchoolId(1L);
        student.setTermId(termId);
        student.setStudentIdentityId(1001L);
        student.setStudentNo(studentNo);
        student.setStudentName("张三");
        student.setGradeId(101L);
        student.setClassId(201L);
        student.setSchoolRollStatus(1);
        student.setRegisterStatus(1);
        student.setIsValid(1);
        return student;
    }

    /**
     * 创建带入学年份的测试稳定身份。
     */
    private AsStudentIdentityEntity identity(Integer enrollmentYear) {
        AsStudentIdentityEntity identity = new AsStudentIdentityEntity();
        identity.setId(1001L);
        identity.setSchoolId(1L);
        identity.setEnrollmentYear(enrollmentYear);
        identity.setIsValid(1);
        return identity;
    }
}
