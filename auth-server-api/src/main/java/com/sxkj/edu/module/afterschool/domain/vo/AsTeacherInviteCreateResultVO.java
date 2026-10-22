package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教师端创建邀请结果
 */
@Data
@ApiModel("教师端创建邀请结果")
public class AsTeacherInviteCreateResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邀请批次ID")
    private Long inviteId;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("邀请链接")
    private String inviteUrl;

    @ApiModelProperty("二维码文件ID")
    private Long inviteQrFileId;

    @ApiModelProperty("二维码访问地址")
    private String inviteQrFileUrl;

    @ApiModelProperty("是否需要家长确认")
    private Integer parentConfirmRequired;

    @ApiModelProperty("成功邀请人数")
    private Integer selectedCount;

    @ApiModelProperty("成功学生ID")
    private List<Long> successStudentIds = new ArrayList<>();

    @ApiModelProperty("失败明细")
    private List<FailedItem> failedItems = new ArrayList<>();

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @Data
    public static class FailedItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long studentId;
        private String studentNo;
        private String studentName;
        private String reason;
    }
}
