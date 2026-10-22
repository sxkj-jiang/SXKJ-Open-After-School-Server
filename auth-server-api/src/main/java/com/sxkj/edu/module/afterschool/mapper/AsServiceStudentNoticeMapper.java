package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceStudentNoticeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课后服务学生提醒去重记录数据访问接口。
 */
@Mapper
public interface AsServiceStudentNoticeMapper
        extends BaseMapper<AsServiceStudentNoticeEntity> {
}
