package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;

/**
 * 评价结果导出Excel VO
 * 字段口径与评价结果详情（EvaluationTargetRankingResVO）保持一致
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ColumnWidth(25)
@ContentRowHeight(20)
@HeadRowHeight(25)
public class AsEvaluationResultExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ColumnWidth(8)
    @ExcelProperty(value = "序号", index = 0)
    private String sequence;

    @ColumnWidth(25)
    @ExcelProperty(value = "课程名称", index = 1)
    private String courseClassName;

    @ColumnWidth(15)
    @ExcelProperty(value = "授课教师", index = 2)
    private String teacherName;

    @ColumnWidth(12)
    @ExcelProperty(value = "应评价人数", index = 3)
    private Long evaluatorSumCount;

    @ColumnWidth(12)
    @ExcelProperty(value = "评价人数", index = 4)
    private Long evaluatorCount;

    @ColumnWidth(12)
    @ExcelProperty(value = "平均分", index = 5)
    private Double averageScore;

    @ColumnWidth(12)
    @ExcelProperty(value = "完成率", index = 6)
    private String completionRate;

    @ColumnWidth(12)
    @ExcelProperty(value = "状态", index = 7)
    private String status;
}
