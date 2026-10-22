package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentIdentityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 学生稳定身份持久层。
 */
@Mapper
@Component
public interface AsStudentIdentityMapper extends BaseMapper<AsStudentIdentityEntity> {
}
