package com.sxkj.edu.common.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Description : 审核类型表
 *@Author : JIANGJJ
 *@Date : 2023-2-7 15:11
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum ExamTypeEnum {
    enrollmentPlan("enrollment_plan", "招生计划审核"),

    corrManager("corr_manager", "函授站管理审核"),

    studentRecord("student_record", "录取审核"),

    majorSubject("major_subject", "专业课程管理审核"),

    subjectResourse("subject_resourse", "课程资源库审核"),

    gradeThesStu("grade_thes_stu", "学生论文答辩审核"),

    gradeThesisExam("grade_thesis_exam", "毕业选导申请审核"),

    gradeThesReq("grade_thes_req", "论文答辩要求审核");

    String code;
    String name;

    ExamTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    private static final Map<String, ExamTypeEnum> keyMap = new HashMap<>();

    public static String getValue(String code) {
        ExamTypeEnum[] roleEnums = values();
        for (ExamTypeEnum roleEnum : roleEnums) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum.getValue(code);
            }
        }
        return null;
    }

    public static List<String> getAllCode() {
        ExamTypeEnum[] roleEnums = values();
        List<String> allCode = new ArrayList<>();
        for (ExamTypeEnum roleEnum : roleEnums) {
            allCode.add(roleEnum.getCode());
        }
        return allCode;
    }

    public static ExamTypeEnum fromCode(String code) {
        return keyMap.get(code);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
