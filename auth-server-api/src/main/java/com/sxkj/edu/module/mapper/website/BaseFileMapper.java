package com.sxkj.edu.module.mapper.website;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.BaseFilePageAo;
import com.sxkj.edu.domain.base.BaseFilePageVo;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 基础-上传附件表 Mapper 接口
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-30
 */
public interface BaseFileMapper extends BaseMapper<BaseFileEntity> {


    Page<BaseFilePageVo> queryPage(Page<BaseFilePageVo> page,@Param("query") BaseFilePageAo pageAo);
}
