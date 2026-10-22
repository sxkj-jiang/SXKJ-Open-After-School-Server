package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.module.afterschool.domain.dto.AsHeadTeacherStudentDestinationPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentDestinationEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentDestinationStatusVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherStudentDestinationTermVO;

import java.util.List;

public interface AsStudentDestinationService extends IService<AsStudentDestinationEntity> {

    // 根据学校课次+学生ID查询学生考勤状态
    AsStudentDestinationStatusVO getBySchoolClassAndStudentIdByStatus(String schoolClass, String studentId);

    // 查询学生最新去向
    AsStudentDestinationEntity getLatestDestinationByStudentId(Long studentId);


    /**
     * 班主任查询自然班级学生去向
     * 以学期名称为一级维度、自然班级名称为二级维度返回
     *
     * @param queryAO 查询参数
     * @return 按学期+班级分组的列表
     */
    List<AsTeacherStudentDestinationTermVO> listHeadTeacherStudentDestination(AsHeadTeacherStudentDestinationPageQueryAO queryAO);

}
