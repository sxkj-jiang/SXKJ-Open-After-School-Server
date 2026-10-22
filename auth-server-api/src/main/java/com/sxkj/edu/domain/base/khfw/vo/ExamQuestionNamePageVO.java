package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ExamQuestionNamePageVO {

    @ApiModelProperty("试卷id主键")
    private Long id;

    @ApiModelProperty("试卷名称")
    private String title;

    @ApiModelProperty("题量")
    private Integer itemNum;

    @ApiModelProperty(value = "介绍")
    private String introduce;


}
