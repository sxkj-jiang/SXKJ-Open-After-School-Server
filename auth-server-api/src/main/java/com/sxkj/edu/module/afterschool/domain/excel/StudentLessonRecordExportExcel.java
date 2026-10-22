package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 学生课时记录导出 Excel 模型
 *
 * @author system
 * @date 2026-06-10
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class StudentLessonRecordExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "学期", index = 1)
    private String termName;

    @ColumnWidth(15)
    @ExcelProperty(value = "上课日期", index = 2)
    private String sessionDate;

    @ColumnWidth(35)
    @ExcelProperty(value = "课程班名称", index = 3)
    private String courseClassName;

    @ColumnWidth(25)
    @ExcelProperty(value = "上课时间", index = 4)
    private String sessionTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "出勤状态", index = 5)
    private String attendanceStatusDesc;

    @ColumnWidth(30)
    @ExcelProperty(value = "备注", index = 6)
    private String remark;
}
