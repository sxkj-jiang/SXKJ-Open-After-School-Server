package com.sxkj.edu.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TPortalIndustryExcel
 * @Author yanghui
 * @Date 2024年10月25日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TPortalIndustryExcel extends BaseRowModel {

    @ExcelProperty(value = "行业名称", index = 0)
    private String industryName;

    @ExcelProperty(value = "行业代码", index = 1)
    private String code;

    @ExcelProperty(value = "排序", index = 2)
    private Integer sort;

    @ExcelProperty(value = "描述", index = 3)
    private String describeNotes;
}
