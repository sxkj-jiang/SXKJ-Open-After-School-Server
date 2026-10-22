package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsContentAdviceEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsContentAdviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AsContentAdviceMapper extends BaseMapper<AsContentAdviceEntity> {

    /**
     * 分页查询意见反馈列表
     */
    Page<AsContentAdviceVO> pageContentAdviceList(Page<AsContentAdviceVO> page, @Param("query") AsContentAdviceQueryAO query);

    /**
     * 查询意见反馈详情
     */
    AsContentAdviceVO selectContentAdviceDetail(@Param("id") Long id);
}
