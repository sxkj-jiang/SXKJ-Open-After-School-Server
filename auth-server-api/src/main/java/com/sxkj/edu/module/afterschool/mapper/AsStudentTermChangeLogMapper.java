package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentTermChangeLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 学生学期内变更记录持久层。
 */
@Mapper
@Component
public interface AsStudentTermChangeLogMapper extends BaseMapper<AsStudentTermChangeLogEntity> {
}
