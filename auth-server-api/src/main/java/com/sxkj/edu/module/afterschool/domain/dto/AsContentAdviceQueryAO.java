package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 意见反馈分页查询参数
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("意见反馈分页查询参数")
public class AsContentAdviceQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("来源：1-教师端、2-家长端、3-班主任")
    private Integer sourceType;

    @ApiModelProperty("意见内容(模糊)")
    private String adviceName;

    @ApiModelProperty("意见反馈分类ID")
    private Long categoryId;

    @ApiModelProperty("反馈人(模糊)")
    private String author;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("状态,1-已处理，0-未处理")
    private Integer status;
}
