package com.sxkj.edu.module.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.wp.QueryShareFileAo;
import com.sxkj.edu.domain.vo.wp.QueryFilePageVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherResourcePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherResourcePageVO;
import com.sxkj.edu.module.business.entity.wp.WpOwnerFilePo;
import com.sxkj.edu.module.business.entity.wp.WpUserFilePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WpOwnerFileDao extends BaseMapper<WpOwnerFilePo> {

    /**
     * 分页查询教师课程资源
     *
     * @param page     分页参数
     * @param query    查询条件
     * @param userId   当前用户ID
     * @return 分页结果
     */
    Page<AsTeacherResourcePageVO> pageTeacherCourseResource(
            Page<Object> page,
            @Param("query") AsTeacherResourcePageQueryAO query,
            @Param("userId") Long userId
    );

}
