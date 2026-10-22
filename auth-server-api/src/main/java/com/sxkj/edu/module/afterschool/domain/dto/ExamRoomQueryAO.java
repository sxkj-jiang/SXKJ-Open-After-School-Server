package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 考场信息查询 AO
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("考场信息查询对象")
public class ExamRoomQueryAO extends PageQuery {

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("教室名")
    private String roomName;

    @ApiModelProperty("教室位置（教学楼）")
    private String roomAddress;

    @ApiModelProperty("教室编码（楼层）")
    private String roomNumber;

    @ApiModelProperty("创建时间-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty("创建时间-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}
