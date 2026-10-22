package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 学生去向预警导出 Excel 模型
 *
 * @author yh
 * @date 2026-06-11
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsWarningAdminExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "学号", index = 1)
    private String studentNo;

    @ColumnWidth(15)
    @ExcelProperty(value = "学生姓名", index = 2)
    private String studentName;

    @ColumnWidth(15)
    @ExcelProperty(value = "年级", index = 3)
    private String gradeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "班级", index = 4)
    private String className;

    @ColumnWidth(15)
    @ExcelProperty(value = "学生去向", index = 5)
    private String destinationDesc;

    @ColumnWidth(15)
    @ExcelProperty(value = "学生状态", index = 6)
    private String attendanceStatusDesc;

    @ColumnWidth(20)
    @ExcelProperty(value = "发起时间", index = 7)
    private String triggerTime;

    @ColumnWidth(12)
    @ExcelProperty(value = "预警状态", index = 8)
    private String warningLevelDesc;

    @ColumnWidth(20)
    @ExcelProperty(value = "学期", index = 9)
    private String termName;
}
