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
 * 学生考勤导出 Excel 模型
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsAttendanceStudentExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "学号", index = 1)
    private String studentNo;

    @ColumnWidth(15)
    @ExcelProperty(value = "姓名", index = 2)
    private String studentName;

    @ColumnWidth(15)
    @ExcelProperty(value = "年级", index = 3)
    private String gradeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "班级", index = 4)
    private String className;

    @ColumnWidth(15)
    @ExcelProperty(value = "选课方式", index = 5)
    private String enrollTypeDesc;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程活动", index = 6)
    private String serviceName;

    @ColumnWidth(15)
    @ExcelProperty(value = "授课教师", index = 7)
    private String teacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "上课地点", index = 8)
    private String placeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "上课时间", index = 9)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date sessionTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "考勤状态", index = 10)
    private String attendanceStatusDesc;

    @ColumnWidth(20)
    @ExcelProperty(value = "学期", index = 11)
    private String termName;
}
