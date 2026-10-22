package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 后勤安全检查记录详情视图对象
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@ApiModel("后勤安全检查记录详情视图对象")
public class AsLogisticsSafetyRecordDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("检查记录ID")
    private Long id;

    @ApiModelProperty("检查日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date checkDate;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("提交教师名称")
    private String teacherName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程类别名称")
    private String categoryName;

    @JsonIgnore
    private String windowFileIds;

    @JsonIgnore
    private String powerFilesIds;

    @JsonIgnore
    private String doorFileIds;

    @ApiModelProperty("窗户关闭附件集合")
    private List<FileVO> windowFiles;

    @ApiModelProperty("电源关闭附件集合")
    private List<FileVO> powerFiles;

    @ApiModelProperty("前后门关闭附件集合")
    private List<FileVO> doorFiles;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
