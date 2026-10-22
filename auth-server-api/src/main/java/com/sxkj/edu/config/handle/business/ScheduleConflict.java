package com.sxkj.edu.config.handle.business;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @Description :
 * @Author : jiangjj$
 * @Date : 20260810$ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class ScheduleConflict {

    private  Long courseClassId;
    private  String courseClassName;

    public ScheduleConflict(){}

    public ScheduleConflict(Long courseClassId, String courseClassName) {
        this.courseClassId = courseClassId;
        this.courseClassName = courseClassName;
    }

    public String message() {
        if (StringUtils.hasText(courseClassName)) {
            return "该学生已选择时间冲突的课程：" + courseClassName;
        }
        return "该学生已选择时间冲突的其他课程";
    }
}
