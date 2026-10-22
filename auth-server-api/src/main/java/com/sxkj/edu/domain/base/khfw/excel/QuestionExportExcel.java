package com.sxkj.edu.domain.base.khfw.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 试题库导出 Excel 模型
 *
 * @author system
 * @date 2026-06-26
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class QuestionExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(20)
    @ExcelProperty(value = "试题编号", index = 1)
    private String examNumber;

    @ColumnWidth(50)
    @ExcelProperty(value = "题目内容", index = 2)
    private String title;

    @ColumnWidth(15)
    @ExcelProperty(value = "题型", index = 3)
    private String questionTypeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "适用角色", index = 4)
    private String applicableRole;

    @ColumnWidth(30)
    @ExcelProperty(value = "评价指标体系", index = 5)
    private String systemName;

    @ColumnWidth(20)
    @ExcelProperty(value = "所属维度", index = 6)
    private String dimension;

}
