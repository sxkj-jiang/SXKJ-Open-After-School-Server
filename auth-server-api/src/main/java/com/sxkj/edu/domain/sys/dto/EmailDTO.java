package com.sxkj.edu.domain.sys.dto;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;


@Data
public class EmailDTO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("收件人")
    private String toEmails;

    @ApiModelProperty("发送状态 0未发送 1已发送")
    private Integer sendStatus;

    @ApiModelProperty("邮件内容")
    private String content;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
