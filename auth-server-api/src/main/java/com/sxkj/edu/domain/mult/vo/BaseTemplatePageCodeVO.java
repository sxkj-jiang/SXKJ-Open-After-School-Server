package com.sxkj.edu.domain.mult.vo;

import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板管理-根据页面code查询(下载导入模板)精简返回
 */
@Data
@ApiModel(value = "BaseTemplatePageCodeVO对象", description = "模板管理-下载导入模板列表")
public class BaseTemplatePageCodeVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "菜单子页面key")
    private String menuChildCode;

    @ApiModelProperty(value = "文件")
    private FileVO attachment;
}
