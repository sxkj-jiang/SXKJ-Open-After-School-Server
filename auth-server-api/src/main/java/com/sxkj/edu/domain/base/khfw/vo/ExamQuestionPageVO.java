package com.sxkj.edu.domain.base.khfw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExamQuestionPageVO {

    @ApiModelProperty("id主键")
    private Long id;

    @ApiModelProperty("试卷编码，系统生成")
    private String examNumber;

    @ApiModelProperty("试卷名称")
    private String title;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty("状态：0 停用，1 启用")
    private Integer enable;

    @ApiModelProperty(value = "适用角色JSON（多个） 1-家长 2-教师 3-管理员")
    private String applicableRoleJson;

    @ApiModelProperty(value = "适用角色名称")
    private String applicableRoleName;

    @ApiModelProperty("题目数量")
    private Integer questNum;

    @ApiModelProperty("学校ID")
    private Long schoolId;
}
