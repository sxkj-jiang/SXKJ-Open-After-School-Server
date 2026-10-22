package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: TPortalInstructionslDetailVO
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
public class TPortalInstructionslDetailVO {

    @ApiModelProperty(value = "操作指南id")
    private Long id;

    @ApiModelProperty(value = "操作指南名称")
    private String operName;

    @ApiModelProperty(value = "操作指南类型，1-操作类，2-体验类，3-其他类")
    private Integer operType;

    @ApiModelProperty(value = "操作指南面向角色，1-教师，2-校友、3-管理员")
    private Integer operRole;

    @ApiModelProperty(value = "操作指南状态，1-已发布(启用)，2-待发布(禁用)")
    private Integer operStatus;

    @ApiModelProperty(value = "操作指南具体内容")
    private String context;

    @ApiModelProperty(value = "操作指南附件地址")
    private String instUrl;

    @ApiModelProperty(value = "操作指南附件集合")
    private List<String> instUrlList;
}
