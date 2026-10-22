package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("班主任端评价任务列表VO")
public class HeadteacherEvaluationTaskDetVO {

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("课程班id")
    private Long courseClassId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;


}
