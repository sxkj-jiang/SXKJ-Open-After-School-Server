package com.sxkj.edu.module.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import com.sxkj.edu.domain.common.BaseModelAss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_base_template")
@ApiModel(value = "BaseTemplatePO对象", description = "基础管理-模板管理")
public class BaseTemplatePo extends BaseModelAss {

    @ApiModelProperty(value = "编号")
    @TableField("template_code")
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    @TableField("template_name")
    private String templateName;

    @ApiModelProperty(value = "应用菜单")
    @TableField("menu")
    private String menu;

    @ApiModelProperty(value = "是否启用: 1-是 0-否")
    @TableField("enable")
    private Integer enable;

    @ApiModelProperty(value = "文件")
    @TableField("attachment")
    private Long attachment;

    @ApiModelProperty(value = "菜单子页面key")
    @TableField("menu_child_code")
    private String menuChildCode;

}
