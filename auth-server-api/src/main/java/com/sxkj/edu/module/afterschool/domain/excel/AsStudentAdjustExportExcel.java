package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 学生调课导出 Excel 行。
 */
@Data
public class AsStudentAdjustExportExcel {

    @ExcelProperty("申请编号")
    private String requestNo;

    @ExcelProperty("学号")
    private String studentNo;

    @ExcelProperty("学生姓名")
    private String studentName;

    @ExcelProperty("年级")
    private String gradeName;

    @ExcelProperty("班级")
    private String className;

    @ExcelProperty("原课程")
    private String fromCourseClassName;

    @ExcelProperty("已学课时")
    private Integer learnedSessions;

    @ExcelProperty("新课程")
    private String toCourseClassName;

    @ExcelProperty("班主任")
    private String headTeacherName;

    @ExcelProperty("申请时间")
    private String applyTime;

    @ExcelProperty("状态")
    private String requestStatusDesc;

    @ExcelProperty("驳回原因")
    private String rejectReason;

    @ExcelProperty("审核人")
    private String handleUserName;

    @ExcelProperty("审核时间")
    private String handleTime;

    @ExcelProperty("学期")
    private String termName;
}
