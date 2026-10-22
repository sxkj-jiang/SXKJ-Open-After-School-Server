package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 意见反馈导出 Excel 模型
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsContentAdviceExportExcel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "来源", index = 1)
    private String sourceTypeDesc;

    @ColumnWidth(40)
    @ExcelProperty(value = "意见内容", index = 2)
    private String adviceName;

    @ColumnWidth(25)
    @ExcelProperty(value = "学校名称", index = 3)
    private String schoolName;

    @ColumnWidth(20)
    @ExcelProperty(value = "意见反馈类型", index = 4)
    private String categoryName;

    @ColumnWidth(15)
    @ExcelProperty(value = "反馈人", index = 5)
    private String author;

    @ColumnWidth(15)
    @ExcelProperty(value = "手机号", index = 6)
    private String mobile;

    @ColumnWidth(25)
    @ExcelProperty(value = "发布时间", index = 7)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ColumnWidth(15)
    @ExcelProperty(value = "状态", index = 8)
    private String statusDesc;

    @ColumnWidth(30)
    @ExcelProperty(value = "处理意见", index = 9)
    private String checkContent;
}
