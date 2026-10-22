package com.sxkj.edu.module.mapper.website;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.mult.TEduclMajorNameCodeVo;
import com.sxkj.edu.domain.base.mult.TEduclMajorQueryAo;
import com.sxkj.edu.domain.base.mult.TEduclMajorVo;
import com.sxkj.edu.domain.sys.entity.TEduclMajorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EduclMajorMapper extends BaseMapper<TEduclMajorEntity> {
    
    Page<TEduclMajorVo> pageList(Page<TEduclMajorVo> page, @Param("query") TEduclMajorQueryAo query);
    
    TEduclMajorVo selectDetail(@Param("id") Integer id);

    List<TEduclMajorNameCodeVo> getAllTEduclMajorList(@Param("libraryId") Long id);
}