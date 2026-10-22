package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class CourseQualityExportExcel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(25)
    @ExcelProperty(value = "课程名称", index = 1)
    private String courseName;

    @ColumnWidth(15)
    @ExcelProperty(value = "课程类别", index = 2)
    private String categoryName;

    @ColumnWidth(30)
    @ExcelProperty(value = "开课周期", index = 3)
    private String coursePeriod;

    @ColumnWidth(15)
    @ExcelProperty(value = "教师", index = 4)
    private String teacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "上课地点", index = 5)
    private String placeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "总出勤率", index = 6)
    private String totalAttendanceRate;

    @ColumnWidth(15)
    @ExcelProperty(value = "学生家长评分", index = 7)
    private String parentScore;

    @ColumnWidth(15)
    @ExcelProperty(value = "管理员评分", index = 8)
    private String adminScore;
}
