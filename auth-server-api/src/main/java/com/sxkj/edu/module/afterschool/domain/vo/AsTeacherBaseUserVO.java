package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据学校查询教师
 *
 * @author system
 * @date 2026-06-19
 */
@Data
@ApiModel("查询教师系统用户状态")
public class AsTeacherBaseUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("账号")
    private String loginName;

    @ApiModelProperty("审批状态:0-待审批 1-已通过 2-已驳回")
    private Integer auditStatus;

}
