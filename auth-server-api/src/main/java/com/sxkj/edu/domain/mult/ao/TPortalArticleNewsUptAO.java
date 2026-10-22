package com.sxkj.edu.domain.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.validation.ChineseTitleAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: TPortalArticleNewsAO
 * @Author yanghui
 * @Date 2024年08月09日
 */
@Data
public class TPortalArticleNewsUptAO {

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "新闻文章名称",required = true)
    @ChineseTitleAnno
    private String newsName;

    @ApiModelProperty(value = "文章类型：1-新闻/2-公告/3-法律法规/4-其他",required = true)
    @NotNull(message = "文章类型不能为空")
    private Integer newsType;

    @ApiModelProperty(value = "新闻文章简介")
    private String introduction;

    @ApiModelProperty(value = "新闻文章内容",required = true)
    @NotBlank(message = "文章简介不能为空")
    private String content;

    @ApiModelProperty(value = "展示状态1-是，其他否,默认为1")
    private Integer isShow;

    @ApiModelProperty(value = "发布有效期起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "有效期止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "附件编号")
    private Long pictureId;
}
