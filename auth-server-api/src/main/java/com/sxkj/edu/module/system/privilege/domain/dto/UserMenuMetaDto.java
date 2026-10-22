package com.sxkj.edu.module.system.privilege.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class UserMenuMetaDto implements Serializable {
    private static final long serialVersionUID = 3848490966432912414L;

    @ApiModelProperty("icon地址")
    private String icon;

    @ApiModelProperty("排序")
    private Integer order;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("前端路由")
    private String activePath;

    @ApiModelProperty("前端是否隐藏,1-true,0-false")
    private Boolean hideInMenu;


}
