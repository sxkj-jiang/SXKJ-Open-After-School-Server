package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.base.mult.TPortalCarouselAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselQueryDetailAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselUptAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselVO;
import com.sxkj.edu.domain.sys.entity.TPortalCarouselEntity;

public interface TPortalCarouselService extends IService<TPortalCarouselEntity> {

    PageVO<TPortalCarouselVO> queryPortalCarouselList(TPortalCarouselAo query);

    JsonResult<String> updateCarouselInfo(TPortalCarouselUptAo cwUptAo);

    JsonResult<String> deleteCarouselInfo(Long id);

    JsonResult<TPortalCarouselVO> queryPortalCarouselDetail(TPortalCarouselQueryDetailAo query);
}
