package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 问题整改详情VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
@ApiModel("问题整改详情VO")
public class AsRectificationOrderDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("整改单ID")
    private Long id;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("上课地点ID")
    private String placeId;

    @ApiModelProperty("上课地点名称")
    private String placeName;

    @ApiModelProperty("问题类型(字典code)")
    private String problemType;

    @ApiModelProperty("问题类型名称")
    private String problemTypeName;

    @ApiModelProperty("问题描述")
    private String problemDesc;

    @ApiModelProperty("整改意见")
    private String rectifyOpinion;

    @ApiModelProperty("整改截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTime;

    @ApiModelProperty("问题附件ID集合JSON")
    private String questFileIds;

    @ApiModelProperty("问题附件集合")
    private List<FileVO> questFileList;

    @ApiModelProperty("整改状态 0待整改 1整改中 2已整改 3已完成 4已逾期")
    private Integer rectifyStatus;

    @ApiModelProperty("整改措施")
    private String rectifyMeasure;

    @ApiModelProperty("整改后附件ID集合JSON")
    private String rectifyFileIds;

    @ApiModelProperty("整改后附件集合")
    private List<FileVO> rectifyFileList;

    @ApiModelProperty("巡课老师姓名")
    private String patrolUserName;
}
