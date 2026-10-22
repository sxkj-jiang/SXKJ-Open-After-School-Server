package com.sxkj.edu.module.system.login.domain;

import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import lombok.Data;


@Data
public class LoginCacheDTO {

    /**
     * 基本信息
     */
    private EmployeeDTO employeeDTO;

    /**
     * 过期时间
     */
    private Long expireTime;
}
