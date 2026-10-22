package com.sxkj.edu.domain.dept;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DeptPageResponse {

    private Integer page;

    private Integer per;

    private Integer total;

    private Integer max_page;

    private Map<String, String> data_struct;

    private List<DeptResponse> data;
}
