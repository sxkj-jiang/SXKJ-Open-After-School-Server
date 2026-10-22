package com.sxkj.edu.module.system.role.basic.domain.dto;

import com.sxkj.edu.common.validation.EnglishChineseAnno;
import com.sxkj.edu.common.validation.UserName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 角色添加DTO
 *
 * @author listen
 * @date 2017/12/28 09:40
 */
@Data
public class RoleAddDTO {

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", required = true)
    @UserName
    private String roleName;

    @ApiModelProperty(value = "角色编码", required = true)
    @EnglishChineseAnno
    private String code;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    @Length(max = 255, message = "角色描述最多255个字符")
    private String remark;

    @ApiModelProperty("角色是否启用，1-启用，0，禁用")
    private Integer status;

    @ApiModelProperty(value = "角色类型，0-管理员，1-教师，2-学生，3-外部单位，默认为2", required = true)
    @NotNull(message = "角色类型不能为空")
    private Integer roleType;
}
