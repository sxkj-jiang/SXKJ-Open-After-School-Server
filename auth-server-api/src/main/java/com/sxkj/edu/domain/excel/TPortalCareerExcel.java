package com.sxkj.edu.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TPortalCareerExcel
 * @Author yanghui
 * @Date 2024年10月25日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TPortalCareerExcel extends BaseRowModel {

    @ExcelProperty(value = "职业名称", index = 0)
    private String name;

    @ExcelProperty(value = "职业代码", index = 1)
    private String code;

    @ExcelProperty(value = "所属行业代码", index = 2)
    private String industryCode;

    @ExcelProperty(value = "排序", index = 3)
    private Integer sort;
}
