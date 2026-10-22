package com.sxkj.edu.module.afterschool.support;

import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionEnrollmentSupportTest {

    @Mock
    private AsCourseSessionMapper asCourseSessionMapper;
    @Mock
    private AsCourseClassMapper asCourseClassMapper;

    private SessionEnrollmentSupport support;

    @Before
    public void setUp() {
        support = new SessionEnrollmentSupport();
        ReflectionTestUtils.setField(support, "asCourseSessionMapper", asCourseSessionMapper);
        ReflectionTestUtils.setField(support, "asCourseClassMapper", asCourseClassMapper);
    }

    @Test
    public void listActiveSessionEnrollmentsShouldIgnoreMissingCourseAndInvalidSession() {
        AsEnrollmentEntity valid = enrollment(1L, 30L, 900L);
        AsEnrollmentEntity missingCourse = enrollment(2L, 52L, 901L);
        AsEnrollmentEntity invalidSession = enrollment(3L, 64L, 902L);
        when(asCourseSessionMapper.selectBatchIds(any()))
                .thenReturn(Arrays.asList(session(900L, 1, 0), session(901L, 1, 0), session(902L, 0, 0)));
        when(asCourseClassMapper.selectBatchIds(any()))
                .thenReturn(Arrays.asList(course(30L, 1), course(64L, 1)));

        @SuppressWarnings("unchecked")
        List<AsEnrollmentEntity> result = (List<AsEnrollmentEntity>) ReflectionTestUtils.invokeMethod(support,
                "filterActiveSessionEnrollments", Arrays.asList(valid, missingCourse, invalidSession));

        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1L), result.get(0).getId());
    }

    private AsEnrollmentEntity enrollment(Long id, Long courseClassId, Long sessionId) {
        AsEnrollmentEntity enrollment = new AsEnrollmentEntity();
        enrollment.setId(id);
        enrollment.setSchoolId(1L);
        enrollment.setStudentId(10L);
        enrollment.setCourseClassId(courseClassId);
        enrollment.setSessionId(sessionId);
        enrollment.setEnrollStatus(1);
        enrollment.setIsValid(1);
        return enrollment;
    }

    private AsCourseSessionEntity session(Long id, Integer isValid, Integer sessionStatus) {
        AsCourseSessionEntity session = new AsCourseSessionEntity();
        session.setId(id);
        session.setIsValid(isValid);
        session.setSessionStatus(sessionStatus);
        return session;
    }

    private AsCourseClassEntity course(Long id, Integer isValid) {
        AsCourseClassEntity course = new AsCourseClassEntity();
        course.setId(id);
        course.setIsValid(isValid);
        return course;
    }
}
