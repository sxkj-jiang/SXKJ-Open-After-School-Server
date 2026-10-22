package com.sxkj.edu.domain.sys.entity.mult;


import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 教务-文章管理-类型
 * @Author yanghui
 * @Date 2025年08月12日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_portal_article_type")
public class TPortalArticleType extends BaseModel {

    private String articleTypeName; //文章类型名称

    private Integer sort; //排序

    private Integer isEnable; //是否启用 0-否 1-是

    private Integer isValid; //是否有效，1-有效，其他无效

}
