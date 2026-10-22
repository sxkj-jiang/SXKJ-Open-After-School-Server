package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.common.BaseModel;
import com.sxkj.edu.domain.mult.ao.*;
import com.sxkj.edu.domain.mult.vo.ProtocolExportVo;
import com.sxkj.edu.domain.mult.vo.TPortalProtocolVO;
import com.sxkj.edu.module.business.entity.TPortalProtocol;
import com.sxkj.edu.module.mapper.website.TPortalProtocolMapper;
import com.sxkj.edu.service.PortalProtocolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortalProtocolServiceImpl extends ServiceImpl<TPortalProtocolMapper, TPortalProtocol> implements PortalProtocolService {


    @Override
    public JsonResult<String> protocolUpdateOrIns(TPortalProtocolUpOrInsAO upOrInsAO) {
        JsonResult<String> result = JsonResult.buildError("操作失败！");

        LambdaQueryWrapper<TPortalProtocol> queryWrapper = new LambdaQueryWrapper<TPortalProtocol>();
        queryWrapper.eq(BaseModel::getIsValid,1);
        if(ObjectUtil.isNotNull(upOrInsAO.getId())) queryWrapper.ne(TPortalProtocol::getId,upOrInsAO.getId());
        queryWrapper.and(wq -> wq
                .eq(TPortalProtocol::getProtocolCode,upOrInsAO.getProtocolCode().trim())
                .or()
                .eq(TPortalProtocol::getProtocolName, upOrInsAO.getProtocolName().trim()));

        if(this.count(queryWrapper) > 0) return JsonResult.buildError("协议名称或编码重复！");

        boolean flag = false;

        TPortalProtocol origin = new TPortalProtocol();
        BeanUtil.copyProperties(upOrInsAO, origin);
        origin.setUseCases(upOrInsAO.getUseCaseList().stream().map(Objects::toString).collect(Collectors.joining(",")));
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        if (null == upOrInsAO.getId()) {
            origin.setCreateTime(new Date());
            origin.setCreateUser(userId);
            flag = this.save(origin);
        } else {
            origin.setUpdateTime(new Date());
            origin.setUpdateUser(userId);
            flag = this.updateById(origin);
        }
        if(flag) result = JsonResult.buildSuccess("操作成功！");
        return result;
    }

    @Override
    public TPortalProtocolVO detailArticleType(TPortalProtocolDelOrInfoAo ao) {
        TPortalProtocolVO vo = new TPortalProtocolVO();

        TPortalProtocol portalProtocol = this.getById(ao.getId());
        if(null != portalProtocol) {
            SmartBeanUtil.copyProperties(portalProtocol, vo);
            if(StringUtils.isNotBlank(portalProtocol.getUseCases())) {
                vo.setUseCaseList(Arrays.stream(portalProtocol.getUseCases().split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            }
        }
        return vo;
    }

    @Override
    public JsonResult<String> enableProtocol(TPortalProtocolEnableAo ao) {
        JsonResult<String> result = JsonResult.buildError("操作失败！");

        TPortalProtocol portalProtocol = this.getById(ao.getId());
        if(null == portalProtocol) return JsonResult.buildError("暂无数据！");

        portalProtocol.setIsEnable(ao.getIsEnable());
        portalProtocol.setUpdateTime(new Date());
        portalProtocol.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(portalProtocol);
        if(flag) result = JsonResult.buildSuccess("操作成功！");

        return result;
    }

    @Override
    public PageVO<TPortalProtocolVO> protocolPageQuery(TPortalProtocolPageAO query) {
        Page<TPortalProtocolVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        baseMapper.queryProtocolPage(page, query);
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public JsonResult<String> deleteProtocol(TPortalProtocolDelOrInfoAo ao) {
        JsonResult<String> result = JsonResult.buildError("操作失败");

        TPortalProtocol portalProtocol = this.getById(ao.getId());
        if(null == portalProtocol) return JsonResult.buildError("暂无数据");

        portalProtocol.setIsValid(0);
        portalProtocol.setUpdateTime(new Date());
        portalProtocol.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(portalProtocol);
        if(flag) result = JsonResult.buildSuccess("操作成功");

        return result;
    }

    @Override
    public List<ProtocolExportVo> batchExportProtocol(List<TPortalProtocolVO> pageVoList) {

        List<ProtocolExportVo> protocolExportVoList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(pageVoList)) {
            for (int i = 0; i < pageVoList.size(); i++) {
                ProtocolExportVo protocolExportVo = new ProtocolExportVo();

                TPortalProtocolVO protocolVO = pageVoList.get(i);
                BeanUtil.copyProperties(protocolVO,protocolExportVo );
                protocolExportVo.setSequence(String.valueOf(i+1));
                String enable = 1 == protocolVO.getIsEnable() ? "启用":"禁用";
                protocolExportVo.setIsEnable(enable);
                if(1 == protocolVO.getProtocolType()) protocolExportVo.setProtocolType("用户协议");
                if(2 == protocolVO.getProtocolType()) protocolExportVo.setProtocolType("隐私政策");
                protocolExportVoList.add(protocolExportVo);
            }
        }
        return protocolExportVoList;
    }

    @Override
    public List<TPortalProtocolVO> getProtocolByUseCase(TPortalProtocolUseCaseAo ao) {
        List<TPortalProtocolVO> list = new ArrayList<>();

        LambdaQueryWrapper<TPortalProtocol> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalProtocol::getIsEnable,1)
                .eq(TPortalProtocol::getIsValid,1)
                .like(TPortalProtocol::getUseCases,ao.getUseCase());

        List<TPortalProtocol> protocols = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(protocols)) list=SmartBeanUtil.copyList(protocols,TPortalProtocolVO.class);

        return list;
    }
}
