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
 * 教师端问题整改分页查询返回VO
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@ApiModel("教师端问题整改分页查询返回VO")
public class AsRectificationOrderAppVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("整改单ID")
    private Long id;

    @ApiModelProperty("问题类型")
    private String problemType;

    @ApiModelProperty("问题描述")
    private String problemDesc;

    @ApiModelProperty("整改截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTime;

    @ApiModelProperty("巡课时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("巡课老师姓名")
    private String patrolUserName;

    @ApiModelProperty("整改意见")
    private String rectifyOpinion;

    @ApiModelProperty("整改状态 0待整改 1整改中 2已整改 3已完成 4已逾期")
    private String rectifyStatus;

    @ApiModelProperty("整改后附件ID集合JSON")
    private String rectifyFileIds;

    @ApiModelProperty("整改后附件集合")
    private List<FileVO> rectifyFileList;

    @ApiModelProperty("问题附件ID集合JSON")
    private String questFileIds;

    @ApiModelProperty("问题附件集合")
    private List<FileVO> questFileList;
}
