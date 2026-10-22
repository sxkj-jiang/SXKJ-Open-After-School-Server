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
 * 课程班-行政班安排(enroll_type=3) 批量导入 Excel 模型。
 * <p>列严格对齐运营上传的真实模板"行政班安排"sheet(共13列，不多不少)。</p>
 * <p>同一课后服务名称 + 学期 + 开课周期的行归为同一 t_as_service。</p>
 * <p>学校由登录态自动带入，模板不含学校列。</p>
 * <p>开课周期为范围列(如 2026-09-01至2027-01-31)；日期兼容 yyyy-MM-dd / yyyy/MM/dd / yyyy.MM.dd / 带时分秒 等多种格式。</p>
 * <p>多值(适用年级/适用班级/每周几/上课时间节次)统一用中文顿号"、"分隔。</p>
 *
 * @author huangpu
 * @date 2026-07-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
@ContentRowHeight(18)
@HeadRowHeight(22)
public class AsAdminClassImportExcel extends BaseRowModel {

    @ColumnWidth(25)
    @ExcelProperty(value = "课后服务名称*", index = 0)
    private String serviceName;

    @ColumnWidth(20)
    @ExcelProperty(value = "学期*", index = 1)
    private String termName;

    /** 开课周期，范围格式：起至止，如 2026-09-01至2027-01-31 */
    @ColumnWidth(28)
    @ExcelProperty(value = "开课周期*", index = 2)
    private String courseCycle;

    @ColumnWidth(25)
    @ExcelProperty(value = "课程名称*", index = 3)
    private String courseClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "适用年级*", index = 4)
    private String gradeNames;

    @ColumnWidth(25)
    @ExcelProperty(value = "适用班级*", index = 5)
    private String adminClassNames;

    /** 选课方式，行政班安排固定为 3 */
    @ColumnWidth(12)
    @ExcelProperty(value = "选课方式*", index = 6)
    private String enrollType;

    @ColumnWidth(20)
    @ExcelProperty(value = "课程类别", index = 7)
    private String categoryName;

    @ColumnWidth(15)
    @ExcelProperty(value = "教师姓名*", index = 8)
    private String teacherName;

    @ColumnWidth(15)
    @ExcelProperty(value = "每周几*", index = 9)
    private String dayScope;

    /** 上课时间节次，数值(如 8-第八节)，多个用顿号 */
    @ColumnWidth(18)
    @ExcelProperty(value = "上课时间节次*", index = 10)
    private String sectionNo;

    @ColumnWidth(15)
    @ExcelProperty(value = "上课地点", index = 11)
    private String placeName;

    @ColumnWidth(30)
    @ExcelProperty(value = "课程简介", index = 12)
    private String intro;
}
