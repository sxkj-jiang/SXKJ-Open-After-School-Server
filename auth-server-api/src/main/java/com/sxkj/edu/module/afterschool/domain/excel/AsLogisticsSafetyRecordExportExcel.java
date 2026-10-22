package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * 后勤安全检查记录导出 Excel 模型
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsLogisticsSafetyRecordExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "日期", index = 1)
    private String checkDate;

    @ColumnWidth(20)
    @ExcelProperty(value = "上课地点", index = 2)
    private String placeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "教师名称", index = 3)
    private String teacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程班名称", index = 4)
    private String courseClassName;

    @ColumnWidth(15)
    @ExcelProperty(value = "课程类别名称", index = 5)
    private String categoryName;

    @ColumnWidth(12)
    @ExcelProperty(value = "状态", index = 6)
    private String recordStatusDesc;

    @ColumnWidth(22)
    @ExcelProperty(value = "提交时间", index = 7)
    private String submitTime;
}
