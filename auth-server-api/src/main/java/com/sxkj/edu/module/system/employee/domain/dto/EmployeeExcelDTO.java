package com.sxkj.edu.module.system.employee.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Excel批量添加用户
 *
 * @author jiangjj
 * @date 2020年12月19日下午2:06:31
 */
@Data
public class EmployeeExcelDTO extends BaseRowModel {


    @ExcelProperty(value = "姓名", index = 0)
    private String actualName;

    @ExcelProperty(value = "登录名", index = 1)
    private String loginName;

    @ExcelProperty(value = "性别,0-男，1-女", index = 2)
    private Integer sex;

    @ExcelProperty(value = "手机号", index = 3)
    private String phone;

    @ExcelProperty(value = "身份证", index = 4)
    private String idCard;

    @ExcelProperty(value = "邮箱", index = 5)
    private String email;

    @ExcelProperty(value = "用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师", index = 6)
    @NotNull(message = "角色类型不能为空")
    private Integer userType;

    @ExcelProperty(value = "部门编号", index = 7)
    private Long departmentId;

}
