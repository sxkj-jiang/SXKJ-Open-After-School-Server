package com.sxkj.edu.module.system.privilege.domain.ao;

import cn.hutool.crypto.digest.mac.MacEngine;
import com.sxkj.edu.common.anno.ApiModelPropertyEnum;
import com.sxkj.edu.common.validation.ChineseAnno;
import com.sxkj.edu.common.validation.EnglishChineseAnno;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class PrivilegeMenuDisableAo {

    @ApiModelProperty(value = "菜单id，更新施必填",required = true)
    @NotNull(message = "菜单id不能为空")
    private Long id;


}
