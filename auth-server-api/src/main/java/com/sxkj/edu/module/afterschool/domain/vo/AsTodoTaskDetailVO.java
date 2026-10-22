package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 待办任务详情视图对象
 */
@Data
@ApiModel("待办任务详情视图对象")
public class AsTodoTaskDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键/编码")
    private Long id;

    @ApiModelProperty("任务标题")
    private String taskTitle;

    @ApiModelProperty("优先级：1低 2中 3高")
    private Integer priorityLevel;

    @ApiModelProperty("待办日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDate;

    @ApiModelProperty("截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadlineTime;

    @ApiModelProperty("任务内容")
    private String taskContent;

    @ApiModelProperty("处理人用户ID")
    private Long userId;

    @ApiModelProperty("处理人姓名")
    private String userName;

    @ApiModelProperty("提醒方式 0不提醒 1提前5分钟 2提前15分钟 3提前30分钟 4提前1小时 5提前1天")
    private Integer remindMode;

    @ApiModelProperty("重复模式 0不重复 1每日 2每周 3每月")
    private Integer repeatMode;

    @ApiModelProperty("附件文件ID集合（JSON数组，内部使用，不直接序列化给前端）")
    @JsonIgnore
    private String fileIds;

    @ApiModelProperty("附件文件集合")
    private List<FileVO> files;

    @ApiModelProperty("任务状态：0待办 1已完成")
    private Integer taskStatus;

    @JsonIgnore
    @ApiModelProperty(value = "来源业务类型，仅供后端解析学期", hidden = true)
    private String sourceBizType;

    @JsonIgnore
    @ApiModelProperty(value = "来源业务ID，仅供后端解析学期", hidden = true)
    private Long sourceBizId;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;
}
