package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 巡课计划导出 Excel 模型
 *
 * @author system
 * @date 2026-06-26
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsPatrolPlanExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "编号", index = 1)
    private String id;

    @ColumnWidth(30)
    @ExcelProperty(value = "巡课计划名称", index = 2)
    private String planName;

    @ColumnWidth(15)
    @ExcelProperty(value = "巡课类型", index = 3)
    private String patrolTypeName;

    @ColumnWidth(30)
    @ExcelProperty(value = "巡课周期", index = 4)
    private String patrolCycle;

    @ColumnWidth(30)
    @ExcelProperty(value = "巡课人员", index = 5)
    private String patrolUserNames;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程类型", index = 6)
    private String patrolScopeCourseTypeNames;

    @ColumnWidth(15)
    @ExcelProperty(value = "状态", index = 7)
    private String planStatusName;

    @ColumnWidth(15)
    @ExcelProperty(value = "巡课记录数", index = 8)
    private String recordNum;
}
