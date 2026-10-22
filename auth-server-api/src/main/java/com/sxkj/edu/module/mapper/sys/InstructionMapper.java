package com.sxkj.edu.module.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.mult.TPortalInstructionsAo;
import com.sxkj.edu.domain.base.mult.TPortalInstructionslVO;
import com.sxkj.edu.domain.sys.entity.TInstructionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 操作指南 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface InstructionMapper extends BaseMapper<TInstructionsEntity> {

    Page<TPortalInstructionslVO> instructionsPageList(Page<TPortalInstructionslVO> page, @Param("query") TPortalInstructionsAo query);

}
