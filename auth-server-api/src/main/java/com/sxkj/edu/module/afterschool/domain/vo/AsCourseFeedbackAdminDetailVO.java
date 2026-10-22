package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课堂反馈详情VO（管理端）
 *
 * @author system
 * @date 2026-06-29
 */
@Data
@ApiModel("课堂反馈详情视图对象（管理端）")
public class AsCourseFeedbackAdminDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("反馈ID")
    private Long id;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程类别")
    private String categoryName;

    @ApiModelProperty("上课时间")
    private String classTime;

    @ApiModelProperty("年级")
    private String gradeNames;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("课堂总结")
    private String classroomSummary;

    @ApiModelProperty("表扬学生名称")
    private String praiseStudentNames;

    @ApiModelProperty("需改进学生名称")
    private String improveStudentNames;

    @ApiModelProperty("课堂照片附件ID集合，JSON数组")
    private String studentFeedbackJson;

    @ApiModelProperty("课堂照片集合")
    private List<FileVO> studentFeedbackFiles;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
