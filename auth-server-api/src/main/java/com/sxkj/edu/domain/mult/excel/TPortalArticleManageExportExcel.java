package com.sxkj.edu.domain.mult.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 文章管理导出 Excel 模型
 *
 * @author system
 * @date 2026-06-26
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class TPortalArticleManageExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(20)
    @ExcelProperty(value = "编号", index = 1)
    private String id;

    @ColumnWidth(20)
    @ExcelProperty(value = "排序", index = 2)
    private Integer sort;

    @ColumnWidth(40)
    @ExcelProperty(value = "文章名称", index = 3)
    private String newsName;

    @ColumnWidth(50)
    @ExcelProperty(value = "文章简介", index = 4)
    private String introduction;

    @ColumnWidth(25)
    @ExcelProperty(value = "文章分类", index = 5)
    private String articleTypeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "是否外部站点", index = 6)
    private String isOutsideName;

    @ColumnWidth(40)
    @ExcelProperty(value = "链接地址", index = 7)
    private String linkUrl;

    @ColumnWidth(25)
    @ExcelProperty(value = "创建时间", index = 8)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ColumnWidth(15)
    @ExcelProperty(value = "是否展示", index = 9)
    private String isShowName;
}
