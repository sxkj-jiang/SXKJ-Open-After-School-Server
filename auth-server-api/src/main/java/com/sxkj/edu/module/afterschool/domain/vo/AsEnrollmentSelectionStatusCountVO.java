package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生选课 Tab 数量汇总。
 */
@Data
@ApiModel("学生选课Tab数量汇总")
public class AsEnrollmentSelectionStatusCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long allCount = 0L;

    @ApiModelProperty("已选课组班数量")
    private Long selectedCount = 0L;

    @ApiModelProperty("未选课组班数量")
    private Long unselectedCount = 0L;
}
