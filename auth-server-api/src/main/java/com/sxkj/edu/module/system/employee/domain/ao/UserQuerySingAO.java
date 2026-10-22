package com.sxkj.edu.module.system.employee.domain.ao;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户列表DTO
 *
 * @author jiangjj
 * @date 2020年12月21日上午09:09:31
 */
@Data
public class UserQuerySingAO {

    @ApiModelProperty(value = "用户手机号，模糊搜索")
    private String phone;

    @ApiModelProperty(value = "用户姓名，模糊搜索")
    private String actualName;

    @ApiModelProperty(value = "关键字搜索:包含用户名、手机号、姓名")
    private String keyword;

    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;


}
