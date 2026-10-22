package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 教师考勤导出 Excel 模型
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsAttendanceTeacherExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "姓名", index = 1)
    private String teacherName;

    @ColumnWidth(15)
    @ExcelProperty(value = "工号", index = 2)
    private String teacherNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程班", index = 3)
    private String courseClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程活动", index = 4)
    private String serviceName;

    @ColumnWidth(15)
    @ExcelProperty(value = "授课教师", index = 5)
    private String courseTeacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "上课地点", index = 6)
    private String placeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "上课时间", index = 7)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date sessionTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "考勤状态", index = 8)
    private String attendanceStatusDesc;
}
