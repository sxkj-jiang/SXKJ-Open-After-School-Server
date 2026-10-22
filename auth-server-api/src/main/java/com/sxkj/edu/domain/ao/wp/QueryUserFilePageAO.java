package com.sxkj.edu.domain.ao.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryUserFilePageAO extends PageParamDTO {


    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id集合")
    private List<Long> userIdList;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件存储类型，1-图片，2-文档，3-视频，4-音频，5-其他")
    private Integer storageType;

    @ApiModelProperty("文件后缀")
    private String extentName;

    @ApiModelProperty("标签id")
    private Long labelId;

    @ApiModelProperty("市州id")
    private Long cityId;

    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
