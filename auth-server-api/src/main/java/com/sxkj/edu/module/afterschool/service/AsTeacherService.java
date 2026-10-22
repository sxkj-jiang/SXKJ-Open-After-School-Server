package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.ao.AsTeacherStudentQueryAO;
import com.sxkj.edu.domain.vo.AsTeacherCourseStudentVO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherListQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherPersonEditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AddTeacherRegisterAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAuditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.TeacherRegisterAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherAuditSummaryVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherByStudentIdVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherPersonDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 教师档案 Service 接口
 *
 * @author system
 * @date 2026-05-19
 */
public interface AsTeacherService extends IService<AsTeacherEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<AsTeacherVO> queryAsTeacherPage(AsTeacherQueryAO qo);

    /**
     * 编辑（含唯一校验：姓名+手机号）
     */
    JsonResult<String> updateAsTeacher(AsTeacherAddAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteAsTeacher(Long id);

    /**
     * 导入
     */
    JsonResult<String> importAsTeacher(Long schoolId, MultipartFile file);

    /**
     * 导出
     */
    void exportAsTeacher(AsTeacherQueryAO qo, HttpServletResponse response);

    /**
     * 授课教师注册
     */
    JsonResult<String> registerTeacher(TeacherRegisterAO ao);


    /**
     * 查询教师列表
     * @return
     */
    List<AsTeacherVO> queryList(AsTeacherListQueryAO qo);


    /**
     * 通过教师用户id查询教师id
     * @param teacherPhone
     * @return
     */
    Long queryTeacherIdByPhone(String teacherPhone,Long schoolId);

    /**
     * 根据学校ID和教师ID查询教师详情（含头像）
     */
    AsTeacherVO getTeacherDetail(Long schoolId, Long teacherId);

    /**
     * 根据授课教师ID查询课程班名称集合
     */
    List<String> queryCourseClassNamesByTeacherId(Long teacherId);

    /**
     * 教师审核
     * 校验教师与t_base_user关联，更新教师表registerStatus和t_base_user审批状态
     */
    JsonResult<String> auditTeacher(AsTeacherAuditAO ao);

    /**
     * 教师个人信息详情查询
     *
     * @param schoolId 学校ID
     * @param userId   当前用户ID
     * @return 教师个人信息详情
     */
    AsTeacherPersonDetailVO queryTeacherPersonDetail(Long schoolId, Long userId);

    /**
     * 教师个人信息编辑
     *
     * @param ao 编辑入参
     * @return 操作结果
     */
    JsonResult<String> updateTeacherPerson(AsTeacherPersonEditAO ao);

    /**
     * 查询教师审核状态汇总（按 register_status 分组统计）
     *
     * @param schoolId 学校ID
     * @return 审核状态汇总
     */
    AsTeacherAuditSummaryVO queryAuditSummary(Long schoolId);

    /**
     * 通过教师用户id查询教师
     * @param teacherPhone
     * @return
     */
    AsTeacherEntity queryTeacherIdByPhoneForEntity(String teacherPhone,Long schoolId);

    // 教师查询任课学生
    List<AsTeacherCourseStudentVO> listTeacherCourseStudents(AsTeacherStudentQueryAO ao);
}
