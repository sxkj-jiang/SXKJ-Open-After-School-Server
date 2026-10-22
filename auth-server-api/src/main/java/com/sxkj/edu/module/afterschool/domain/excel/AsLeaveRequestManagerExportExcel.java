package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 管理端请假申请导出 Excel 行
 *
 * @author system
 * @date 2026-06-25
 */
@Data
public class AsLeaveRequestManagerExportExcel {

    @ExcelProperty("学生姓名")
    private String studentName;

    @ExcelProperty("学号")
    private String studentNo;

    @ExcelProperty("年级")
    private String gradeName;

    @ExcelProperty("班级")
    private String className;

    @ExcelProperty("请假类型")
    private String leaveTypeDesc;

    @ExcelProperty("请假天数")
    private String leaveDays;

    @ExcelProperty("请假时间")
    private String leaveTime;

    @ExcelProperty("请假原因")
    private String leaveReason;

    @ExcelProperty("发起人")
    private String applicantName;

    @ExcelProperty("班主任")
    private String classNameTeacher;

    @ExcelProperty("审批状态")
    private String approveStatusDesc;

    @ExcelProperty("申请时间")
    private String createTime;

    @ExcelProperty("学期")
    private String termName;
}
