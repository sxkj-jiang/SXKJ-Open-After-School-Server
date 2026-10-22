package com.sxkj.edu.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SchoolManageSwithAO", description = "学校信息表")
public class SchoolManageSwithAO {

    @ApiModelProperty(value = "学校编号", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学校名称", required = true)
    private String schoolName;

    public SchoolManageSwithAO(){}

    public SchoolManageSwithAO(Long schoolId, String schoolName) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }
}