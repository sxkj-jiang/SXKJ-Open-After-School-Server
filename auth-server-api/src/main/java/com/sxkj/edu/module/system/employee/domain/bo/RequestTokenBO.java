package com.sxkj.edu.module.system.employee.domain.bo;

import lombok.Getter;


@Getter
public class RequestTokenBO {

    private Long requestUserId;

    private EmployeeBO employeeBO;

    private Long userSchoolId;

    public RequestTokenBO(EmployeeBO employeeBO, Long userSchoolId) {
        this.requestUserId = employeeBO.getId();
        this.userSchoolId = userSchoolId;
        this.employeeBO = employeeBO;
    }

    public RequestTokenBO(EmployeeBO employeeBO) {
        this.requestUserId = employeeBO.getId();
        this.employeeBO = employeeBO;
    }

    @Override
    public String toString() {
        return "RequestTokenBO{" +
                "requestUserId=" + requestUserId +
                "userSchoolId=" + userSchoolId +
                ", employeeBO=" + employeeBO +
                '}';
    }
}
