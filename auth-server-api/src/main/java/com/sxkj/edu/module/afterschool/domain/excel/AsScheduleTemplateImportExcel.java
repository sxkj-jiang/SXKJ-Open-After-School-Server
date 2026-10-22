package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课后时间批量导入 Excel 模型
 * 每行代表一个节次，相同学校名称+课后时间名称的行归为同一方案
 *
 * @author system
 * @date 2026-07-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsScheduleTemplateImportExcel extends BaseRowModel {

    @ColumnWidth(25)
    @ExcelProperty(value = "学校名称", index = 0)
    private String schoolName;

    @ColumnWidth(25)
    @ExcelProperty(value = "课后时间名称", index = 1)
    private String templateName;

    @ColumnWidth(30)
    @ExcelProperty(value = "使用年级", index = 2)
    private String gradeNames;

    @ColumnWidth(20)
    @ExcelProperty(value = "每周几", index = 3)
    private String dayScope;

    @ColumnWidth(15)
    @ExcelProperty(value = "上课时间节次", index = 4)
    private String sectionNo;

}
