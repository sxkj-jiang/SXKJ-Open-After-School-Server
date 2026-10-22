package com.sxkj.edu.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: BaseFileUploadAo
 * @Author yanghui
 * @Date 2024年12月27日
 */
@Data
public class BaseFileUploadVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @ApiModelProperty(value = "附件类型")
    private String fileType;

    @ApiModelProperty(value = "附件地址")
    private String fileUrl;

    @ApiModelProperty(value = "菜单名称，来源")
    private String menuType;

    @ApiModelProperty(value = "操作人")
    private Long operUser;


    @ApiModelProperty(value = "上传时间")
    private Date uploadDate;

    @ApiModelProperty(value = "请求编号，外部云服务id/1-敏感文件，0-其他")
    private String requestId;

    @ApiModelProperty(value = "随机id")
    private String random;

}
