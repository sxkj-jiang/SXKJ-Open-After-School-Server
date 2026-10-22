package com.sxkj.edu.domain.base.khfw.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author Administrator
 * @Date 2024/10/15 16:33
 */
@Setter
@Getter
@ToString
public class QuestPracticeExcel extends BaseRowModel {

    @ExcelProperty(value = {"题目导入表", "习题类型*（数值：单选-1，多选-2，文本-3）"}, index = 0)
    private Integer questionType;

    @ExcelProperty(value = {"题目导入表", "题干*"}, index = 1)
    private String title;

    @ExcelProperty(value = {"题目导入表", "选项*"}, index = 2)
    private String prefix;

    @ExcelProperty(value = {"题目导入表", "分值*（数值）"}, index = 3)
    private Integer score;

    @ExcelProperty(value = {"题目导入表", "是否必填*（数值：0-非必填，1-必填）"}, index = 4)
    private Integer isRequired;

//    @ExcelProperty(value = {"题目导入表", "适用角色*（数值：家长-1，教师-2，管理员-3）"}, index = 5)
//    private String applicableRole;

    @ExcelProperty(value = {"题目导入表", "指标体系*"}, index = 5)
    private String indicatorSystemName;

    @ExcelProperty(value = {"题目导入表", "所属维度*"}, index = 6)
    private String dimensionName;


}
