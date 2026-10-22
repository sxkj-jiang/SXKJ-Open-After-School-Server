package com.sxkj.edu.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@ColumnWidth(25)
@ContentRowHeight(20)
@HeadRowHeight(25)
public class PlateOperatePvExportVo {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "用户名称", index = 1)
    private String userName;

    @ColumnWidth(25)
    @ExcelProperty(value = "访问时间", index = 2)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "访问端", index = 3)
    private String type;

    @ColumnWidth(20)
    @ExcelProperty(value = "页面名称", index = 4)
    private String model;

    @ColumnWidth(20)
    @ExcelProperty(value = "ip", index = 5)
    private String ip;


}
