package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.mult.ao.*;
import com.sxkj.edu.domain.mult.vo.ProtocolExportVo;
import com.sxkj.edu.domain.mult.vo.TPortalProtocolVO;
import com.sxkj.edu.module.business.entity.TPortalProtocol;

import java.util.List;


public interface PortalProtocolService extends IService<TPortalProtocol> {

    // 协议管理-更新/新增
    JsonResult<String> protocolUpdateOrIns(TPortalProtocolUpOrInsAO upOrInsAO);

    // 协议管理-详情
    TPortalProtocolVO detailArticleType(TPortalProtocolDelOrInfoAo ao);

    // 协议管理-启用/禁用
    JsonResult<String> enableProtocol(TPortalProtocolEnableAo ao);

    // 协议管理-分页查询
    PageVO<TPortalProtocolVO> protocolPageQuery(TPortalProtocolPageAO query);

    JsonResult<String> deleteProtocol(TPortalProtocolDelOrInfoAo ao);

    List<ProtocolExportVo> batchExportProtocol(List<TPortalProtocolVO> pageVoList);

    List<TPortalProtocolVO> getProtocolByUseCase(TPortalProtocolUseCaseAo ao);
}
