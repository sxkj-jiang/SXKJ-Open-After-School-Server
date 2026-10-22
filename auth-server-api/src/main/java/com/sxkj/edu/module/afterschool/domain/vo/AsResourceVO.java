package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课后资源VO
 *
 * @author wangyouhuai
 * @date 2026-06-17
 */
@Data
@ApiModel("课后资源视图对象")
public class AsResourceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("资源ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("父目录/父资源ID")
    private Long parentId;

    @ApiModelProperty("所属用户ID")
    private Long ownerUserId;

    @ApiModelProperty("所属用户名称")
    private String ownerUserName;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源类型：1目录 2文档 3图片 4视频 5音频 6链接")
    private Integer resourceType;

    @ApiModelProperty("资源类型描述")
    private String resourceTypeDesc;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件URL")
    private String fileUrl;

    @ApiModelProperty("封面文件ID")
    private Long coverFileId;

    @ApiModelProperty("封面文件URL")
    private String coverFileUrl;

    @ApiModelProperty("资源简介")
    private String intro;

    @ApiModelProperty("共享范围：1个人 2学校 3课程班")
    private Integer shareScope;

    @ApiModelProperty("共享范围描述")
    private String shareScopeDesc;

    @ApiModelProperty("关联课程班ID")
    private Long courseClassId;

    @ApiModelProperty("排序号")
    private Integer sortNo;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
