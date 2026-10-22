package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 考场信息导出 Excel 模型
 *
 * @author system
 * @date 2026-05-25
 */
@Data
public class ExamRoomExportExcel extends BaseRowModel {

    @ExcelProperty(value = "序号", index = 0)
    @ColumnWidth(10)
    private String sequence;

    @ExcelProperty(value = "编码", index = 1)
    @ColumnWidth(10)
    private String id;

    @ExcelProperty(value = "学校名称", index = 2)
    @ColumnWidth(30)
    private String schoolName;

    @ExcelProperty(value = "教学楼", index = 3)
    @ColumnWidth(30)
    private String roomAddress;

    @ExcelProperty(value = "楼层", index = 4)
    @ColumnWidth(20)
    private String roomNumber;

    @ExcelProperty(value = "教室名称", index = 5)
    @ColumnWidth(20)
    private String roomName;

    @ExcelProperty(value = "创建时间", index = 6)
    @ColumnWidth(35)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
