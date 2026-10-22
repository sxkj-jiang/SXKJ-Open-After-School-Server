package com.sxkj.edu.domain.dept;

import lombok.Data;

@Data
public class DeptApiResponse {

    private Integer code;

    private String message;

    private String description;

    private String uuid;

    private DeptPageResponse result;
}
