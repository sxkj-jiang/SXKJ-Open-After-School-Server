package com.sxkj.edu.module.system.privilege.service;

import com.google.common.collect.Lists;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.sxkj.edu.common.utils.SmartStringUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import com.sxkj.edu.module.system.privilege.domain.entity.UserMenuEntity;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserDao;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserService;
import com.sxkj.edu.module.system.role.roleprivilege.RolePrivilegeDao;
import com.sxkj.edu.service.CacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


/**
 * @Description : 后台员工权限缓存方法
 * @Author : JIANGJJ
 * @Date : 2022-9-7 11:33
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Service
@Slf4j
public class PrivilegeEmployeeService {

    /**
     * 后台用户权限缓存 <id,<controllerName,methodName></>></>
     */
    private ConcurrentMap<Long, Map<String, List<String>>> employeePrivileges = new ConcurrentLinkedHashMap.Builder<Long, Map<String, List<String>>>().maximumWeightedCapacity(1000).build();
    private ConcurrentMap<Long, List<UserMenuEntity>> employeePrivilegeListMap = new ConcurrentLinkedHashMap.Builder<Long, List<UserMenuEntity>>().maximumWeightedCapacity(1000).build();


    @Autowired
    private RoleUserDao roleEmployeeDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    @Resource
    private UserMenuService userMenuService;

    @Resource
    private RoleUserService roleUserService;

    @Resource
    private CacheServiceImpl cacheService;

    /**
     * 移除某人缓存中的权限
     *
     * @param employeeId
     */
    public void removeCache(Long employeeId) {
        this.employeePrivileges.remove(employeeId);
    }

    /**
     * 检查某人是否有访问某个方法的权限
     *
     * @param requestTokenBO
     * @param controllerName
     * @param methodName
     * @return
     */
    public Boolean checkEmployeeHavePrivilege(RequestTokenBO requestTokenBO, String controllerName, String methodName) {
        if (StringUtils.isEmpty(controllerName) || StringUtils.isEmpty(methodName)) {
            return false;
        }
        Boolean isSuperman = requestTokenBO.getEmployeeBO().getIsSuperman();
        if (isSuperman) {
            return true;
        }
        Map<String, List<String>> privileges = this.getPrivileges(requestTokenBO.getRequestUserId());
        List<String> urlList = privileges.get(controllerName.toLowerCase());
        if (CollectionUtils.isEmpty(urlList)) {
            return false;
        }
        return urlList.contains(methodName);
    }

    public List<UserMenuEntity> getEmployeeAllPrivilege(Long employeeId) {
        return employeePrivilegeListMap.get(employeeId);
    }

    /**
     * 判断是否为管理员
     *
     * @param employeeId
     * @return
     */
    public Boolean isSuperman(Long employeeId) {
        Boolean isAdmin = roleUserService.checkUserIsAdmin(employeeId);
        return isAdmin;
//        SystemConfigDTO systemConfig = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
//        if (systemConfig == null) {
//            throw new BaseBusinessException("缺少系统配置项[" + SystemConfigEnum.Key.EMPLOYEE_SUPERMAN.name() + "]");
//        }
//
//        List<Long> superManIdsList = SmartStringUtil.splitConverToLongList(systemConfig.getConfigValue(), ",");
//        return superManIdsList.contains(employeeId);
    }

    /**
     * 判断用户账号是否为超管
     *
     * @param loginName
     * @return
     */
    public Boolean isSupermanByLoginName(String loginName) {
        Boolean isAdmin = roleUserService.checkUserIsAdminByLoginName(loginName, 1);
        return isAdmin;
    }

    /**
     * 判断用户账号是否为超管
     *
     * @param loginPhone
     * @return
     */
    public Boolean isSupermanByLoginPhone(String loginPhone) {
        Boolean isAdmin = roleUserService.checkUserIsAdminByLoginName(loginPhone, 2);
        return isAdmin;
    }

    /**
     * 根据员工ID 获取 权限信息
     *
     * @param employeeId
     * @return
     */
    public List<UserMenuEntity> getPrivilegesByEmployeeId(Long employeeId,List<Long> roleList) {
        List<UserMenuEntity> privilegeEntities = null;
        // 超管用户
        Boolean isSuperman = this.isSuperman(employeeId);
        if (isSuperman) {
            privilegeEntities = userMenuService.getPrivilegeListByRole();
        } else {
            privilegeEntities = loadPrivilegeFromDb(employeeId,roleList);
        }

        if (privilegeEntities == null) {
            privilegeEntities = Lists.newArrayList();
        }

        this.updateCachePrivilege(employeeId, privilegeEntities);
        return privilegeEntities;
    }

    /**
     * 获取某人所能访问的方法
     *
     * @param employeeId
     * @return
     */
    private Map<String, List<String>> getPrivileges(Long employeeId) {
        Map<String, List<String>> privileges = employeePrivileges.get(employeeId);
        if (privileges != null) {
            return privileges;
        }
        List<UserMenuEntity> privilegeEntities = this.loadPrivilegeFromDb(employeeId,null);
        return updateCachePrivilege(employeeId, privilegeEntities);
    }

    private List<UserMenuEntity> loadPrivilegeFromDb(Long employeeId,List<Long> roleList) {
        List<Long> roleIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(roleIdList)){
            roleIdList = roleList;
        }else{
            roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        }
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        List<UserMenuEntity> privilegeEntities = rolePrivilegeDao.listByRoleIds(roleIdList);
        if (privilegeEntities != null) {
            return privilegeEntities;
        }
        return Collections.emptyList();
    }

    public Map<String, List<String>> updateCachePrivilege(Long employeeId, List<UserMenuEntity> privilegeEntities) {
        employeePrivilegeListMap.put(employeeId, privilegeEntities);
        List<String> privilegeList = new ArrayList<>();
        Map<String, List<String>> privilegeMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(privilegeEntities)) {
            List<List<String>> setList =
                    privilegeEntities.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.POINTS.getValue())).map(UserMenuEntity::getUrl).collect(Collectors.toList()).stream().map(e -> SmartStringUtil.splitConvertToList(e, ",")).collect(Collectors.toList());
            setList.forEach(privilegeList::addAll);
        }
        privilegeList.forEach(item -> {
            List<String> path = SmartStringUtil.splitConvertToList(item, "\\.");
            if (CollectionUtils.isNotEmpty(path)) {
                String controllerName = path.get(0).toLowerCase();
                String methodName = path.get(1);
                List<String> methodNameList = privilegeMap.get(controllerName);
                if (null == methodNameList) {
                    methodNameList = new ArrayList();
                }
                if (!methodNameList.contains(methodName)) {
                    methodNameList.add(methodName);
                }
                privilegeMap.put(controllerName, methodNameList);
            }
        });

        employeePrivileges.put(employeeId, privilegeMap);
        return privilegeMap;
    }

    public void updateOnlineEmployeePrivilegeByRoleId(Long roleId) {
        List<Long> roleList = new ArrayList<>();
        roleList.add(roleId);
        List<EmployeeDTO> roleEmployeeList = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        List<Long> employeeIdList = roleEmployeeList.stream().map(e -> e.getId()).collect(Collectors.toList());

        for (Long empId : employeePrivileges.keySet()) {
            if (employeeIdList.contains(empId)) {
                getPrivilegesByEmployeeId(empId,roleList);
            }
        }
        // 清空redis缓存
        String loginType = AssembleUtil.getUserAgentLoginType();
        if(CollectionUtils.isNotEmpty(roleEmployeeList)){
            for(EmployeeDTO user: roleEmployeeList){
                String loginName = user.getLoginName();
                cacheService.removeLoginUserCache(loginType, loginName);
            }

        }
    }

    /**
     * 查询指定用户的角色集合
     *
     * @param employeeId 用户ID
     * @return 角色集合
     */
    public Set<Long> queryRoleByEmployeeId(Long employeeId){
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        if(null != roleIdList && !roleIdList.isEmpty()){
            return new HashSet<>(roleIdList);
        }
        return new HashSet<>();
    }
}
