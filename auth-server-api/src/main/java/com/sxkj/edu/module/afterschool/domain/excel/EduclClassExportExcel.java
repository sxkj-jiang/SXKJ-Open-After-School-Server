package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 班级导出 Excel 模型
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
public class EduclClassExportExcel {

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(15)
    @ExcelProperty(value = "班级编码", index = 1)
    private String classCode;

    @ColumnWidth(15)
    @ExcelProperty(value = "学校名称", index = 2)
    private String schoolName;

    @ColumnWidth(15)
    @ExcelProperty(value = "年级", index = 3)
    private String gradeName;

    @ColumnWidth(15)
    @ExcelProperty(value = "班级", index = 4)
    private String className;

    @ColumnWidth(15)
    @ExcelProperty(value = "班主任", index = 5)
    private String headTeacherName;

    @ColumnWidth(15)
    @ExcelProperty(value = "入学年份", index = 6)
    private Integer enrollmentYear;

    @ColumnWidth(30)
    @ExcelProperty(value = "创建时间", index = 7)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
