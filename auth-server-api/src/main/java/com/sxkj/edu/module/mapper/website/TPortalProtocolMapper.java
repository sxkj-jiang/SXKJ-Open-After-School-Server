package com.sxkj.edu.module.mapper.website;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.mult.ao.TPortalProtocolPageAO;
import com.sxkj.edu.domain.mult.vo.TPortalProtocolVO;
import com.sxkj.edu.module.business.entity.TPortalProtocol;
import org.apache.ibatis.annotations.Param;

public interface TPortalProtocolMapper extends BaseMapper<TPortalProtocol> {

    Page<TPortalProtocolVO> queryProtocolPage(Page<TPortalProtocolVO> page, @Param("query") TPortalProtocolPageAO query);
}
