package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_content_advice")
@ApiModel(value="Suggest对象", description="意见反馈表")
public class SuggestEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "反馈人id")
    private Long userId;

    @ApiModelProperty(value = "处理人id")
    private Long authorUpdate;

    @ApiModelProperty(value = "字典id")
    private Long categoryId;

    @ApiModelProperty(value = "反馈内容")
    private String adviceName;

    @ApiModelProperty(value = "来源：PC、移动端")
    private String sourceName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "反馈人名称")
    private String author;

    @ApiModelProperty(value = "附件地址id,多个用逗号拼接")
    private String fileUrl;

    @ApiModelProperty(value = "0-未处理；1-已处理")
    private Integer status;

    @ApiModelProperty(value = "处理意见")
    private String checkContent;


    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDate;

}
