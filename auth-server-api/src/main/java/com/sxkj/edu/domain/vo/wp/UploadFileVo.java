package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UploadFileVo {

    @ApiModelProperty("时间戳")
    private String timeStampName;

    @ApiModelProperty("跳过上传")
    private boolean skipUpload;

    @ApiModelProperty("是否需要合并分片")
    private boolean needMerge;

    @ApiModelProperty("已经上传的分片")
    private List<Integer> uploaded;


}