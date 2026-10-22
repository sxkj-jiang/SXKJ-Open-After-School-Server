package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后勤安全检查记录提交对象
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@ApiModel("后勤安全检查记录提交对象")
public class AsLogisticsSafetyRecordSubmitAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "检查记录ID不能为空")
    @ApiModelProperty(value = "检查记录ID", required = true)
    private Long id;

    @NotBlank(message = "窗户关闭附件ID数组JSON不能为空")
    @ApiModelProperty(value = "窗户关闭附件ID数组JSON", required = true)
    private String windowFileIds;

    @NotBlank(message = "电源关闭附件ID数组JSON不能为空")
    @ApiModelProperty(value = "电源关闭附件ID数组JSON", required = true)
    private String powerFilesIds;

    @NotBlank(message = "前后门关闭附件ID数组JSON不能为空")
    @ApiModelProperty(value = "前后门关闭附件ID数组JSON", required = true)
    private String doorFileIds;
}
