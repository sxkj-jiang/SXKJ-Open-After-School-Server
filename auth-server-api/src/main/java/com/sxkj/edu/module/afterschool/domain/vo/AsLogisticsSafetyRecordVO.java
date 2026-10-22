package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("后勤安全检查记录视图对象")
public class AsLogisticsSafetyRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("记录ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("检查日期")
    private Date checkDate;
    @ApiModelProperty("检查人用户ID")
    private Long checkUserId;
    @ApiModelProperty("检查人姓名")
    private String checkUserName;
    @ApiModelProperty("检查结果")
    private Integer checkResult;
    @ApiModelProperty("检查结果描述")
    private String checkResultDesc;
    @ApiModelProperty("问题描述")
    private String problemDesc;
    @ApiModelProperty("处理结果")
    private String handleResult;
    @ApiModelProperty("附件文件ID集合JSON")
    private String attachmentIds;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
