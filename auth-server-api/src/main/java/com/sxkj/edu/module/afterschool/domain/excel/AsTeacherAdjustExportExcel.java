package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 教师调课导出 Excel 模型。
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsTeacherAdjustExportExcel extends BaseRowModel {

    @ExcelProperty(value = "编号", index = 0)
    private String requestNo;

    @ExcelProperty(value = "申请人", index = 1)
    private String applicantName;

    @ExcelProperty(value = "课程信息", index = 2)
    private String courseInfo;

    @ExcelProperty(value = "调课周期", index = 3)
    private String adjustPeriod;

    @ExcelProperty(value = "总节数", index = 4)
    private Integer totalSessions;

    @ExcelProperty(value = "上课时间", index = 5)
    private String scheduleText;

    @ExcelProperty(value = "教师", index = 6)
    private String originalTeacherName;

    @ExcelProperty(value = "代课教师", index = 7)
    private String substituteTeacherName;

    @ExcelProperty(value = "上课地点", index = 8)
    private String placeName;

    @ExcelProperty(value = "申请时间", index = 9)
    private String applyTime;

    @ExcelProperty(value = "状态", index = 10)
    private String requestStatusDesc;

    @ExcelProperty(value = "处理人", index = 11)
    private String handleUserName;

    @ExcelProperty(value = "处理时间", index = 12)
    private String handleTime;

    @ExcelProperty(value = "处理意见", index = 13)
    private String handleOpinion;
}
