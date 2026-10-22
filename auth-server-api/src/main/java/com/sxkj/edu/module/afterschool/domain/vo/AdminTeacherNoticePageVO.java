package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminTeacherNoticePageVO {

    @ApiModelProperty(value = "教师通知ID 编辑必传")
    private Long id;

    @ApiModelProperty(value = "通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "通知内容")
    private String noticeContent;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "通知对象类型 1-行政班 2-课程班 3-学生")
    private Integer noticeObjType;

    @ApiModelProperty(value = "通知对象对应ids，JSON数组")
    private String noticeObjIds;

    @ApiModelProperty(value = "通知对象对应ids名称")
    private String noticeObjIdNames;

    @ApiModelProperty(value = "状态 0-保存草稿 1-发布")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

}
