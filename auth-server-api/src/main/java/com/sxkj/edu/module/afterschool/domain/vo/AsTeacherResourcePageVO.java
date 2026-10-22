package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师课程资源分页VO
 *
 * @author jiangjj
 * @date 2026-07-07
 */
@Data
@ApiModel("教师课程资源分页视图对象")
public class AsTeacherResourcePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("资源ID")
    private Long resourceId;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源介绍")
    private String resourceIntr;

    @ApiModelProperty("资源类型")
    private String resourceType;

    @ApiModelProperty("文件后缀")
    private String extendName;

    @ApiModelProperty("上传日期 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDate;

    @ApiModelProperty("资源封面完整URL")
    private String coverFileUrl;

}
