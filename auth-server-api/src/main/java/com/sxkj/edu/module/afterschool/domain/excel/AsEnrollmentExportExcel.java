package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 学生选课导出 Excel 模型。
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsEnrollmentExportExcel extends BaseRowModel {

    @ExcelProperty(value = "课程班名称", index = 0)
    private String courseClassName;

    @ExcelProperty(value = "课程类别", index = 1)
    private String categoryName;

    @ExcelProperty(value = "报名方式", index = 2)
    private String enrollTypeName;

    @ExcelProperty(value = "学号", index = 3)
    private String studentNo;

    @ExcelProperty(value = "姓名", index = 4)
    private String studentName;

    @ExcelProperty(value = "年级", index = 5)
    private String gradeName;

    @ExcelProperty(value = "班级", index = 6)
    private String className;

    @ExcelProperty(value = "未报名课后时间", index = 7)
    private String unregisteredScheduleText;

    @ExcelProperty(value = "分配课后去向", index = 8)
    private String destinationName;

    @ExcelProperty(value = "操作人", index = 9)
    private String operatorName;

    @ExcelProperty(value = "报名/分配时间", index = 10)
    private String enrollTime;

    @ExcelProperty(value = "学期", index = 11)
    private String termName;
}
