package com.sxkj.edu.module.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.domain.ao.QueryChirdDictTypeListAo;
import com.sxkj.edu.domain.base.DictItemVo;
import com.sxkj.edu.domain.sys.entity.TBaseDictItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseDictItemMapper extends BaseMapper<TBaseDictItemEntity> {

    List<DictItemVo> queryBaseDictParentByList(@Param("query") QueryChirdDictTypeListAo query);

    List<DictItemVo> queryBaseDictParentByCode(@Param("typeCode") String typeCode);

}
