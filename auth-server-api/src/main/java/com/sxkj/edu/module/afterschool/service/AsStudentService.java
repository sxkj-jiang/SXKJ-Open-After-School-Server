package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentNoByGradeClassAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentQueryListAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 学生档案 Service 接口
 *
 * @author system
 * @date 2026-05-19
 */
public interface AsStudentService extends IService<AsStudentEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<AsStudentVO> queryAsStudentPage(AsStudentQueryAO qo);

    /**
     * 编辑（唯一校验：学校+学期+学号）
     */
    JsonResult<String> updateAsStudent(AsStudentAddAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteAsStudent(Long id);

    /**
     * 导入
     */
    JsonResult<String> importAsStudentData(MultipartFile file, Long schoolId);

    /**
     * 导出
     */
    void exportAsStudent(AsStudentQueryAO qo, HttpServletResponse response);


    /**
     * 在指定业务学期保存或更新家长注册学生。
     */
    AsStudentEntity saveOrUpdateStudent(ParentRegisterAO ao, Long termId);

    /**
     * 根据学校ID和学生ID查询学生的出勤记录、报名记录、请假记录、调课记录
     */
    AsStudentMergedRecordVO queryStudentMergedRecord(Long schoolId, Long studentId);

    /**
     * 根据学校ID和学生ID查询学生详情
     */
    AsStudentVO getStudentDetail(Long schoolId, Long studentId);

    // 获取学生id
    Long getStudentId(Long schoolId, String studentName, String studentNo);

    /**
     * 学生家长审核
     * 校验t_as_guardian_student、t_as_student、t_base_user关联，
     * 更新学生表registerStatus和t_base_user审批状态
     */
    JsonResult<String> auditStudentParent(AsStudentAuditAO ao);

    // 学生审核状态汇总
    AsTeacherAuditSummaryVO queryStudentAuditSummary(Long schoolId, Long termId);

    PageResultDTO<AsStudentLearningAchievementVO> pageLearningAchievement(AsStudentLearningAchievementQueryAO qo);

    void exportLearningAchievement(AsStudentLearningAchievementQueryAO qo, HttpServletResponse response);


    /**
     * 在指定业务学期校验家长注册学生状态。
     */
    AsStudentEntity registerParentStudentCheckExist(
            Long schoolId, String studentName, String studentNo, Long termId);

    /**
     * 根据条件查询学生列表
     */
    List<AsStudentVO> queryAsStudentList(AsStudentQueryListAO qo);


    // 根据学期id和学生身份id获取学生id
    Long getStuIdByTermAndIdentifyId( Long termId,Long schoolId, Long studentIdentityId);



    // 注册时，根据年级和班级、关键字查询学生编号
    List<AsQueryStudentNoByGradeClassVO> queryStudentNoByGradeClass(AsStudentNoByGradeClassAO qo);

}
