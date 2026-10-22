package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AsTeacherNoticeAddOrUptAO {

    @ApiModelProperty(value = "教师通知ID 编辑必传", required = true)
    private Long id;

    @NotBlank(message = "通知标题不能为空")
    @ApiModelProperty(value = "通知标题", required = true)
    private String noticeTitle;

    @NotBlank(message = "通知内容不能为空")
    @ApiModelProperty(value = "通知内容", required = true)
    private String noticeContent;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "通知附件ids，JSON数组")
    private String noticeFileIds;

    @ApiModelProperty(value = "通知链接路径，JSON数组")
    private String noticeUrls;

    @NotNull(message = "通知对象类型不能为空")
    @ApiModelProperty(value = "通知对象类型 1-行政班 2-课程班 3-学生", required = true)
    private Integer noticeObjType;

    @NotBlank(message = "通知对象对应ids的JSON不能为空")
    @ApiModelProperty(value = "通知对象对应ids，JSON数组", required = true)
    private String noticeObjIds;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态 0-保存草稿 1-发布", required = true)
    private Integer status;

    @NotNull(message = "角色ID不能为空 班主任/任课教师")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;



}
