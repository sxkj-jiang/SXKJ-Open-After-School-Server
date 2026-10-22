package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class TPortalAboutContactVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "学校校友会名称")
    private String intrName;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系电话")
    private String contactMobile;

    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "联系地址")
    private String contactAddress;

    @ApiModelProperty(value = "文章类型id")
    private Long articleTypeId;

    @ApiModelProperty(value = "附件地址编号")
    private Long photoUrl;

    @ApiModelProperty(value = "排序,数值越大越靠前")
    private Integer sort;

    @ApiModelProperty(value = "详细内容")
    private String introduction;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日")
    private Date updateTime;

    @ApiModelProperty(value = "附件对象")
    private FileVO fileVO;

}
