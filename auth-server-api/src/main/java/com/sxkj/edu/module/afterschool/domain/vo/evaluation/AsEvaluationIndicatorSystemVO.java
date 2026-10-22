package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.base.khfw.vo.IndicatorConfigItemVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价指标体系VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价指标体系视图对象")
public class AsEvaluationIndicatorSystemVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("指标体系ID")
    private Long id;
    
    @ApiModelProperty("学校ID")
    private Long schoolId;
    
    @ApiModelProperty("学校名称")
    private String schoolName;
    
    @ApiModelProperty("指标体系名称")
    private String systemName;
    
    @ApiModelProperty("指标体系描述")
    private String systemDesc;
    
    @ApiModelProperty("适用角色JSON")
    private String applicableRoles;
    
    @ApiModelProperty("指标配置JSON")
    private String indicatorConfig;
    
    @ApiModelProperty("状态：1启用 0停用")
    private Integer status;
    
    @ApiModelProperty("状态描述")
    private String statusDesc;
    
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("维度指标配置")
    private List<IndicatorConfigItemVo> indicatorConfigItemVos;
}
