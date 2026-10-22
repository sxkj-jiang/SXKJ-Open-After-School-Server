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
 * 年级导入Excel 模型
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
public class EduclGradeExcel extends BaseRowModel {

    @ColumnWidth(25)
    @ExcelProperty(value = "学校名称", index = 0)
    private String schoolName;

    @ColumnWidth(20)
    @ExcelProperty(value = "阶段(小学/初中/高中等)", index = 1)
    private String schoolStage;

    @ColumnWidth(15)
    @ExcelProperty(value = "年级名称", index = 2)
    private String gradeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "年级主任姓名", index = 3)
    private String directorName;

    @ColumnWidth(30)
    @ExcelProperty(value = "备注", index = 4)
    private String remark;
}
