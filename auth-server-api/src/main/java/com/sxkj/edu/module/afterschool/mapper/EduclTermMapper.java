package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.vo.EduclTermVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 学期/校历Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
@Mapper
@Component
public interface EduclTermMapper extends BaseMapper<EduclTermEntity> {

    /**
     * 锁定目标学期，串行处理同一学期的复制冲突校验
     */
    EduclTermEntity selectByIdForUpdate(@Param("id") Long id);

    /**
     * 学期分页关联查询（含学校名称）
     */
    Page<EduclTermVO> pageList(Page<EduclTermVO> page, @Param("query") EduclTermQueryAO query);

    /**
     * 学期详情关联查询（含学校名称）
     */
    EduclTermVO selectDetail(@Param("id") Long id);
}
