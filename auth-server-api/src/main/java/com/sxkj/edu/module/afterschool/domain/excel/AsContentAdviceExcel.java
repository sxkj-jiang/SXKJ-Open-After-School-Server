package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 意见反馈导入 Excel 模型
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsContentAdviceExcel extends BaseRowModel {

    @ColumnWidth(15)
    @ExcelProperty(value = "来源(1教师端/2家长端/3班主任)", index = 0)
    private Integer sourceType;

    @ColumnWidth(40)
    @ExcelProperty(value = "意见内容", index = 1)
    private String adviceName;

    @ColumnWidth(15)
    @ExcelProperty(value = "反馈人", index = 2)
    private String author;

    @ColumnWidth(15)
    @ExcelProperty(value = "手机号", index = 3)
    private String mobile;

    @ColumnWidth(15)
    @ExcelProperty(value = "状态(0未处理/1已处理)", index = 4)
    private Integer status;

    @ColumnWidth(30)
    @ExcelProperty(value = "处理意见", index = 5)
    private String checkContent;

    @ColumnWidth(30)
    @ExcelProperty(value = "附件地址(多个用,拼接)", index = 6)
    private String fileUrl;
}
