package com.sxkj.edu.domain.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SyncEsDataDtO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 应用id
    private String appId;

    // 标题
    private String title;

    // 内容id
    private Long contentId;

    // 地址
    private String url;

    // 正文
    private String content;

    // 类型
    private String type;
}
