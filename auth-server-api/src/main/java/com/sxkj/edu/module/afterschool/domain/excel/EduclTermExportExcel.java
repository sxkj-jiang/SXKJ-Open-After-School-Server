package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EduclTermExportExcel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(20)
    @ExcelProperty(value = "编号", index = 1)
    private String termCode;

    @ColumnWidth(25)
    @ExcelProperty(value = "学校名称", index = 2)
    private String schoolName;

    @ColumnWidth(30)
    @ExcelProperty(value = "学期名称", index = 3)
    private String termName;

    @ColumnWidth(30)
    @ExcelProperty(value = "学期周期", index = 4)
    private String termRange;

    @ColumnWidth(8)
    @ExcelProperty(value = "总周数", index = 5)
    private String totalWeeks;

    @ColumnWidth(20)
    @ExcelProperty(value = "创建人", index = 6)
    private String createUser;

    @ColumnWidth(30)
    @ExcelProperty(value = "创建时间", index = 7)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
