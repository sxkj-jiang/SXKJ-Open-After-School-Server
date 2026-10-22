package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教师端邀请批次详情
 */
@Data
@ApiModel("教师端邀请批次详情")
public class AsTeacherInviteDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邀请批次ID")
    private Long inviteId;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("邀请链接")
    private String inviteUrl;

    @ApiModelProperty("二维码访问地址")
    private String inviteQrFileUrl;

    @ApiModelProperty("邀请状态：1有效 2失效")
    private Integer inviteStatus;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseClassName;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("是否需要家长确认")
    private Integer parentConfirmRequired;

    @ApiModelProperty("选中人数")
    private Integer selectedCount;

    @ApiModelProperty("待确认人数")
    private Integer pendingConfirmCount;

    @ApiModelProperty("已确认人数")
    private Integer confirmedCount;

    @ApiModelProperty("已拒绝人数")
    private Integer rejectedCount;

    @ApiModelProperty("学生列表")
    private List<StudentItem> students = new ArrayList<>();

    @ApiModelProperty("是否可失效")
    private Boolean canDisable;

    @ApiModelProperty("是否可再次邀请")
    private Boolean canReinvite;

    @Data
    public static class StudentItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long studentId;
        private Long termId;
        private String termName;
        private String studentNo;
        private String studentName;
        private String gradeName;
        private String className;
        private String inviteStudentStatus;
        private String inviteStudentStatusName;
        private Date confirmTime;
    }
}
