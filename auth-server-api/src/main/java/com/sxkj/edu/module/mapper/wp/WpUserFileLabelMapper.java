package com.sxkj.edu.module.mapper.wp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.business.entity.wp.WpUserFileLabelPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WpUserFileLabelMapper extends BaseMapper<WpUserFileLabelPo> {
}
