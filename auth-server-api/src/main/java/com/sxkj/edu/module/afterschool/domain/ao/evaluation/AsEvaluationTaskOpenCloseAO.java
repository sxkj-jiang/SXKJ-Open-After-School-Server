package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.validation.ChineseTitleAnno;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价任务新增/更新AO
 *
 * @author jiangjj
 * @date 2026-05-23
 */
@Data
@ApiModel("评价任务启用禁用对象")
public class AsEvaluationTaskOpenCloseAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "任务ID",required = true)
    @NotNull(message = "任务ID不能为空")
    private Long id;

    @ApiModelProperty(value = "是否启用：1-是，0-否",required = true)
    @NotNull(message = "是否启用不能为空")
    private Integer isOpen;
}
