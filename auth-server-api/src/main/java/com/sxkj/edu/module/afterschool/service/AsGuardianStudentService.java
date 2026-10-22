package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.vo.khfw.QueryStudentGuardVO;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentPersonEditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AddParentRegisterAO;
import com.sxkj.edu.module.afterschool.domain.dto.ParentRegisterAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsStudentExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsParentPersonDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDataVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDetailVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentTaskVO;

import java.util.List;

public interface AsGuardianStudentService extends IService<AsGuardianStudentEntity> {

    Boolean createStudentGuardImport(Long schoolId, Long student, String studentName, AsStudentExcel excel,String phone);

    /**
     * 家长注册
     */
    JsonResult<String> registerParent(ParentRegisterAO ao);


    // 根据家长查询学生id，返回最新学期的学生信息
    Long queryStudentIdByParent(Long parentId,Long schoolId);


    // 根据家长查询学生信息,多学期时返回最新学期数据
    LoginStuParentDetailVO queryStudentByParent(Long parentId, Long schoolId);

    // 根据家长查询名下的所有学生信息,多学期时返回最新学期数据
    List<LoginStuParentDetailVO> queryAllStudentByParent(String parentPhone,Long parentId, Long schoolId);


    // 根据学生查询学生家长信息
    LoginStuParentDataVO queryParentByStudentId(Long studentId, Long schoolId);

    // 根据学生更新家长信息
    Boolean updateParentInfoByStudent(Long schoolId,Long guardianId, Long studentId, String relationName, String phone, String parentName);


    /**
     * 查询家长指定稳定学生身份的个人信息详情。
     *
     * @param schoolId         学校ID
     * @param userId           当前家长用户ID
     * @param studentIdentityId 学生稳定身份ID，单孩子时可为空
     * @return 家长个人信息详情
     */
    AsParentPersonDetailVO queryParentPersonDetail(Long schoolId,
                                                   Long userId,
                                                   Long studentIdentityId);

    /**
     * 家长个人信息编辑
     *
     * @param ao 编辑入参
     * @return 操作结果
     */
    JsonResult<String> updateParentPerson(AsParentPersonEditAO ao);



}
