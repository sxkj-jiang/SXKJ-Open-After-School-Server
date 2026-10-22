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
 * 教师档案导入/导出 Excel 模型
 *
 * @author jiangjj
 * @date 2026-05-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsTeacherExcel extends BaseRowModel {

    @ColumnWidth(20)
    @ExcelProperty(value = "教师姓名", index = 0)
    private String teacherName;

    @ColumnWidth(20)
    @ExcelProperty(value = "手机号", index = 1)
    private String phone;

    @ColumnWidth(20)
    @ExcelProperty(value = "教师工号", index = 2)
    private String teacherNo;

    @ColumnWidth(10)
    @ExcelProperty(value = "性别(0男 1女)", index = 3)
    private String gender;

    @ColumnWidth(12)
    @ExcelProperty(value = "教师类型：1班主任 2任课教师", index = 4)
    private Integer isHeadTeacher;

    @ColumnWidth(12)
    @ExcelProperty(value = "入职年份", index = 5)
    private Long registYear;
}
