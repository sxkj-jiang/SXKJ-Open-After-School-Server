package com.sxkj.edu.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加员工
 *
 * @author lidoudou
 * @date 2017年12月19日下午2:06:31
 */
@Data
public class EmployeeUpdateDTO extends EmployeeBaseDTO {

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Long id;

//    @ApiModelProperty("密码")
//    private String loginPwd;

    @ApiModelProperty("岗位ID 集合")
    private List<Long> positionIdList;

    @ApiModelProperty("角色id集合")
    private List<Long> roleIds;

    @ApiModelProperty("学校id,非超管账户都有值")
    private Long schoolId;

    @ApiModelProperty(value = "用户简介")
    private String userDesc;

    @ApiModelProperty(value = "用户头像")
    private String userLogo;

}
