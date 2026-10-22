package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 课时完成情况-课时详情导出 Excel 模型
 *
 * @author system
 * @date 2026-06-25
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class CourseSessionDetailExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程班名称", index = 1)
    private String courseClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程类别", index = 2)
    private String categoryName;

    @ColumnWidth(12)
    @ExcelProperty(value = "总课时", index = 3)
    private String totalSessions;

    @ColumnWidth(12)
    @ExcelProperty(value = "已上课课时", index = 4)
    private String completedSessions;

    @ColumnWidth(12)
    @ExcelProperty(value = "剩余课时", index = 5)
    private String remainingSessions;

    @ColumnWidth(12)
    @ExcelProperty(value = "完成率", index = 6)
    private String completionRate;

    @ColumnWidth(15)
    @ExcelProperty(value = "授课教师", index = 7)
    private String teacherName;

    @ColumnWidth(12)
    @ExcelProperty(value = "课程状态", index = 8)
    private String courseStatusDesc;
}
