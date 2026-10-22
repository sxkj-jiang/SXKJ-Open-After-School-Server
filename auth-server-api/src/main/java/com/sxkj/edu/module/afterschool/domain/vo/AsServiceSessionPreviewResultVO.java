package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课后服务课次生成预览结果。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务课次生成预览结果")
public class AsServiceSessionPreviewResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("预计课次数")
    private Integer totalSessionCount = 0;

    @ApiModelProperty("冲突课次数")
    private Integer conflictCount = 0;

    @ApiModelProperty("是否存在冲突")
    private Boolean hasConflict = false;

    @ApiModelProperty("预览课次")
    private List<AsCourseSessionPreviewVO> sessions = new ArrayList<>();
}
