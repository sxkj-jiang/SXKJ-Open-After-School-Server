package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("家长端评价任务列表AO")
public class GuardianEvaluationTaskListAO extends PageParamDTO {

    @ApiModelProperty("Tab类型： 1-我对老师的评价 2-我收到的评价")
    private Integer tabType;

    @ApiModelProperty("评价状态：null或0-全部 1-待评价 2-已评价")
    private Integer evalStatus;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;

    @ApiModelProperty("学生稳定身份ID，家长绑定多个孩子时用于指定学生")
    private Long studentIdentityId;
}
