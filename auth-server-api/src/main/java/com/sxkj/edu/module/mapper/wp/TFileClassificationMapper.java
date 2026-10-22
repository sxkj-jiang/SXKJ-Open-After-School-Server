package com.sxkj.edu.module.mapper.wp;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.business.entity.wp.WpFileClassificationPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TFileClassificationMapper extends BaseMapper<WpFileClassificationPo> {



}
