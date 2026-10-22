package com.sxkj.edu.module.afterschool.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课后服务课程班下拉筛选查询条件
 */
@Data
@ApiModel("课后服务课程班下拉筛选查询条件")
public class AsCourseClassDropdownQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后类别ID集合")
    private List<Long> categoryIdList;

    @ApiModelProperty("年级ID集合")
    private List<Long> gradeIdList;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("巡课开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("巡课结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("当日日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date currentDate;

    @ApiModelProperty("教师ID")
    private Long teacherId;
}
