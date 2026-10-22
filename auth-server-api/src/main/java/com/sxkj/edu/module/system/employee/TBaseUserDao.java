package com.sxkj.edu.module.system.employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.system.employee.domain.ao.UserQueryAO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 用户dao接口
 *
 * @author jiangjj
 * @date 2017年12月19日下午1:36:30
 */
@Mapper
@Component
public interface TBaseUserDao extends BaseMapper<TBaseUserEntity> {

    /**
     * 查询员工列表
     *
     * @param page
     * @param userQueryAo
     * @return
     */
    List<EmployeeDTO> selectEmployeeList(Page page, @Param("userQueryAo") UserQueryAO userQueryAo, @Param("roleId") Long roleId);

    /**
     * 不带分页查询员工列表
     *
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeAllList(@Param("userQueryAo") UserQueryAO queryDTO);


    /**
     * 登录
     *
     * @param loginName
     * @param loginPwd
     * @return
     */
    List<EmployeeDTO> userLoginMethod(@Param("loginName") String loginName, @Param("loginPwd") String loginPwd, @Param("schoolId") Long schoolId);

    /**
     * 通过登录名查询
     *
     * @param loginName
     * @param isValid
     * @return
     */
    EmployeeDTO getByLoginName(@Param("loginName") String loginName, @Param("isValid") Integer isValid);

    /**
     * 通过手机号登录
     *
     * @param phone
     * @param isValid
     * @return
     */
    EmployeeDTO getUserLoginByPhone(@Param("phone") String phone, @Param("isValid") Integer isValid, @Param("schoolId") Long schoolId);


    /**
     * 获取某个部门员工数
     *
     * @param depId
     * @return
     */
    Integer countByDepartmentId(@Param("depId") Long depId, @Param("isValid") Boolean isValid);

    /**
     * 获取一批员工
     *
     * @param userIds
     * @return
     */
    List<EmployeeDTO> getEmployeeByIds(@Param("ids") Collection<Long> userIds);


    EmployeeDTO getEmployeeById(@Param("id") Long userId);

    /**
     * 获取某个部门的员工
     *
     * @param departmentId
     * @return
     */
    List<EmployeeVO> getEmployeeIdByDeptId(@Param("departmentId") Long departmentId);

    /**
     * 获取某批部门的员工
     *
     * @param departmentIds
     * @return
     */
    List<EmployeeDTO> getEmployeeIdByDeptIds(@Param("departmentIds") List<Long> departmentIds);


    /**
     * 员工重置密码
     *
     * @param userId
     * @param password
     * @return
     */
    Integer updatePassword(@Param("employeeId") Integer userId, @Param("password") String password, @Param("updateId") Long updateId);


    /**
     * 查询所有员工
     *
     * @return
     */
//    List<EmployeeVO> selectAll();

    List<RoleSelectedVO> selectByEmployee(@Param("id") Long id);

    // 查询多账号的用户角色
    List<RoleSelectedVO> selectByAllUserRole(@Param("loginName") String loginName,@Param("schoolId") Long schoolId,
                                             @Param("loginType") String loginType);

    // 根据员工ID、角色id查询角色
    List<RoleSelectedVO> selectByEmployeeByUserIdAndRoleId(@Param("id") Long id, @Param("roleId") Long roleId);

    List<TBaseUserEntity> selectUserListByRole(@Param("roleCode") String roleCode);

    Page<TBaseUserEntity> queryUserPage(Page<TBaseUserEntity> page, @Param("roleId") Long roleId, @Param("userName") String userName);

    // 根据姓名+手机号+学校查询用户
    EmployeeDTO getParentUserLoginByPhoneAndUser(@Param("phone") String phone, @Param("userName") String userName, @Param("schoolId") Long schoolId,@Param("userType") Integer userType);

    // 根据教师id查询用户类型
    Integer getUserTypeByTeacherId(@Param("teacherId") Long teacherId);
}