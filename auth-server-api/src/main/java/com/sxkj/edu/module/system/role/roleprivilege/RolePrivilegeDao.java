package com.sxkj.edu.module.system.role.roleprivilege;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.system.privilege.domain.entity.UserMenuEntity;
import com.sxkj.edu.module.system.role.roleprivilege.domain.entity.RolePrivilegeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface RolePrivilegeDao extends BaseMapper<RolePrivilegeEntity> {


    /**
     * 查询某批角色的权限
     *
     * @param roleIds
     * @return
     */
    List<UserMenuEntity> listByRoleIds(@Param("roleIds") List<Long> roleIds);


    /**
     * 查询某个角色的菜单+功能权限
     *
     * @param roleId
     * @return
     */
    List<UserMenuEntity> listByMenuGnTypeRoleId(@Param("roleId") Long roleId);

    List<UserMenuEntity> listByMenuGnTypeRoleIdForPc(@Param("roleId") Long roleId);

    /**
     * 查询某个用户拥有的角色菜单
     *
     * @param userId
     * @return
     */
    List<String> queryUserMenuListByUserId(@Param("userId") Long userId);
}
