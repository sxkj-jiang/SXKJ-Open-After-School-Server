package com.sxkj.edu.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class BuildingExcel extends BaseRowModel {

    @ExcelProperty(value = "教学楼名称", index = 0)
    private String teachingBuildingName;

    @ExcelProperty(value = "所在地", index = 1)
    private String site;
    
    @ExcelProperty(value = "具体地址", index = 2)
    private String specificAddress;

}
