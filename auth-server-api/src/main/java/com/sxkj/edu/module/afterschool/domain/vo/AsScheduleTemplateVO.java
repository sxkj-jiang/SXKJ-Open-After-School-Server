package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 作息管理方案视图对象
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("作息管理方案视图对象")
public class AsScheduleTemplateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方案ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("方案名称")
    private String templateName;

    @ApiModelProperty("启用状态：1启用 0停用")
    private Integer enabledStatus;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("适用年级ID集合")
    private List<Long> gradeIdList;

    @ApiModelProperty("适用年级名称集合")
    private List<String> gradeNameList;

    @ApiModelProperty("时间安排")
    private String timeArrange;

    @ApiModelProperty("适用年级ID集合的JSON数组")
    private String gradeScopeJson;

    @ApiModelProperty("课后时间频率，如周一至周五的JSON数组")
    private String dayScopeJson;

    @ApiModelProperty("开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm:ss")
    private Date endTime;


}
