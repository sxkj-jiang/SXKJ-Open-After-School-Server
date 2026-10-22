package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 根据学生查询预警详情VO
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("学生预警详情VO")
public class AsStudentWarningDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("头像文件URL")
    private String avatarFileUrl;

    @ApiModelProperty("自然班级名称")
    private String className;

    @ApiModelProperty("考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("考勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("预警级别：1普通 2预警 3紧急")
    private Integer warningLevel;

    @ApiModelProperty("预警级别描述")
    private String warningLevelDesc;

    @ApiModelProperty("预警信息列表（按预警时间倒序）")
    private List<WarningInfoVO> warningInfoList;

    @ApiModelProperty("去向轨迹时间轴（按课次时间倒序）")
    private List<DestinationTrajectoryVO> destinationTrajectoryList;

    /**
     * 预警信息VO
     */
    @Data
    @ApiModel("预警信息VO")
    public static class WarningInfoVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("预警记录ID")
        private Long warningRecordId;

        @ApiModelProperty("预警时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
        private Date warningTime;

        @ApiModelProperty("预警说明")
        private String warningDesc;

        @ApiModelProperty("处理状态：0未处理 1处理中 2已处理 3已关闭")
        private Integer handleStatus;

        @ApiModelProperty("处理状态描述")
        private String handleStatusDesc;

        @ApiModelProperty("预警级别：1普通 2预警 3紧急")
        private Integer warningLevel;

        @ApiModelProperty("预警级别描述")
        private String warningLevelDesc;

        @ApiModelProperty("备注")
        private String remark;
    }
}
