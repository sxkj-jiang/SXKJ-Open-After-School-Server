package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypeAO;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypePageAO;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypeUptDisableAo;
import com.sxkj.edu.domain.mult.vo.TPortalArticleTypeVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleType;

import java.util.List;

public interface TPortalArticleTypeService extends IService<TPortalArticleType> {

    //文章类型新增或编辑
    JsonResult<String> articleTypeUpdateOrIns(TPortalArticleTypeAO update);

    //删除文章类型
    JsonResult<String> deleteArticleTypeInfo(Long id);

    //文章类型分页查询
    PageVO<TPortalArticleTypeVO> queryArticleTypePage(TPortalArticleTypePageAO query);

    //文章类型启用或禁用
    JsonResult<String> articleTypeEnableOrDis(TPortalArticleTypeUptDisableAo ao);

    //文章类型不分页查询
    List<TPortalArticleTypeVO> queryArticleTypeList();

    //小程序文章类型固定不分页查询
    List<TPortalArticleTypeVO> articleTypeAppListQuery();

}
