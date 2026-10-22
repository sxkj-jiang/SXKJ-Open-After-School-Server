package com.sxkj.edu.config.handle.business;

import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description : 保存选课操作日志
 * @Author : jiangjj$
 * @Date : 20260810$ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class EnrollmentTimeSlot {
    private  Long sessionId;
    private  String slotKey;
    private  Date sessionDate;
    private  Integer weekDay;
    private  Integer startMinute;
    private  Integer endMinute;
    private  String displayText;
    private  List<Long> gradeIds;

    public EnrollmentTimeSlot(Long sessionId, String slotKey, Date sessionDate, Integer weekDay,
                               Integer startMinute, Integer endMinute, String displayText, List<Long> gradeIds) {
        this.sessionId = sessionId;
        this.slotKey = slotKey;
        this.sessionDate = sessionDate;
        this.weekDay = weekDay;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.displayText = displayText;
        this.gradeIds = gradeIds == null ? Collections.emptyList() : gradeIds;
    }

    public EnrollmentTimeSlot(){}

}
