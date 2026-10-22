package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAdjustSessionDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("教师调课申请视图对象")
public class AsTeacherAdjustRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("申请ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("申请单号")
    private String requestNo;
    @ApiModelProperty("申请人教师ID")
    private Long applicantTeacherId;
    @ApiModelProperty("申请人姓名")
    private String applicantName;
    @ApiModelProperty("课程班ID")
    private Long courseClassId;
    @ApiModelProperty("课后服务ID")
    private Long serviceId;
    @ApiModelProperty("课后服务名称")
    private String serviceName;
    @ApiModelProperty("申请所属学期ID")
    private Long termId;
    @ApiModelProperty("申请所属学期名称")
    private String termName;
    @ApiModelProperty("课程类别ID")
    private Long categoryId;
    @ApiModelProperty("课程类别名称")
    private String categoryName;
    @ApiModelProperty("课程班名称")
    private String courseClassName;
    @ApiModelProperty("班级/适用班级展示")
    private String className;
    @ApiModelProperty("列表课程信息")
    private String courseInfo;
    @ApiModelProperty("原教师ID")
    private Long originalTeacherId;
    @ApiModelProperty("原教师姓名")
    private String originalTeacherName;
    @ApiModelProperty("代课教师ID")
    private Long substituteTeacherId;
    @ApiModelProperty("代课教师姓名")
    private String substituteTeacherName;
    @ApiModelProperty("原地点")
    private String originalPlace;
    @ApiModelProperty("调整后地点")
    private String adjustPlace;

    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date adjustStartDate;

    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date adjustEndDate;
    @ApiModelProperty("调课课次明细JSON")
    private String adjustSessionsJson;
    @ApiModelProperty("总课次数")
    private Integer totalSessions;
    @ApiModelProperty("调课周期展示")
    private String adjustPeriod;
    @ApiModelProperty("代课周期展示")
    private String substitutePeriod;
    @ApiModelProperty("代课周期开始日期，yyyy-MM-dd")
    private String substitutePeriodStartDate;
    @ApiModelProperty("代课周期结束日期，yyyy-MM-dd")
    private String substitutePeriodEndDate;
    @ApiModelProperty("代课周期可选日期，yyyy-MM-dd")
    private List<String> substitutePeriodDateOptions = new ArrayList<>();
    @ApiModelProperty("上课时间展示")
    private String scheduleText;
    @ApiModelProperty("调课原因")
    private String reason;
    @ApiModelProperty("申请状态：0待处理 1已同意 2已拒绝 3已撤回")
    private Integer requestStatus;
    @ApiModelProperty("申请状态描述")
    private String requestStatusDesc;

    @ApiModelProperty("申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;
    @ApiModelProperty("处理人用户ID")
    private Long handleUserId;
    @ApiModelProperty("处理人姓名")
    private String handleUserName;

    @ApiModelProperty("处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;
    @ApiModelProperty("处理意见")
    private String handleOpinion;
    @ApiModelProperty("调课课次明细")
    private List<AsTeacherAdjustSessionDTO> adjustSessions = new ArrayList<>();
    @ApiModelProperty("教师原申请课次明细")
    private List<AsTeacherAdjustSessionDTO> applySessions = new ArrayList<>();
    @ApiModelProperty("管理端可选择的实际代课课次")
    private List<AsTeacherAdjustSessionDTO> availableSessions = new ArrayList<>();
    @ApiModelProperty("管理员最终批准代课的课次ID集合")
    private List<Long> approvedSessionIds = new ArrayList<>();
    @ApiModelProperty("附件列表")
    private List<AsTeacherAdjustAttachmentVO> attachments = new ArrayList<>();
    @ApiModelProperty("是否需要分配代课教师")
    private Boolean needAssignSubstitute = false;
    @ApiModelProperty("是否可分配代课教师")
    private Boolean canAssignSubstitute = false;
    @ApiModelProperty("推荐代课教师列表")
    private List<SubstituteTeacherVO> recommendedSubstituteTeachers = new ArrayList<>();
    @ApiModelProperty("是否可查看详情")
    private Boolean canDetail = true;
    @ApiModelProperty("是否可处理")
    private Boolean canHandle = false;
    @ApiModelProperty("是否可撤回")
    private Boolean canWithdraw = false;
}
