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
 * 巡课记录(管理后台)导出 Excel 模型
 *
 * @author system
 * @date 2026-06-26
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsPatrolRecordAdminExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "编号", index = 1)
    private String id;

    @ColumnWidth(30)
    @ExcelProperty(value = "巡课计划", index = 2)
    private String planName;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程名称", index = 3)
    private String courseClassName;

    @ColumnWidth(15)
    @ExcelProperty(value = "巡课类型", index = 4)
    private String patrolTypeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "巡课日期", index = 5)
    private String patrolDate;

    @ColumnWidth(20)
    @ExcelProperty(value = "课次", index = 6)
    private String sectionName;

    @ColumnWidth(20)
    @ExcelProperty(value = "巡课人员", index = 7)
    private String patrolUserName;

    @ColumnWidth(25)
    @ExcelProperty(value = "提交时间", index = 8)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

}
