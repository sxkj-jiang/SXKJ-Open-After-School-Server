package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 学生学习成果导出 Excel 模型
 *
 * @author system
 * @date 2026-06-19
 */
@Data
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsLearningAchievementExportExcel extends BaseRowModel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(25)
    @ExcelProperty(value = "课后服务名称", index = 1)
    private String serviceName;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程班名称", index = 2)
    private String courseClassName;

    @ColumnWidth(15)
    @ExcelProperty(value = "学号", index = 3)
    private String studentNo;

    @ColumnWidth(15)
    @ExcelProperty(value = "学生姓名", index = 4)
    private String studentName;

    @ColumnWidth(15)
    @ExcelProperty(value = "课次日期", index = 5)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ColumnWidth(20)
    @ExcelProperty(value = "任课老师", index = 6)
    private String teacherName;

    @ColumnWidth(15)
    @ExcelProperty(value = "是否打卡", index = 7)
    private String isCheckInDesc;

    @ColumnWidth(20)
    @ExcelProperty(value = "学期", index = 8)
    private String termName;
}
