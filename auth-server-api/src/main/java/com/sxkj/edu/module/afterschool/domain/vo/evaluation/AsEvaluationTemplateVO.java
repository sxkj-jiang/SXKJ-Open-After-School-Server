package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价模板视图对象
 */
@Data
@ApiModel("评价模板视图对象")
public class AsEvaluationTemplateVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("模板ID")
    private Long id;
    
    @ApiModelProperty("学校ID")
    private Long schoolId;
    
    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("模板编号")
    private String templateCode;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板描述")
    private String templateDesc;

    @ApiModelProperty("指标体系ID")
    private Long indicatorSystemId;

    @ApiModelProperty("指标体系名称")
    private String indicatorSystemName;

    @ApiModelProperty("适用角色JSON数组（角色ID）")
    private String applicableRoles;

    @ApiModelProperty("适用角色ID列表（关联t_role表）")
    private List<Long> applicableRoleList;

    @ApiModelProperty("适用角色描述")
    private String applicableRolesDesc;
    
    @ApiModelProperty("题目数量")
    private Integer questionCount;
    
    @ApiModelProperty("模板配置JSON（题目列表）")
    private String templateConfig;
    
    @ApiModelProperty("状态：1启用 0停用")
    private Integer templateStatus;

    @ApiModelProperty("计分类型：1-计分 0-不计分")
    private Integer scoreStatus;

    @ApiModelProperty("状态描述")
    private String statusDesc;
    
    @ApiModelProperty("创建人ID")
    private Long createUser;

    @ApiModelProperty("创建人姓名")
    private String createUserName;
    
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
