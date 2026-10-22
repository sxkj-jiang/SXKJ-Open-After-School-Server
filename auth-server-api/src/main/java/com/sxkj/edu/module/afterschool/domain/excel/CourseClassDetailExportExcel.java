package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 课程分布详情-课程列表导出 Excel 模型
 *
 * @author system
 * @date 2026-06-24
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class CourseClassDetailExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程班名称", index = 1)
    private String courseClassName;

    @ColumnWidth(25)
    @ExcelProperty(value = "所属类别", index = 2)
    private String categoryPath;

    @ColumnWidth(25)
    @ExcelProperty(value = "适用年级", index = 3)
    private String gradeNames;

    @ColumnWidth(20)
    @ExcelProperty(value = "授课教师", index = 4)
    private String teacherName;

    @ColumnWidth(40)
    @ExcelProperty(value = "上课时间", index = 5)
    private String sessionTimes;
}
