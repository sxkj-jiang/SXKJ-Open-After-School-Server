package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsTeacherNoticeExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(30)
    @ExcelProperty(value = "通知标题", index = 1)
    private String noticeTitle;

    @ColumnWidth(35)
    @ExcelProperty(value = "通知内容", index = 2)
    private String noticeContent;

    @ColumnWidth(30)
    @ExcelProperty(value = "通知对象", index = 3)
    private String noticeObjIdNames;

    @ColumnWidth(15)
    @ExcelProperty(value = "创建人", index = 4)
    private String createUserName;

    @ColumnWidth(15)
    @ExcelProperty(value = "发布状态", index = 5)
    private String statusName;

    @ColumnWidth(25)
    @ExcelProperty(value = "创建时间", index = 6)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
