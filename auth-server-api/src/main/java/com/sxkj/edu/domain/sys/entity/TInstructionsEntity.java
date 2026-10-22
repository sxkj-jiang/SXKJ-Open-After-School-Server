package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 操作指南表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_instructions")
@ApiModel(value="Instructions对象", description="操作指南表")
public class TInstructionsEntity extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作指南名称",required = true)
    @NotBlank(message = "操作指南名称不能为空")
    private String operName;

    @ApiModelProperty(value = "操作指南类型，1-操作类，2-体验类，3-其他类",required = true)
    @NotBlank(message = "操作指南类型不能为空")
    private Integer operType;

    @ApiModelProperty(value = "操作指南面向角色，1-教师，2-学生、3-管理员",required = true)
    @NotBlank(message = "操作指南面向角色不能为空")
    private Integer operRole;

    @ApiModelProperty(value = "操作指南状态，1-已发布，2-待发布",required = true)
    @NotBlank(message = "操作指南状态不能为空")
    private Integer operStatus;

    @ApiModelProperty(value = "操作指南具体内容",required = true)
    @NotBlank(message = "操作指南具体内容不能为空")
    private String context;

    @ApiModelProperty(value = "操作指南文本",required = true)
    @NotBlank(message = "操作指南文本内容不能为空")
    private String sourceContext;

    @ApiModelProperty(value = "操作指南附件地址")
    private String instUrl;


    public TInstructionsEntity() {
        this.setIsValid(1);
        this.setCreateTime(new Date());
    }
}
