package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AsStudentBindParentVO {

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("家长id")
    private Long parentId;

    @ApiModelProperty("家长用户id")
    private Long parentUserId;
}
