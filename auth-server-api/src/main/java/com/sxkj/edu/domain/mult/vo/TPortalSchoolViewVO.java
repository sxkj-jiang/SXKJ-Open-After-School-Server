package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class TPortalSchoolViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "校友风光图片集标题")
    private String viewName;

    @ApiModelProperty(value = "照片附件地址编号")
    private String photoUrl;

    @ApiModelProperty(value = "排序,数值越大越靠前")
    private Integer sort;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "照片附件集合")
    private List<FileVO> fileVOList;

    @ApiModelProperty(value = "是否显示，1-显示，0不显示,默认为1")
    private Integer isShow;

    @ApiModelProperty(value = "浏览量")
    private Integer lookNum;


}
