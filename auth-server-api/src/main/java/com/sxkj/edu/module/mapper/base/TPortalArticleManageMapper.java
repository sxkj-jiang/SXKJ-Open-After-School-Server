package com.sxkj.edu.module.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.mult.ao.PortalArticleManageAo;
import com.sxkj.edu.domain.mult.ao.TPortalArticleManagePageAO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleManage;
import org.apache.ibatis.annotations.Param;

public interface TPortalArticleManageMapper extends BaseMapper<TPortalArticleManage> {

    //文章管理分页查询
    Page<TPortalArticleManageVO> queryArticleManagePage(Page<TPortalArticleManageVO> page, @Param("query") TPortalArticleManagePageAO query);

    Page<TPortalArticleManageVO>  articleManageeMobileMore(Page<TPortalArticleManageVO> page,@Param("query") PortalArticleManageAo manageAo);
}
