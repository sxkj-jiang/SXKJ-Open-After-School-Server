package com.sxkj.edu.domain.vo.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class QueryRecoveryPageVO {

    @ApiModelProperty("文件id")
    private Long id;

    @ApiModelProperty("操作人id")
    private Long userId;

    @ApiModelProperty("操作人名称")
    private String userName;

    @ApiModelProperty("用户文件编号")
    private String fileMd5;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String filePath;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("回收站删除标识(0-已删除，1-未删除)")
    private Integer isValid;

    @ApiModelProperty("删除日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;

    @ApiModelProperty("文件类型")
    private String extendName;

    @ApiModelProperty("文件访问地址")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件大小")
    private String fileSizeStr;

}
