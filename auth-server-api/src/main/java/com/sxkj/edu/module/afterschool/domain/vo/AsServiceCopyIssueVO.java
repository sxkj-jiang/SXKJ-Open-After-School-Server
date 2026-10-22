package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务复制校验明细。
 */
@Data
@ApiModel("课后服务复制校验明细")
public class AsServiceCopyIssueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问题编码")
    private String code;

    @ApiModelProperty("问题对象类型")
    private String objectType;

    @ApiModelProperty("问题对象编号")
    private Long objectId;

    @ApiModelProperty("问题对象名称")
    private String objectName;

    @ApiModelProperty("问题字段")
    private String field;

    @ApiModelProperty("问题说明")
    private String message;
}
