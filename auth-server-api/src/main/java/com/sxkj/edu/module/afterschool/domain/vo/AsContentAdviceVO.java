package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 意见反馈视图对象
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@ApiModel("意见反馈视图对象")
public class AsContentAdviceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("意见ID")
    private Long id;

    @ApiModelProperty("来源：1-教师端、2-家长端、3-班主任")
    private Integer sourceType;

    @ApiModelProperty("来源描述")
    private String sourceTypeDesc;

    @ApiModelProperty("意见内容")
    private String adviceName;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("反馈用户ID")
    private Long userId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("意见反馈类型名称(字典)")
    private String categoryName;

    @ApiModelProperty("反馈人")
    private String author;

    @ApiModelProperty("处理人")
    private String handler;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("操作人")
    private Long authorUpdate;

    @ApiModelProperty("附件地址,多个用,拼接")
    private String fileUrl;

    @ApiModelProperty("附件地址集合")
    private List<String> fileUrlList;

    @ApiModelProperty("申请日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("处理日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("状态,1-已处理，0-未处理")
    private Integer status;

    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("意见正文")
    private String checkContent;

    @ApiModelProperty("处理意见")
    private String auditRemark;

}
