package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 考场信息新增/编辑 AO
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("考场信息新增/编辑对象")
public class ExamRoomAddAO {

    @ApiModelProperty("考场ID(编辑时必填)")
    private Long id;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "教室名", required = true)
    @NotBlank(message = "教室名不能为空")
    private String roomName;

    @ApiModelProperty("教室位置")
    private String roomAddress;

    @ApiModelProperty("教室编码")
    private String roomNumber;

    @ApiModelProperty("备注")
    private String remark;
}
