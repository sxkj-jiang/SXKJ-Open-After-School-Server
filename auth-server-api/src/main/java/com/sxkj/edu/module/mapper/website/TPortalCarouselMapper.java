package com.sxkj.edu.module.mapper.website;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.mult.TPortalCarouselAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselQueryDetailAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselVO;
import com.sxkj.edu.domain.sys.entity.TPortalCarouselEntity;
import org.apache.ibatis.annotations.Param;

/**
 *@Description : 平台管理-轮播图
 *@Author : JIANGJJ
 *@Date : 2022-12-26 20:35
 *@Company : SXKJ
 *@Version : 1.0
 **/
public interface TPortalCarouselMapper extends BaseMapper<TPortalCarouselEntity> {

    Page<TPortalCarouselVO> selectPortalCarouselList(Page<TPortalCarouselVO> page, @Param("query") TPortalCarouselAo query);

    TPortalCarouselVO selectPortalCarouselDetail(@Param("query") TPortalCarouselQueryDetailAo query);
}
