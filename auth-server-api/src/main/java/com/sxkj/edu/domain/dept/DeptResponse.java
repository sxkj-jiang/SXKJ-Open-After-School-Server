package com.sxkj.edu.domain.dept;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DeptResponse {

    @JsonProperty("YXSDM")
    private String code;

    @JsonProperty("YXSMC")
    private String name;

}
