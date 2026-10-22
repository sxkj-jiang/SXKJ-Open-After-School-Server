package com.sxkj.edu.module.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypePageAO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleTypeVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleType;
import org.apache.ibatis.annotations.Param;

public interface TPortalArticleTypeMapper extends BaseMapper<TPortalArticleType> {

    //文章类型管理分页查询
    Page<TPortalArticleTypeVO> queryArticleTypePage(Page<TPortalArticleTypeVO> page, @Param("query") TPortalArticleTypePageAO query);
}
