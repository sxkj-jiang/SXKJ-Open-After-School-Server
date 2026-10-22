package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评价任务分页查询AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评价任务分页查询对象")
public class AsEvaluationTaskPageAO extends PageParamDTO {
    
    @ApiModelProperty("任务名称（模糊查询）")
    private String taskName;
    
    @ApiModelProperty("模板ID")
    private Long templateId;
    
    @ApiModelProperty("课程班ID")
    private Long courseClassId;
    
    @ApiModelProperty("评价角色（角色ID）")
    private Long applicableRole;
    
    @ApiModelProperty("任务状态：0-未发布 1-进行中 2-已结束")
    private Integer taskStatus;
    
    @ApiModelProperty("适用场景")
    private Long applicableScene;

    @ApiModelProperty("学校")
    private Long schoolId;
}
