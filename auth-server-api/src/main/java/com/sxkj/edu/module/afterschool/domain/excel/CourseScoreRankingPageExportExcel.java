package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 课程评分排行-课程评价导出 Excel 模型
 *
 * @author system
 * @date 2026-06-26
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class CourseScoreRankingPageExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程班名称", index = 1)
    private String courseClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程类型", index = 2)
    private String categoryName;

    @ColumnWidth(20)
    @ExcelProperty(value = "授课教师", index = 3)
    private String teacherName;

    @ColumnWidth(15)
    @ExcelProperty(value = "评价平均分", index = 4)
    private String averageScore;

    @ColumnWidth(15)
    @ExcelProperty(value = "评价数量", index = 5)
    private String evaluationCount;
}
