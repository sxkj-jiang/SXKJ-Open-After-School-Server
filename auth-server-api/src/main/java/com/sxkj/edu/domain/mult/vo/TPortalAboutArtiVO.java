package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class TPortalAboutArtiVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "学校校友会名称")
    private String intrName;

    @ApiModelProperty(value = "校友会章程标题")
    private String artiName;

    @ApiModelProperty(value = "文章类型id")
    private Long articleTypeId;

    @ApiModelProperty(value = "附件地址编号")
    private Long photoUrl;

    @ApiModelProperty(value = "校友会章程正文")
    private String introduction;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日")
    private Date updateTime;

    @ApiModelProperty(value = "附件对象")
    private FileVO fileVO;

}
