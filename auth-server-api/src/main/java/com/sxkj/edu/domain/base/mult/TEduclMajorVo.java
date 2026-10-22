package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TEduclMajorVo {

    @ApiModelProperty("登记Id")
    private Long id;

    @ApiModelProperty("专业名称")
    private String majorName;

    @ApiModelProperty("专业代码")
    private String majorCode;

    @ApiModelProperty("专业说明")
    private String majorDesc;

    @ApiModelProperty("专业库名称")
    private String libraryName;

    @ApiModelProperty("院系名称")
    private String siteName;

    @ApiModelProperty("院系Id")
    private Long siteId;

    @ApiModelProperty("专业库Id")
    private Long libraryId;

    @ApiModelProperty("创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("是否有效，1-有效，其他无效")
    private Integer isValid;

}
