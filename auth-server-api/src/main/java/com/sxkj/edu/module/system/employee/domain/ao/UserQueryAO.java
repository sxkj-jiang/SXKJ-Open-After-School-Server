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
public class UserQueryAO extends PageParamDTO {

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户姓名")
    private String actualName;

    @ApiModelProperty(value = "关键字搜索:包含用户名、手机号、姓名")
    private String keyword;

    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    @ApiModelProperty(value = "审批状态: 0-待审批 1-已通过 2-已驳回")
    private String approveStatus;

    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;

    @ApiModelProperty("用户id集合")
    private List<Long> userIds;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty("学校id,非超管账户都有值")
    private Long schoolId;

}
