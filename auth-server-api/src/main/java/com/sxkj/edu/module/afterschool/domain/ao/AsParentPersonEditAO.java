package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 家长个人信息编辑AO
 *
 * @author system
 * @date 2026-06-13
 */
@Data
public class AsParentPersonEditAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "当前用户ID", required = true)
    private Long userId;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "家长手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "头像附件ID")
    private Long avatarFileId;

    @NotBlank(message = "家长姓名不能为空")
    @ApiModelProperty(value = "家长姓名", required = true)
    private String parentName;

    @NotBlank(message = "亲属关系不能为空")
    @ApiModelProperty(value = "亲属关系", required = true)
    private String relationName;

    @ApiModelProperty(value = "性别：0男 1女")
    private Integer gender;

    @ApiModelProperty(value = "身份证")
    private String idCard;
}
