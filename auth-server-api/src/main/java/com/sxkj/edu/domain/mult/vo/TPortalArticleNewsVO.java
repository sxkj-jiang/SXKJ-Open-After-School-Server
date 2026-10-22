package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TPortalArticleNewsVO
 * @Author yanghui
 * @Date 2024年08月09日
 */
@Data
public class TPortalArticleNewsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("新闻文章名称")
    private String newsName;

    @ApiModelProperty(value = "文章类型：1-新闻/2-公告/3-法律法规/4-其他",required = true)
    @NotBlank(message = "文章类型不能为空")
    private Integer newsType;

    @ApiModelProperty("新闻文章简介")
    private String introduction;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty("有效期起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("有效期止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("是否展示1-是，其他否")
    private Integer isShow;

    @ApiModelProperty(value = "新闻文章正文内容")
    private String content;

    @ApiModelProperty(value = "附件编号")
    private Long pictureId;

    @ApiModelProperty(value = "附件对象")
    private FileVO fileVO;

}
