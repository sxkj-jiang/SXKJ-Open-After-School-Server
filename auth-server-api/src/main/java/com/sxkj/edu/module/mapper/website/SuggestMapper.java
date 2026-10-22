package com.sxkj.edu.module.mapper.website;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.suggest.request.SuggestPageAo;
import com.sxkj.edu.domain.base.suggest.vo.SuggestVO;
import com.sxkj.edu.domain.sys.entity.SuggestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 意见反馈表 Mapper 接口
 * </p>
 *
 * @author jjj
 * @since 2022-12-23
 */
public interface SuggestMapper extends BaseMapper<SuggestEntity> {

    Page<SuggestVO> queryPageAdvice(Page<SuggestVO> page, @Param("query") SuggestPageAo query);

    SuggestVO detailById(@Param("id")Long id);
}
