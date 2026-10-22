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
 * 问题整改工单(管理后台)导出 Excel 模型
 *
 * @author system
 * @date 2026-06-26
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsRectificationOrderAdminExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(20)
    @ExcelProperty(value = "编号", index = 1)
    private String id;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程名称", index = 2)
    private String courseClassName;

    @ColumnWidth(40)
    @ExcelProperty(value = "问题描述", index = 3)
    private String problemDesc;

    @ColumnWidth(20)
    @ExcelProperty(value = "提交人", index = 4)
    private String createUserName;

    @ColumnWidth(20)
    @ExcelProperty(value = "处理人", index = 5)
    private String responsibleTeacherName;

    @ColumnWidth(25)
    @ExcelProperty(value = "提交时间", index = 6)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "截止时间", index = 7)
    private String deadlineTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "整改状态", index = 8)
    private String rectifyStatusName;
}
