package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 考场信息导入 Excel 模型
 *
 * @author system
 * @date 2026-05-25
 */
@Data
public class ExamRoomExcel extends BaseRowModel {

    @ExcelProperty(value = "学校名称", index = 0)
    @ColumnWidth(20)
    private String schoolName;

    @ExcelProperty(value = "教学楼", index = 1)
    @ColumnWidth(30)
    private String roomAddress;

    @ExcelProperty(value = "楼层", index = 2)
    @ColumnWidth(20)
    private String roomNumber;

    @ExcelProperty(value = "教室名", index = 3)
    @ColumnWidth(20)
    private String roomName;

    @ExcelProperty(value = "备注", index = 4)
    @ColumnWidth(30)
    private String remark;
}
