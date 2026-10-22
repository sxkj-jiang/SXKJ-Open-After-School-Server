package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("评价任务视图对象")
public class AsEvaluationTaskVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("模板ID")
    private Long templateId;
    @ApiModelProperty("模板名称")
    private String templateName;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty(value = "任务描述")
    private String taskDesc;
    @ApiModelProperty("课程班ID集合")
    private List<Long> courseClassIds;
    @ApiModelProperty("课程班名称列表")
    private List<String> courseClassNames;
    @ApiModelProperty("评价角色（角色ID）")
    private Long applicableRole;
    @ApiModelProperty("评价角色描述")
    private String applicableRoleDesc;
    @ApiModelProperty("适用场景ID集合")
    private List<Long> applicableScene;
    @ApiModelProperty("适用场景")
    private List<String> applicableSceneNames;
    @ApiModelProperty("开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty("结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty("任务开始时提醒")
    private Integer remindStart;
    @ApiModelProperty("任务中期提醒")
    private Integer remindMiddle;
    @ApiModelProperty("任务结束前提醒")
    private Integer remindEnd;
    @ApiModelProperty("任务状态")
    private Integer taskStatus;
    @ApiModelProperty("任务状态描述")
    private String taskStatusDesc;
    @ApiModelProperty("是否已发布：0-未发布 1-已发布")
    private Integer isPublished;
    @ApiModelProperty("参与人数")
    private Integer participantCount;
    @ApiModelProperty("已完成人数")
    private Integer completedCount;
    @ApiModelProperty("应参与人数")
    private Integer shouldParticipantCount;
    @ApiModelProperty("完成率")
    private String completionRate;

    @ApiModelProperty("是否启用：1-是，0-否")
    private Integer isOpen;

    @ApiModelProperty("学期ID")
    private Long termId;
    @ApiModelProperty("学期名称")
    private String termName;
}
