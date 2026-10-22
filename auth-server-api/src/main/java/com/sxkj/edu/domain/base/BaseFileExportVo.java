package com.sxkj.edu.domain.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @Description: BaseFileExportVo
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
@ColumnWidth(25)
@ContentRowHeight(20)
@HeadRowHeight(25)
public class BaseFileExportVo extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "编码", index = 1)
    private Long id;

    @ColumnWidth(50)
    @ExcelProperty(value = "文件名称", index = 2)
    private String fileName;

    @ColumnWidth(20)
    @ExcelProperty(value = "菜单名称", index = 3)
    private String menuType;

    @ColumnWidth(110)
    @ExcelProperty(value = "文件地址", index = 4)
    private String fileUrl;
}
