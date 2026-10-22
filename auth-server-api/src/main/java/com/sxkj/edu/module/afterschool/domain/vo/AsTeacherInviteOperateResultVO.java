package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师端邀请操作结果
 */
@Data
@ApiModel("教师端邀请操作结果")
public class AsTeacherInviteOperateResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邀请批次ID")
    private Long inviteId;

    @ApiModelProperty("邀请状态")
    private Integer inviteStatus;

    @ApiModelProperty("是否成功")
    private Boolean success;
}
