package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 班主任端-学生调课-课程视图对象
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@ApiModel("班主任-学生调课课程视图")
public class AsHeadteacherStudentAdjustCourseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报名记录ID（当前课程返回）")
    private Long enrollmentId;

    @ApiModelProperty("原去向类型：1课程班 2回家 3校外机构/培训机构")
    private Integer fromDestinationType;

    @ApiModelProperty("原去向名称")
    private String fromDestinationName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课程分类ID")
    private Long categoryId;

    @ApiModelProperty("课程分类名称")
    private String categoryName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("报名方式：1公开选课 2邀请选课 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("教室名称")
    private String placeName;

    @ApiModelProperty("上课时间描述")
    private String scheduleText;

    @ApiModelProperty("上课时间明细")
    private List<String> scheduleItems = new ArrayList<>();

    @ApiModelProperty("课程容量")
    private Integer capacity;

    @ApiModelProperty("公开课限额；目标课程包含学生年级可用名额，当前课程只展示整体限额")
    private AsCourseQuotaVO quota;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("剩余名额")
    private Integer surplusCount;

    @ApiModelProperty("仅跨服务调课可选列表返回true：满额仍可申请，需管理端审核生效")
    private Boolean allowFullCourseAdjust;

    @ApiModelProperty("调课超额提示，普通剩余名额字段仍表示真实可用名额")
    private String capacityWarning;

    @ApiModelProperty("已上课次数（当前课程返回）")
    private Integer learnedSessions;

    @ApiModelProperty("是否合并原去向")
    private Boolean mergedSource;

    @ApiModelProperty("合并原去向明细")
    private List<SourceItem> sourceItems = new ArrayList<>();


    @Data
    @ApiModel("班主任端-学生调课-合并原去向明细")
    public static class SourceItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("报名记录ID")
        private Long enrollmentId;

        @ApiModelProperty("原去向类型：1课程班 2回家 3校外机构/培训机构")
        private Integer fromDestinationType;

        @ApiModelProperty("原去向名称")
        private String fromDestinationName;

        @ApiModelProperty("课后服务ID")
        private Long serviceId;

        @ApiModelProperty("课后服务名称")
        private String serviceName;

        @ApiModelProperty("学期ID")
        private Long termId;

        @ApiModelProperty("学期名称")
        private String termName;

        @ApiModelProperty("上课时间描述")
        private String scheduleText;

        @ApiModelProperty("上课时间明细")
        private List<String> scheduleItems = new ArrayList<>();
    }
}
