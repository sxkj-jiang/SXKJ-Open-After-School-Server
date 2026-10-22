package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基础-上传附件表
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_base_file")
@ApiModel(value="BaseFile对象", description="基础-上传附件表")
public class BaseFileEntity {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @ApiModelProperty(value = "上传时间")
    private Date uploadDate;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否有效，1-有效，其他无效")
    private Integer isValid;

    @ApiModelProperty(value = "请求编号，外部云服务id/1-敏感文件，0-其他")
    private String requestId;

    @ApiModelProperty(value = "随机id")
    private String random;

    @ApiModelProperty(value = "密钥过期时间")
    private String expiration;


}
