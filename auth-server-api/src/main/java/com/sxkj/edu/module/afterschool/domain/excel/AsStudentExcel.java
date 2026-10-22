package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生档案导入/导出 Excel 模型
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
public class AsStudentExcel extends BaseRowModel {


    @ColumnWidth(20)
    @ExcelProperty(value = "学生姓名", index = 0)
    private String studentName;

    @ColumnWidth(20)
    @ExcelProperty(value = "学号", index = 1)
    private String studentNo;

    @ColumnWidth(10)
    @ExcelProperty(value = "性别(0男 1女)", index = 2)
    private String gender;

    @ColumnWidth(20)
    @ExcelProperty(value = "身份证号", index = 3)
    private String idCard;

    @ColumnWidth(24)
    @ExcelProperty(value = "学段名称(小学/初中)", index = 4)
    private String stageName;

    @ColumnWidth(25)
    @ExcelProperty(value = "学期名称", index = 5)
    private String termName;

    @ColumnWidth(15)
    @ExcelProperty(value = "年级名称", index = 6)
    private String gradeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "班级名称", index = 7)
    private String className;

    @ColumnWidth(20)
    @ExcelProperty(value = "亲属关系", index = 8)
    private String relationName;

    @ColumnWidth(20)
    @ExcelProperty(value = "家长姓名", index = 9)
    private String parentName;

    @ColumnWidth(20)
    @ExcelProperty(value = "家长手机号", index = 10)
    private String parentPhone;

    @ColumnWidth(30)
    @ExcelProperty(value = "家庭住址", index = 11)
    private String address;

    @ColumnWidth(15)
    @ExcelProperty(value = "入学年份", index = 12)
    private Integer enrollmentYear;

    @ColumnWidth(30)
    @ExcelProperty(value = "备注", index = 13)
    private String remark;

}
