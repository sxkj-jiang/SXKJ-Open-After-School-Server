package com.sxkj.edu.module.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.wp.QueryShareFileAo;
import com.sxkj.edu.domain.vo.wp.QueryFilePageVO;
import com.sxkj.edu.module.business.entity.wp.WpUserFilePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WpBaseFileDao extends BaseMapper<WpUserFilePo> {

    Page<QueryFilePageVO> selectFilePage(Page<QueryFilePageVO> page, @Param("query") QueryShareFileAo query);

    QueryFilePageVO selectFilePageDetail(@Param("id") Long id);

    Long selectStorageSizeByUserId(@Param("userId") Long userId);


}
