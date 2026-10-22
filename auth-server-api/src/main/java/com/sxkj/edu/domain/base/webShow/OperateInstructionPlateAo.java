package com.sxkj.edu.domain.base.webShow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: OperateInstructionPlateAo
 * @Author yanghui
 * @Date 2025年01月17日
 */
@Data
public class OperateInstructionPlateAo {

    @ApiModelProperty(value = "平台:1 教务端 2 学习平台-web 3 学习平台-移动")
    private Integer belongPlate;

    @ApiModelProperty(value = "用户类型：管理员-0,教师-1,学生-2,财务-3,4-院系领导,5-外部单位")
    private Integer roleType;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;
}
