package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseCategoryEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsCourseCategoryMapper extends BaseMapper<AsCourseCategoryEntity> {

    Page<AsCourseCategoryVO> pageList(Page<AsCourseCategoryVO> page, @Param("query") AsCourseCategoryQueryAO query);

    AsCourseCategoryVO selectDetail(@Param("id") Long id);

    List<AsCourseCategoryVO> selectDropList(@Param("query") AsCourseCategoryListQueryAO query);

    List<AsCourseCategoryVO> selectCategoryTree(@Param("query") AsCourseCategoryQueryAO query);
}
