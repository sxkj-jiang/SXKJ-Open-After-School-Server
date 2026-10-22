package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 考场信息视图对象
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("考场信息视图对象")
public class ExamRoomVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("考场ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("教室名")
    private String roomName;

    @ApiModelProperty("教室位置")
    private String roomAddress;

    @ApiModelProperty("教室编码")
    private String roomNumber;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
