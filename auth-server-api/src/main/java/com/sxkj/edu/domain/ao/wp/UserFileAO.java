package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserFileAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户文件编号")
    private String userFileId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String filePath;

    @ApiModelProperty("扩展名称")
    private String extendName;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("删除标识(0-未删除，1-已删除)")
    private Integer deleteFlag;

    @ApiModelProperty("删除日期")
    private Date deleteTime;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;
}
