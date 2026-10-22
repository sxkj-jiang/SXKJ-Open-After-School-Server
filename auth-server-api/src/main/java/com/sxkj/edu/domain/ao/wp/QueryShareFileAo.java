package com.sxkj.edu.domain.ao.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryShareFileAo extends PageParamDTO {


    @ApiModelProperty(value = "标签集合")
    private List<Long> fileLabelId;

    @ApiModelProperty(value = "关键字搜索：文件名/操作人")
    private String keyword;

    @ApiModelProperty("文件存储类型，1-图片，2-文档，3-视频，4-音频，5-其他")
    private Integer storageType;

    @ApiModelProperty("文件后缀")
    private String extentName;

    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 文件状态：0-正常；1-回收站
     */
    private Integer status;

}
