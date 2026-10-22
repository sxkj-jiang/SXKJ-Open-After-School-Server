package com.sxkj.edu.module.afterschool.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AsTodoTaskHomePageAO {

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "查询日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date queryDate;

    @ApiModelProperty(value = "用户角色id  用于小程序任课教师与班主任角色切换")
    private Long roleId;
}
