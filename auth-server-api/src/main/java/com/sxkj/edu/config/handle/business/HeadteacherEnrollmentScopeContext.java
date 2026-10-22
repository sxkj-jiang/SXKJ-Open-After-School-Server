package com.sxkj.edu.config.handle.business;

import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description :
 * @Author : jiangjj$
 * @Date : 20260810$ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class HeadteacherEnrollmentScopeContext {

    public Long schoolId;
    public List<Long> classIds;
    public List<AsStudentEntity> students;
    public Set<Long> studentIds;
    public Map<Long, AsServiceEntity> serviceMap;
    public List<AsEnrollmentEntity> allEnrollments;
}
