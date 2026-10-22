package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "BaseTemplateVO对象", description = "基础管理-模板管理")
public class BaseTemplateVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "编号")
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "应用菜单名称")
    private String menuName;

    @ApiModelProperty(value = "应用菜单key")
    private String menu;

    @ApiModelProperty(value = "菜单子页面key")
    private String menuChildCode;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "启用状态：1-启用 0-禁用")
    private Integer enable;

    @ApiModelProperty(value = "文件")
    private FileVO attachment;

}