package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师课程资源状态VO
 *
 * @author jiangjj
 * @date 2026-07-07
 */
@Data
@ApiModel("教师课程资源状态视图对象")
public class AsTeacherResourceStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("资源ID")
    private Long resourceId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源介绍")
    private String resourceIntr;

    @ApiModelProperty("收藏状态：0-未收藏，1-已收藏")
    private Integer collectStatus;

    @ApiModelProperty("资源封面完整URL")
    private String coverFileUrl;

}
