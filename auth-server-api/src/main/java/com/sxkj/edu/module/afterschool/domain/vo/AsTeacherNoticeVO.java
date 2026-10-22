package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AsTeacherNoticeVO {

    @ApiModelProperty(value = "教师通知ID 编辑必传")
    private Long id;
    
    @ApiModelProperty(value = "通知标题")
    private String noticeTitle;
    
    @ApiModelProperty(value = "通知内容")
    private String noticeContent;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "通知附件ids，JSON数组")
    private String noticeFileIds;

    @ApiModelProperty(value = "通知附件数据")
    private List<FileVO> noticeFileList;

    @ApiModelProperty(value = "通知链接路径，JSON数组")
    private List<String> noticeUrlList;

    @ApiModelProperty(value = "通知对象类型 1-行政班 2-课程班 3-学生")
    private Integer noticeObjType;
    
    @ApiModelProperty(value = "通知对象对应ids，JSON数组")
    private String noticeObjIds;

    @ApiModelProperty(value = "通知对象对应ids名称")
    private String noticeObjIdNames;
    
    @ApiModelProperty(value = "状态 0-保存草稿 1-发布")
    private Integer status;

}
