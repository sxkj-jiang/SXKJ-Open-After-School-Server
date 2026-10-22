package com.sxkj.edu.module.system.employee.domain.ao;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 根据部门查询员工-分页
 *
 * @author jiangjj
 * @date 2020年12月19日下午2:06:31
 */
@Data
public class UserDeptSearchAo extends PageParamDTO {

    @ApiModelProperty(value = "部门名称", required = true)
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    @ApiModelProperty(value = "部门编号")
    private String deptId;

    @ApiModelProperty(value = "搜索关键字")
    private String keyWord;


}
