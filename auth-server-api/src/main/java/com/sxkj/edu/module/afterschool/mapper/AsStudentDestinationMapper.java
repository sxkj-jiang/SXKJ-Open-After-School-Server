package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentDestinationEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsHeadTeacherStudentDestinationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsStudentDestinationMapper extends BaseMapper<AsStudentDestinationEntity> {

    // 查询学生最新去向记录
    AsStudentDestinationEntity queryLatestStudentDestination(Long studentId);


    /**
     * 班主任查询自然班级学生去向（含每个学生最新去向）
     *
     * @param classIds 班级ID列表
     */
    List<AsHeadTeacherStudentDestinationVO> listHeadTeacherStudentDestination(
            @Param("classIds") List<Long> classIds);
}
