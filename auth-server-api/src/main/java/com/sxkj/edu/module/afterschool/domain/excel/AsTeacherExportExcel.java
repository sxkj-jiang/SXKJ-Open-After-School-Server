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
public class AsTeacherExportExcel extends BaseRowModel {

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

    @ColumnWidth(20)
    @ExcelProperty(value = "年级", index = 4)
    private String gradeName;

    @ColumnWidth(25)
    @ExcelProperty(value = "行政班级名称", index = 5)
    private String className;

    @ColumnWidth(20)
    @ExcelProperty(value = "职位", index = 6)
    private String positionName;

    @ColumnWidth(12)
    @ExcelProperty(value = "教师类型:班主任/任课教师", index = 7)
    private String isHeadTeacher;

    @ColumnWidth(12)
    @ExcelProperty(value = "入职年份", index = 8)
    private Long registYear;

    @ColumnWidth(12)
    @ExcelProperty(value = "在职状态", index = 9)
    private String status;

    @ColumnWidth(12)
    @ExcelProperty(value = "账号状态", index = 10)
    private String accountStatus;
}
