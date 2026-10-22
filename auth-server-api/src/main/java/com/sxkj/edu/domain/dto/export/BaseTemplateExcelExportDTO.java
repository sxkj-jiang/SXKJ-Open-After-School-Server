package com.sxkj.edu.domain.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "FinanceAccountExcelExportDTO对象", description = "财务-收款账户")
@ColumnWidth(25)
@ContentRowHeight(20)
@HeadRowHeight(25)
public class BaseTemplateExcelExportDTO {

    @ExcelProperty(value = "编号", index = 0)
    private String templateCode;

    @ExcelProperty(value = "模板名称", index = 1)
    private String templateName;

    @ExcelProperty(value = "应用菜单", index = 2)
    private String menuName;

    @ExcelProperty(value = "创建人", index = 3)
    private String createUser;

    @ExcelProperty(value = "创建时间", index = 4)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = "启用状态", index = 5)
    private String enable;

}