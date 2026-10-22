package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 课时统计-学生课时导出 Excel 模型
 *
 * @author system
 * @date 2026-07-20
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class LessonStatisticsStudentExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "学号", index = 1)
    private String studentNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "学期", index = 2)
    private String termName;

    @ColumnWidth(15)
    @ExcelProperty(value = "姓名", index = 3)
    private String studentName;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程类别", index = 4)
    private String courseCategoryNames;

    @ColumnWidth(15)
    @ExcelProperty(value = "总课时", index = 5)
    private Integer totalLessonHours;

    @ColumnWidth(15)
    @ExcelProperty(value = "出勤课时", index = 6)
    private Integer attendanceHours;

    @ColumnWidth(15)
    @ExcelProperty(value = "缺勤/请假课时", index = 7)
    private Integer absenceHours;

    @ColumnWidth(15)
    @ExcelProperty(value = "出勤率", index = 8)
    private String attendanceRate;
}
