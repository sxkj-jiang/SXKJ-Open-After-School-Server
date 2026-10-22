package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.ExamRoomEntity;
import com.sxkj.edu.module.afterschool.domain.vo.ExamRoomVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考场信息 Mapper 接口
 *
 * @author system
 * @date 2026-05-25
 */
public interface ExamRoomMapper extends BaseMapper<ExamRoomEntity> {

    /**
     * 分页查询考场列表
     */
    Page<ExamRoomVO> pageExamRoomList(Page<ExamRoomVO> page, @Param("query") ExamRoomQueryAO query);

    /**
     * 查询考场详情
     */
    ExamRoomVO selectExamRoomDetail(@Param("id") Long id);

}
