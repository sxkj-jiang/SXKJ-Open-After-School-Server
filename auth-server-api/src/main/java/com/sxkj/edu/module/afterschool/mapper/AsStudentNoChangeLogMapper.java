package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentNoChangeLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 学生学号纠错审计持久层。
 */
@Mapper
@Component
public interface AsStudentNoChangeLogMapper extends BaseMapper<AsStudentNoChangeLogEntity> {
}
