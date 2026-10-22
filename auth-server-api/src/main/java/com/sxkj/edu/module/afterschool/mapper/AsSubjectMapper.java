package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsSubjectMapper extends BaseMapper<AsSubjectEntity> {

    /**
     * 分页查询学科列表
     */
    Page<AsSubjectVO> pageList(Page<AsSubjectVO> page, @Param("query") AsSubjectQueryAO qo);

    /**
     * 按年级分组查询启用学科（用于树形下拉）
     */
    List<AsSubjectVO> listEnabledGroupByGrade(@Param("schoolId") Long schoolId);
}
