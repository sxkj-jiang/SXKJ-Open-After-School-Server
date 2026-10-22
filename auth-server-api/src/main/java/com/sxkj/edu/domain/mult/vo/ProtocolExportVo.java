package com.sxkj.edu.domain.mult.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;


@Data
@ColumnWidth(25)
@ContentRowHeight(20)
@HeadRowHeight(25)
public class ProtocolExportVo {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(25)
    @ExcelProperty(value = "协议名称", index = 1)
    private String protocolName;

    @ColumnWidth(15)
    @ExcelProperty(value = "协议编号", index = 2)
    private String protocolCode;

    @ColumnWidth(15)
    @ExcelProperty(value = "协议类型", index = 3)
    private String protocolType;

    @ColumnWidth(15)
    @ExcelProperty(value = "版本", index = 4)
    private String versionNum;

    @ColumnWidth(10)
    @ExcelProperty(value = "启用状态", index = 5)
    private String isEnable;

    @ColumnWidth(30)
    @ExcelProperty(value = "创建时间", index = 6)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ColumnWidth(30)
    @ExcelProperty(value = "最后更新时间", index = 7)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
