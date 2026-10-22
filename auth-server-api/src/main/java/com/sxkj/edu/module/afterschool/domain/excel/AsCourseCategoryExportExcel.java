package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 课后类别导出 Excel 模型
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
public class AsCourseCategoryExportExcel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(30)
    @ExcelProperty(value = "课后类别名称", index = 1)
    private String categoryName;

    @ColumnWidth(20)
    @ExcelProperty(value = "类别编码", index = 2)
    private String categoryCode;

    @ColumnWidth(18)
    @ExcelProperty(value = "启用状态", index = 3)
    private String enabledStatus;

    @ColumnWidth(10)
    @ExcelProperty(value = "排序", index = 4)
    private Integer sortNo;

    @ColumnWidth(25)
    @ExcelProperty(value = "创建时间", index = 5)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "创建人", index = 6)
    private String createUser;


}
