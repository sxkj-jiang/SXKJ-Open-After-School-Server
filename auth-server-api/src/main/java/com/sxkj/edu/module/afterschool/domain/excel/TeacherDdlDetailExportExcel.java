package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 教师统计-点到率详情导出 Excel 模型
 *
 * @author system
 * @date 2026-06-17
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class TeacherDdlDetailExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "上课日期", index = 1)
    private String sessionDate;

    @ColumnWidth(15)
    @ExcelProperty(value = "授课教师", index = 2)
    private String teacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "年级", index = 3)
    private String gradeNames;

    @ColumnWidth(35)
    @ExcelProperty(value = "课程", index = 4)
    private String courseClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "点到时间", index = 5)
    private String operateAttendTime;

    @ColumnWidth(18)
    @ExcelProperty(value = "状态", index = 6)
    private String isOperateAttendDesc;
}
