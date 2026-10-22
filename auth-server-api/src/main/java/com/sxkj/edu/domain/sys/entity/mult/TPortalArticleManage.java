package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 教务-文章管理
 * @Author yanghui
 * @Date 2025年08月12日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_portal_article_manage")
public class TPortalArticleManage extends BaseModel {

    private String newsName; //文章名称

    private Long articleTypeId; //文章类型id

    private String introduction; //文章简介

    private String content; //文章内容

    private Date startTime; //有效期起

    private Date endTime; //有效期止

    private Integer isShow; //是否展示1-是，其他否

    private Integer sort; //排序

    private Integer isValid; //是否有效，1-有效，其他无效

    private String photoUrl; //文章图片路径

    private Long schoolId; //学校id

    private Integer isOutside; //是否外部站点 1-是 0-否

    private String linkUrl; //链接地址
}
