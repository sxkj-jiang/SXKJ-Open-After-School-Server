package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 教师课程资源详情VO
 *
 * @author jiangjj
 * @date 2026-07-07
 */
@Data
@ApiModel("教师课程资源详情视图对象")
public class AsTeacherResourceDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程类别名称")
    private String categoryName;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源介绍")
    private String resourceIntr;

    @ApiModelProperty("上传日期（t_wp_owner_file.create_time）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDate;

    @ApiModelProperty("资源封面完整URL")
    private String coverFileUrl;

    @ApiModelProperty("文件集合")
    private List<FileItem> files;

    /**
     * 资源文件项
     */
    @Data
    @ApiModel("资源文件项")
    public static class FileItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("文件名")
        private String fileName;

        @ApiModelProperty("文件完整URL")
        private String fileUrl;

        @ApiModelProperty("文件后缀")
        private String extendName;
    }

}
