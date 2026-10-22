package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 课时统计-教师课时导出 Excel 模型
 *
 * @author system
 * @date 2026-07-20
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class LessonStatisticsTeacherExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "教师工号", index = 1)
    private String teacherNo;

    @ColumnWidth(15)
    @ExcelProperty(value = "姓名", index = 2)
    private String teacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "学习周期", index = 3)
    private String termName;

    @ColumnWidth(15)
    @ExcelProperty(value = "总课时", index = 4)
    private Integer totalLessonHours;

    @ColumnWidth(15)
    @ExcelProperty(value = "已完成课时", index = 5)
    private Integer completedLessonHours;

    @ColumnWidth(15)
    @ExcelProperty(value = "调课课时", index = 6)
    private Integer adjustedSessions;

    @ColumnWidth(15)
    @ExcelProperty(value = "代课课时", index = 7)
    private Integer substituteSessions;
}
