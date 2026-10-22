package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程班导出 Excel 模型
 *
 * @author wangyouhuai
 * @date 2026-06-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsCourseClassExportExcel extends BaseRowModel {

    @ExcelProperty(value = "课后服务名称", index = 0)
    private String serviceName;

    @ColumnWidth(25)
    @ExcelProperty(value = "课程班编号", index = 1)
    private String courseClassCode;

    @ExcelProperty(value = "课程班名称", index = 2)
    private String courseClassName;

    @ExcelProperty(value = "课程类别名称", index = 3)
    private String categoryName;

    @ExcelProperty(value = "适用年级", index = 4)
    private String applicableGradeNames;

    @ColumnWidth(15)
    @ExcelProperty(value = "报名方式", index = 5)
    private String enrollTypeDesc;

    @ColumnWidth(30)
    @ExcelProperty(value = "开课周期", index = 6)
    private String courseCycle;

    @ExcelProperty(value = "教师姓名", index = 7)
    private String teacherName;

    @ExcelProperty(value = "上课地点", index = 8)
    private String placeName;

    @ColumnWidth(12)
    @ExcelProperty(value = "学生人数", index = 9)
    private Integer studentCount;

    @ColumnWidth(12)
    @ExcelProperty(value = "总出勤率(%)", index = 10)
    private Double attendanceRate;

    @ExcelProperty(value = "学期", index = 11)
    private String termName;
}
