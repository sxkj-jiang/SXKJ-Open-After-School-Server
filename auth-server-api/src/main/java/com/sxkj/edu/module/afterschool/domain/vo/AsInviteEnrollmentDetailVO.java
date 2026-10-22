package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 邀请报名详情
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("邀请报名详情")
public class AsInviteEnrollmentDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邀请ID")
    private Long inviteId;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("是否需要家长确认")
    private Integer parentConfirmRequired;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("可确认学生")
    private List<StudentOption> students = new ArrayList<>();

    @Data
    public static class StudentOption implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long studentId;
        private String studentNo;
        private String studentName;
        private Long termId;
        private String termName;
    }
}
