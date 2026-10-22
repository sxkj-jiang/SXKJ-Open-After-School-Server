package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AsCourseStuFeedbackQueryAO {

    @ApiModelProperty("学生打卡反馈记录ID（与课次ID二选一）")
    private Long id;

    @ApiModelProperty("课次ID（与学生打卡反馈记录ID二选一）")
    private Long sessionId;

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("学生唯一身份id")
    private Long studentIdentityId;

}
