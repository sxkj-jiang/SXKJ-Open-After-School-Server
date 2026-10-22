package com.sxkj.edu.config.handle.business;

import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author : jiangjj$
 * @Date : 20260810$ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class HeadteacherEnrollmentBuildContext {

    public Map<Long, AsStudentEntity> studentMap = Collections.emptyMap();
    public Map<Long, AsCourseClassEntity> courseMap = Collections.emptyMap();
    public Map<Long, Boolean> courseStartedMap = Collections.emptyMap();
    public Map<Long, String> scheduleTimeDescMap = Collections.emptyMap();
    public Map<Long, AsCourseCategoryEntity> categoryMap = Collections.emptyMap();
    public Map<Long, EduclGradeEntity> gradeMap = Collections.emptyMap();
    public Map<Long, TEduclClass> classMap = Collections.emptyMap();
    public Map<Long, EduclTermEntity> termMap = Collections.emptyMap();
    public Map<String, List<EnrollmentTimeSlot>> remainingSlotMap = Collections.emptyMap();
    public Map<String, AsStudentDestinationEntity> nonCourseDestinationMap = Collections.emptyMap();
    public Map<String, List<EnrollmentTimeSlot>> nonCourseDestinationSlotMap = Collections.emptyMap();
}
