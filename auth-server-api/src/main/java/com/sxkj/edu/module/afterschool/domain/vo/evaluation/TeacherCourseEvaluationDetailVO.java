package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.base.khfw.vo.QuestionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("任课老师端课程评价详情VO")
public class TeacherCourseEvaluationDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("上课时间描述")
    private String classTimeDesc;

    @ApiModelProperty("开课周期")
    private String coursePeriod;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("课程类型")
    private String categoryName;

    @ApiModelProperty("平均评分")
    private BigDecimal averageScore;
    @ApiModelProperty("总评分")
    private BigDecimal score;

    @ApiModelProperty("已评人数")
    private Integer evaluationCount;

    @ApiModelProperty("应评总人数")
    private Integer enrolledCount;

    @ApiModelProperty("评价列表")
    private List<EvaluationItem> evaluationList;

    @ApiModelProperty("题目列表")
    private List<QuestionVO> questionList;

    @Data
    @ApiModel("单条评价项")
    public static class EvaluationItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("答案ID")
        private Long answerId;

        @ApiModelProperty("评价人账号ID")
        private Long evaluatorUserId;

        @ApiModelProperty("评价人姓名")
        private String evaluatorName;

        @ApiModelProperty("学生账号ID")
        private Long studentUserId;

        @ApiModelProperty("教师id")
        private Long teacherId;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("评价人头像文件ID")
        private Long avatarFileId;

        @ApiModelProperty("评价总分")
        private BigDecimal totalScore;

        @ApiModelProperty("评价文字意见")
        private String commentText;

        @ApiModelProperty("作答明细JSON")
        private String answerJson;

        @ApiModelProperty("维度得分明细")
        private List<DimensionScore> dimensionScores;

        @ApiModelProperty("提交时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date submitTime;
    }

    @Data
    @ApiModel("维度得分明细")
    public static class DimensionScore implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("维度名称")
        private String dimensionName;

        @ApiModelProperty("维度得分")
        private BigDecimal dimensionScore;

        @ApiModelProperty("维度满分")
        private BigDecimal dimensionMaxScore;
    }
}
