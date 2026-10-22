package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TPortalProtocolVO {

    @ApiModelProperty("协议主键id 修改必传")
    private Long id;

    @ApiModelProperty(value = "协议名称")
    private String protocolName;

    @ApiModelProperty(value = "协议编号")
    private String protocolCode;

    @ApiModelProperty(value = "协议类型 1-用户协议 2-隐私政策")
    private Integer protocolType;

    @ApiModelProperty(value = "版本号")
    private String versionNum;

    @ApiModelProperty(value = "是否启用 0-否 1-是")
    private Integer isEnable;

    @ApiModelProperty(value = "条款内容")
    private String protocolContent;

    @ApiModelProperty(value = "使用场景(多选) 1-用户注册 2-用户认证")
    private List<Integer> useCaseList;


    /*分页列表的额外字段*/
    @ApiModelProperty(value = "最后更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
