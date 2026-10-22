package com.sxkj.edu.domain.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class TPortalSchoolViesPageAO extends PageQuery {

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "相册名称")
    private String viewName;

    @ApiModelProperty(value = "是否显示，1-显示，0不显示,默认为1")
    private Integer isShow;

    @ApiModelProperty(value = "创建起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}
