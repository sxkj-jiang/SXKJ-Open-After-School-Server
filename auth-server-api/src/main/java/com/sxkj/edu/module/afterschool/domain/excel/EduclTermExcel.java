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
 * 学期导入/导出 Excel 模型
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class EduclTermExcel extends BaseRowModel {

    @ColumnWidth(25)
    @ExcelProperty(value = "学期名称", index = 0)
    private String termName;

    @ColumnWidth(15)
    @ExcelProperty(value = "学期编码", index = 1)
    private String termCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "学年(如2023-2024)", index = 2)
    private String schoolYear;

    @ColumnWidth(20)
    @ExcelProperty(value = "学期类型(1第一学期 2第二学期 3其他)", index = 3)
    private Integer termType;

    @ColumnWidth(20)
    @ExcelProperty(value = "开始日期(yyyy-MM-dd)", index = 4)
    private String startDate;

    @ColumnWidth(20)
    @ExcelProperty(value = "结束日期(yyyy-MM-dd)", index = 5)
    private String endDate;

    @ColumnWidth(12)
    @ExcelProperty(value = "总周数", index = 6)
    private Integer totalWeeks;

    @ColumnWidth(18)
    @ExcelProperty(value = "状态(1启用 0停用)", index = 7)
    private Integer termStatus;
}
