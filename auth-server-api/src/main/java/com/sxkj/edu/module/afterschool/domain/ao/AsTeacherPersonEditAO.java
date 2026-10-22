package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师个人信息编辑AO
 *
 * @author system
 * @date 2026-06-13
 */
@Data
public class AsTeacherPersonEditAO implements Serializable {
    private static final long serialVersionUID = 1L;


    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "教师手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "头像附件ID")
    private Long avatarFileId;

    @NotNull(message = "是否班主任不能为空")
    @ApiModelProperty(value = "是否班主任：0否 1是", required = true)
    private Integer isHeadTeacher;

    @ApiModelProperty(value = "职位(岗位)名称")
    private String positionName;
}
