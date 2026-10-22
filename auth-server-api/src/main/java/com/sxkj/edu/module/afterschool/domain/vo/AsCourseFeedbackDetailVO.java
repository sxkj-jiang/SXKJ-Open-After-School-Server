package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课堂表现详情VO
 */
@Data
@ApiModel("课堂表现详情VO")
public class AsCourseFeedbackDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("反馈记录ID")
    private Long id;

    @ApiModelProperty("课堂总结")
    private String classroomSummary;

    @ApiModelProperty("表扬学生ID集合，JSON数组")
    private String praiseStudentIds;

    @ApiModelProperty("表扬学生名称字符串集合")
    private List<String> praiseStudentNames;

    @ApiModelProperty("改进学生ID集合，JSON数组")
    private String improveStudentIds;

    @ApiModelProperty("改进学生名称字符串集合")
    private List<String> improveStudentNames;

    @ApiModelProperty("课堂照片附件ID集合，JSON数组")
    private String studentFeedbackJson;

    @ApiModelProperty("课堂照片附件集合")
    private List<FileVO> studentFeedbackFiles;

    @ApiModelProperty("提交状态：0草稿 1已提交")
    private Integer submitStatus;
}
