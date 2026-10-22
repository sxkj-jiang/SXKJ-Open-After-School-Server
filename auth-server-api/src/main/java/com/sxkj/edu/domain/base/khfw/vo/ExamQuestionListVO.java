package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamQuestionListVO {

    @ApiModelProperty("id主键")
    private Long id;

    @ApiModelProperty("试卷编码，系统生成")
    private String examNumber;

    @ApiModelProperty("试卷名称")
    private String title;
}
