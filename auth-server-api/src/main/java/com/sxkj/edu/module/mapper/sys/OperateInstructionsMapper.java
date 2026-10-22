package com.sxkj.edu.module.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.webShow.OperateInstructionPageAo;
import com.sxkj.edu.domain.base.webShow.OperateInstructionVO;
import com.sxkj.edu.domain.sys.entity.mult.TOperateInstructionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: OperateInstructionsMapper
 * @Author yanghui
 * @Date 2025年01月17日
 */
@Mapper
public interface OperateInstructionsMapper extends BaseMapper<TOperateInstructionsEntity> {

    Page<OperateInstructionVO> queryOperateInstructionPage(Page<OperateInstructionVO> page, @Param("query") OperateInstructionPageAo query);
}
