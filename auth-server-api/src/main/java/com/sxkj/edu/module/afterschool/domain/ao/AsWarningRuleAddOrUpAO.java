package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AsWarningRuleAddOrUpAO {

    @ApiModelProperty(value = "预警ID 编辑时填写")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "预警类型：1-迟到预警 2-缺勤预警 3-早退预警")
    private Integer warningType;

    @ApiModelProperty(value = "迟到预警时长(分钟)")
    private Integer lateMinute;

    @ApiModelProperty(value = "预警状态：1普通 2预警 3紧急")
    private Integer warningLevel;

    @ApiModelProperty(value = "通知对象 1-班主任 2-家长")
    private List<Integer> noticeObjects;

    @ApiModelProperty(value = "通知方式 1-系统通知 2-短信通知")
    private Integer noticeWay;

    @ApiModelProperty(value = "通知模板")
    private String noticeTemplate;

    @ApiModelProperty(value = "状态：1启用 0停用")
    private Integer status;

    @ApiModelProperty(value = "是否删除：0-删除 1-正常")
    private Integer isValid;
}
