package com.sxkj.edu.module.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.wp.QueryFileLabelPageAo;
import com.sxkj.edu.domain.vo.wp.QueryFileLabelCountVO;
import com.sxkj.edu.domain.vo.wp.QueryFileLabelDetailPageVO;
import com.sxkj.edu.module.business.entity.wp.WpFileLabelPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WpFileLabelDao extends BaseMapper<WpFileLabelPo> {

    Page<QueryFileLabelDetailPageVO> selectFileLabelPage(Page<QueryFileLabelDetailPageVO> page, @Param("query") QueryFileLabelPageAo query);

    List<QueryFileLabelCountVO> selectFileLabelCountNum(@Param("userId") Long userId, @Param("schoolId") Long schoolId);

    List<WpFileLabelPo> selectAllFileLabel(@Param("userId") Long userId, @Param("schoolId") Long schoolId);
}
