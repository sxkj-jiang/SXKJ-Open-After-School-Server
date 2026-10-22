package com.sxkj.edu.module.system.role.roleemployee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleQueryDTO;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserDataDTO;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface RoleUserDao extends BaseMapper<RoleUserEntity> {

    /**
     * 判断用户是否为管理员用户
     *
     * @param userId
     * @return
     */
    Integer checkUserIsAdmin(@Param("userId") Long userId);

    /**
     * 根据员工id 查询所有的角色
     *
     * @param userId
     * @return
     */
    List<Long> selectRoleIdByEmployeeId(@Param("userId") Long userId);

    /**
     * @param page
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeByNamePage(Page page, @Param("queryDTO") RoleQueryDTO queryDTO);

    /**
     * 根据角色查询用户
     * @param roleId
     * @return
     */
    List<EmployeeDTO> selectEmployeeByRoleId(@Param("roleId") Long roleId);

    /**
     * 通过角色ID查询员工(权限)
     *
     * @param roleId
     * @return
     */
    List<EmployeeDTO> selectEmployeeByRoleIdOnPrivileges(@Param("roleId") Long roleId);


    int countByRoleName(@Param("roleName") String roleName);

    List<RoleUserDataDTO> queryAllUserRoleList();

    // 根据用户名或手机号查询角色
    List<String> checkUserIsAdminByLoginName(@Param("loginName") String loginName, @Param("type") Integer type);


    List<String> checkRoleByLoginNameAndRoleCode(@Param("userId") Long userId, @Param("schoolId") Long schoolId, @Param("roleCode") String roleCode);


}
