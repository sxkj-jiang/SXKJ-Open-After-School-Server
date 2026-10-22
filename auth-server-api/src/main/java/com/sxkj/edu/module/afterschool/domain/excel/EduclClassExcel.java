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
 * 班级导入/导出 Excel 模型
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class EduclClassExcel extends BaseRowModel {

    @ColumnWidth(25)
    @ExcelProperty(value = "学校名称", index = 0)
    private String schoolName;

    @ColumnWidth(25)
    @ExcelProperty(value = "年级名称", index = 1)
    private String gradeName;

    @ColumnWidth(25)
    @ExcelProperty(value = "班级名称", index = 2)
    private String className;

    @ColumnWidth(25)
    @ExcelProperty(value = "班主任", index = 3)
    private String headTeacher;

    @ColumnWidth(10)
    @ExcelProperty(value = "入学年份", index = 4)
    private Integer enrollmentYear;

    @ColumnWidth(30)
    @ExcelProperty(value = "备注", index = 5)
    private String remark;
}
