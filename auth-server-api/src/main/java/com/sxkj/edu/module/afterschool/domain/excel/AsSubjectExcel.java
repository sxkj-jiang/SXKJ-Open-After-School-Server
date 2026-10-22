package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学科管理导入 Excel 模型
 *
 * @author system
 * @date 2026-09-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsSubjectExcel extends BaseRowModel {

    @ColumnWidth(20)
    @ExcelProperty(value = "学科名称", index = 0)
    private String subjectName;

    @ColumnWidth(20)
    @ExcelProperty(value = "学科编码", index = 1)
    private String subjectCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "年级编码", index = 2)
    private String gradeCode;

    @ColumnWidth(15)
    @ExcelProperty(value = "启用状态", index = 3)
    private Integer enabled;

    @ColumnWidth(30)
    @ExcelProperty(value = "学科描述", index = 4)
    private String subjectDesc;
}
