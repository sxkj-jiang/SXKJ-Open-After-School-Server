package com.sxkj.edu.module.afterschool.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 排课管理-课后服务导出 Excel 行
 *
 * @author wangyouhuai
 * @date 2026-06-25
 */
@Data
public class AsServiceExportExcel {

    @ExcelProperty("编码")
    private String serviceCode;

    @ExcelProperty("课后服务名称")
    private String serviceName;

    @ExcelProperty("学期")
    private String termName;

    @ExcelProperty("开课周期")
    private String courseDateRange;

    @ExcelProperty("报名时间")
    private String enrollTimeRange;

    @ExcelProperty("创建人")
    private String creatorName;

    @ExcelProperty("发布时间")
    private String publishTime;

    @ExcelProperty("状态")
    private String serviceStatusName;
}
