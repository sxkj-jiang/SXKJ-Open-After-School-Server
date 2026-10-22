package com.sxkj.edu.module.mapper.website;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.down.TDownloadCenterAo;
import com.sxkj.edu.domain.base.down.TDownloadReq;
import com.sxkj.edu.domain.sys.entity.TDownloadCenterEntity;
import com.sxkj.edu.domain.vo.TDownloadCenterQueryVo;
import org.apache.ibatis.annotations.Param;



public interface TDownloadCenterMapper extends BaseMapper<TDownloadCenterEntity> {

    /**
     * 列表分页查询
     * @param page
     * @param query
     * @return
     */
    Page<TDownloadCenterQueryVo> selectDownloadCenterList(Page<TDownloadCenterQueryVo> page, @Param("query") TDownloadReq query);
}
