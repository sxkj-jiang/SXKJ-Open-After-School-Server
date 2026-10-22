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
 * 课后类别导入/导出 Excel 模型
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
public class AsCourseCategoryExcel extends BaseRowModel {

    @ColumnWidth(25)
    @ExcelProperty(value = "类别名称", index = 0)
    private String categoryName;

    @ColumnWidth(25)
    @ExcelProperty(value = "父类别名称", index = 1)
    private String parentCategoryName;

    @ColumnWidth(18)
    @ExcelProperty(value = "状态(1启用 0停用)", index = 2)
    private Integer enabledStatus;

    @ColumnWidth(12)
    @ExcelProperty(value = "排序号", index = 3)
    private Integer sortNo;

    @ColumnWidth(30)
    @ExcelProperty(value = "备注", index = 4)
    private String remark;
}
