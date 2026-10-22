package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsScheduleTemplateEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsScheduleTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AsScheduleTemplateMapper extends BaseMapper<AsScheduleTemplateEntity> {

    /**
     * 分页查询作息管理方案
     */
    Page<AsScheduleTemplateVO> pageList(Page<AsScheduleTemplateVO> page, @Param("query") AsScheduleTemplateQueryAO query);

}
