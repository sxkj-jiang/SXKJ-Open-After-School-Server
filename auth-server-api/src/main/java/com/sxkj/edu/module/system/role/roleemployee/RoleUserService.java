package com.sxkj.edu.module.system.role.roleemployee;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.SmartPageUtil;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.department.DepartmentDao;
import com.sxkj.edu.module.system.department.domain.entity.DepartmentEntity;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.module.system.role.basic.RoleDao;
import com.sxkj.edu.module.system.role.basic.RoleService;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleBatchDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleQueryDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserDataDTO;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserEntity;
import com.sxkj.edu.module.system.role.roleemployee.domain.UserRoleFormAo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理业务
 *
 * @author jiangjj
 * @date 2019/4/3
 */
@Service
@Slf4j
public class RoleUserService extends ServiceImpl<RoleUserDao, RoleUserEntity> {

    @Autowired
    private RoleUserDao roleUserDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Resource
    private RoleService roleService;


    /**
     * 通过角色id，分页获取成员用户列表
     *
     * @param queryDTO
     * @return
     */
    public JsonResult<PageResultDTO<EmployeeVO>> listEmployeeByName(RoleQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<EmployeeDTO> employeeDTOS = roleUserDao.selectEmployeeByNamePage(page, queryDTO);
        employeeDTOS.stream().filter(e -> e.getDepartmentId() != null).forEach(employeeDTO -> {
            if (ObjectUtil.isNotNull(employeeDTO.getDepartmentId())) {
                DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());
                if (ObjectUtil.isNotNull(departmentEntity)) {
                    employeeDTO.setDepartmentName(departmentEntity.getName());
                }
            }
        });
        PageResultDTO<EmployeeVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, employeeDTOS, EmployeeVO.class);
        return JsonResult.buildSuccess(pageResultDTO);
    }

    public JsonResult<List<EmployeeVO>> getAllEmployeeByRoleId(Long roleId) {
        List<EmployeeDTO> employeeDTOS = roleUserDao.selectEmployeeByRoleIdOnPrivileges(roleId);
        List<EmployeeVO> list = SmartBeanUtil.copyList(employeeDTOS, EmployeeVO.class);
        return JsonResult.buildSuccess(list);
    }

    public List<EmployeeVO> getUserByRoleId(Long roleId) {
        List<EmployeeDTO> employeeDTOS = roleUserDao.selectEmployeeByRoleIdOnPrivileges(roleId);
        List<EmployeeVO> list = SmartBeanUtil.copyList(employeeDTOS, EmployeeVO.class);
        return list;
    }

    /**
     * 判断用户是否为管理员
     *
     * @param userId
     * @return
     */
    public Boolean checkUserIsAdmin(Long userId) {
        Boolean check = false;
        Integer rower = roleUserDao.checkUserIsAdmin(userId);
        if (CheckParamUtil.checkIntegerStatusParam(rower)) {
            check = true;
        }
        return check;
    }

    /**
     * 判断用户角色是否为学校管理员、校友、员工，如果是则登录时需要根据学校id校验
     *
     * @param loginName
     * @param type      1-登录名，2-手机号
     * @return
     */
    public Boolean checkUserIsAdminByLoginName(String loginName, Integer type) {
        Boolean check = false;
        List<String> roleList = roleUserDao.checkUserIsAdminByLoginName(loginName, type);
        if (CollectionUtil.isNotEmpty(roleList)) {
            check = true;
        }
        return check;
    }

    /**
     * 移除用户角色
     *
     * @param employeeId
     * @param roleId
     * @return JsonResult<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> removeEmployeeRole(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return JsonResult.buildError(ResultCode.ERROR_PARAM.code(), ResultCode.getMapByCode(ResultCode.ERROR_PARAM.code()));
        }
        Boolean uptRow = deleteUserRoleByRoleId(employeeId, roleId);
        if(uptRow){
            return JsonResult.buildSuccess("移除用户角色成功");
        }else{
            return JsonResult.buildError("移除用户角色失败");
        }
    }

    /**
     * 逻辑删除用户角色
     *
     * @param roleId
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRoleByRoleId(Long userId, Long roleId) {
        LambdaUpdateWrapper<RoleUserEntity> wrapper = new LambdaUpdateWrapper<>();
        if(!CheckParamUtil.checkLongIdParam(roleId) || !CheckParamUtil.checkLongIdParam(userId)){
            return false;
        }
        wrapper.eq(RoleUserEntity::getRoleId, roleId);
        wrapper.eq(RoleUserEntity::getUserId, userId);
        wrapper.eq(RoleUserEntity::getIsValid, 1);
        wrapper.set(RoleUserEntity::getIsValid, 0);
        wrapper.set(RoleUserEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RoleUserEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deleteUserRoleByRoleId userId:{}, roleId:{},result:{}", userId, roleId, uptRoleRes);
        return uptRoleRes;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRoleByRoleId(Long userId, List<Long> roleIdList) {
        LambdaUpdateWrapper<RoleUserEntity> wrapper = new LambdaUpdateWrapper<>();
        if(CollectionUtil.isEmpty(roleIdList) || !CheckParamUtil.checkLongIdParam(userId)){
            return false;
        }
        wrapper.in(RoleUserEntity::getRoleId, roleIdList);
        wrapper.eq(RoleUserEntity::getUserId, userId);
        wrapper.eq(RoleUserEntity::getIsValid, 1);
        wrapper.set(RoleUserEntity::getIsValid, 0);
        wrapper.set(RoleUserEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RoleUserEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deleteUserRoleByRoleId userId:{}, roleId:{},result:{}", userId, roleIdList, uptRoleRes);
        return uptRoleRes;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRoleByUserListAndRoleId(List<Long> userIdList, Long roleId) {
        LambdaUpdateWrapper<RoleUserEntity> wrapper = new LambdaUpdateWrapper<>();
        if(!CheckParamUtil.checkLongIdParam(roleId) || CollectionUtil.isEmpty(userIdList)){
            return false;
        }
        wrapper.in(RoleUserEntity::getUserId, userIdList);
        wrapper.eq(RoleUserEntity::getRoleId, roleId);
        wrapper.eq(RoleUserEntity::getIsValid, 1);
        wrapper.set(RoleUserEntity::getIsValid, 0);
        wrapper.set(RoleUserEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RoleUserEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deleteUserRoleByRoleId userIdList:{}, roleId:{},result:{}", userIdList, roleId, uptRoleRes);
        return uptRoleRes;
    }

    /**
     * 逻辑删除用户角色
     *
     * @param roleId
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRoleByRoleIdAndUserList(List<Long> userIdList, Long roleId) {
        LambdaUpdateWrapper<RoleUserEntity> wrapper = new LambdaUpdateWrapper<>();
        if(!CheckParamUtil.checkLongIdParam(roleId) || CollectionUtil.isEmpty(userIdList)){
            return false;
        }
        wrapper.in(RoleUserEntity::getUserId, userIdList);
        wrapper.eq(RoleUserEntity::getRoleId, roleId);
        wrapper.eq(RoleUserEntity::getIsValid, 1);
        wrapper.set(RoleUserEntity::getIsValid, 0);
        wrapper.set(RoleUserEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RoleUserEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deleteUserRoleByRoleIdAndUserList userIdList:{}, roleId:{},result:{}", userIdList, roleId, uptRoleRes);
        return uptRoleRes;
    }

    /**
     * 批量删除角色的成员用户
     *
     * @param removeDTO
     * @return JsonResult<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> batchRemoveEmployeeRole(RoleBatchDTO removeDTO) {
        List<Long> employeeIdList = removeDTO.getUserIds();
        Boolean uptRow = deleteUserRoleByRoleIdAndUserList(employeeIdList, removeDTO.getRoleId());
        if(uptRow){
            return JsonResult.buildSuccess("移除用户角色成功");
        }else{
            return JsonResult.buildError("移除用户角色失败");
        }
    }

    /**
     * 批量添加角色的成员用户
     *
     * @param addDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> batchAddEmployeeRole(RoleBatchDTO addDTO) {
        Long roleId = addDTO.getRoleId();
        List<Long> employeeIdList = addDTO.getUserIds();
        Boolean uptRowRem = deleteUserRoleByUserListAndRoleId(employeeIdList, roleId);
        List<RoleUserEntity> roleRelationEntities = Lists.newArrayList();
        RoleUserEntity employeeRoleRelationEntity;
        for (Long employeeId : employeeIdList) {
            employeeRoleRelationEntity = new RoleUserEntity();
            employeeRoleRelationEntity.setRoleId(roleId);
            employeeRoleRelationEntity.setUserId(employeeId);
            employeeRoleRelationEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            employeeRoleRelationEntity.setCreateTime(new Date());
            employeeRoleRelationEntity.setIsValid(1);
            roleRelationEntities.add(employeeRoleRelationEntity);
        }
        Boolean addRow = this.saveBatch(roleRelationEntities);
        if(uptRowRem && addRow){
            return JsonResult.buildSuccess("添加用户角色成功");
        }
        return JsonResult.buildError("添加用户角色失败");
    }


    /**
     * 通过用户id获取用户角色
     *
     * @param employeeId
     * @return
     */
    public JsonResult<List<RoleSelectedVO>> getRolesByEmployeeId(Long employeeId) {
        List<Long> roleIds = roleUserDao.selectRoleIdByEmployeeId(employeeId);
        List<RoleEntity> roleList = roleDao.selectList(null);
        List<RoleSelectedVO> result = SmartBeanUtil.copyList(roleList, RoleSelectedVO.class);
        result.stream().forEach(item -> item.setSelected(roleIds.contains(item.getId())));
        return JsonResult.buildSuccess(result);
    }

    /**
     * 通过用户id、角色代码获取用户角色是否已存在
     *
     * @param employeeId
     * @return
     */
    public Boolean checkRoleByEmployeeIdAndRoleCode(Long employeeId,Long schoolId, String roleCode) {
        List<String> roleIds = roleUserDao.checkRoleByLoginNameAndRoleCode(employeeId,schoolId, roleCode);
        if(CollectionUtil.isNotEmpty(roleIds)) return true;
        return false;
    }

    // 暂定一个家长最多2个账号
    public Boolean checkParentRoleByUserIdAndRoleCode(Long employeeId,Long schoolId, String roleCode) {
        List<String> roleIds = roleUserDao.checkRoleByLoginNameAndRoleCode(employeeId,schoolId, roleCode);
        if(CollectionUtil.isNotEmpty(roleIds) && roleIds.size() > 1) return true;
        return false;
    }


    /**
     * 通过用户id获取用户角色
     *
     * @param employeeId
     * @return
     */
    public Boolean getRolesByEmployeeIdForAdmin(Long employeeId) {
        Boolean checkAdmin = false;
        JsonResult<List<RoleSelectedVO>> jsonResult = getRolesByEmployeeId(employeeId);
        List<RoleSelectedVO> list = jsonResult.getData();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            for(RoleSelectedVO role: list){
                if(role.getSelected() && role.getCode().equals(RoleEnum.PORTAL_ROLE_ADMIN.getCode())){
                    checkAdmin = true;
                    return checkAdmin;
                }
            }
        }
        return checkAdmin;
    }

    /**
     * 获取用户所有的角色枚举列表
     * @param employeeId 用户id
     * @return 角色枚举列表
     */
    public List<RoleEnum> getRoleEnumsByEmployeeId(Long employeeId) {
        List<RoleEnum> roles = new ArrayList<>();

        JsonResult<List<RoleSelectedVO>> jsonResult = getRolesByEmployeeId(employeeId);
        List<RoleSelectedVO> list = jsonResult.getData();
        if(CollectionUtils.isNotEmpty(list)){
            for(RoleSelectedVO role: list){
                if (!role.getSelected()) {
                    continue;
                }

                if (role.getCode().equals(RoleEnum.PORTAL_ROLE_ADMIN.getCode())) {
                    roles.add(RoleEnum.PORTAL_ROLE_ADMIN);
                } else if (role.getCode().equals(RoleEnum.PORTAL_ROLE_SCHOOL_ADMIN.getCode())) {
                    roles.add(RoleEnum.PORTAL_ROLE_SCHOOL_ADMIN);
                }
            }
        }

        return roles;
    }

    /**
     * 获取用户所有的角色列表
     * @param userId 用户id
     * @return 角色枚举列表
     */
    public List<String> getRoleCodeListByUserId(Long userId) {
        List<String> roles = this.baseMapper.checkRoleByLoginNameAndRoleCode(userId,null,null);

        return roles;
    }

    /**
     * 初始化用户角色
     *
     * @param userIds
     * @param roleEnum
     */
    public void setInitUserRoleBatch(List<Long> userIds, RoleEnum roleEnum) {
        RoleEntity role = roleService.getRolePoByCode(roleEnum.getCode());
        if (ObjectUtil.isNotNull(role)) {
            Long roleId = role.getId();
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(roleId);
            for (Long userId : userIds) {
                UserRoleFormAo userRoleFormReq = new UserRoleFormAo();
                userRoleFormReq.setRoleIds(roleIds);
                userRoleFormReq.setUserId(userId);
                addOrUpdate(userRoleFormReq);
            }
        }
    }

    /**
     * 小程序中新增角色身份，不存在时保存
     *
     * @param userId
     * @param roleEnum
     */
    public void updateUserRoleBatchForApp(Long userId, RoleEnum roleEnum) {
        RoleEntity role = roleService.getRolePoByCode(roleEnum.getCode());
        if (ObjectUtil.isNotNull(role)) {
            Long roleId = role.getId();
            LambdaQueryWrapper<RoleUserEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleUserEntity::getUserId, userId);
            wrapper.eq(RoleUserEntity::getRoleId, roleEnum.getRoleId());
            wrapper.eq(RoleUserEntity::getIsValid, 1);
            List<RoleUserEntity> roleUserEntities = this.list(wrapper);
            if(CollectionUtil.isNotEmpty(roleUserEntities)){
                log.warn("call updateUserRoleBatchForApp in exists userId:{}, roleId:{}", userId, roleEnum.getRoleId());
            }else{
                RoleUserEntity userRole = new RoleUserEntity();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(new Date());
                userRole.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                userRole.setIsValid(1);
                Boolean saveRow = this.save(userRole);
                log.info("call updateUserRoleBatchForApp userId:{}, roleId:{}, saveRow:{}", userId, roleEnum.getRoleId(), saveRow);
            }
        }
    }

    public void setInitUserRoleBatch(List<Long> userIds, String roleCode) {
        RoleEntity role = roleService.getRolePoByCode(roleCode);
        if (ObjectUtil.isNotNull(role)) {
            Long roleId = role.getId();
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(roleId);
            for (Long userId : userIds) {
                UserRoleFormAo userRoleFormReq = new UserRoleFormAo();
                userRoleFormReq.setRoleIds(roleIds);
                userRoleFormReq.setUserId(userId);
                addOrUpdate(userRoleFormReq);
            }
        }
    }

    /**
     * 初始化用户角色
     *
     * @param userIds
     * @param roleList
     */
    public void setInitUserRoleListBatch(List<Long> userIds, List<Long> roleList) {
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (Long userId : userIds) {
                UserRoleFormAo userRoleFormReq = new UserRoleFormAo();
                userRoleFormReq.setRoleIds(roleList);
                userRoleFormReq.setUserId(userId);
                addOrUpdate(userRoleFormReq);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(UserRoleFormAo req) {
        log.info("call roleUserServ addOrUpdate req: {}", req);
        LambdaUpdateWrapper<RoleUserEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleUserEntity::getUserId, req.getUserId());
        wrapper.eq(RoleUserEntity::getIsValid, 1);
        wrapper.set(RoleUserEntity::getIsValid, 0);
        wrapper.set(RoleUserEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RoleUserEntity::getUpdateTime, new Date());
        this.update(wrapper);

        //重新写入新的关联
        List<RoleUserEntity> list = new ArrayList<>();
        req.getRoleIds().forEach(item -> {
            RoleUserEntity userRole = new RoleUserEntity();
            userRole.setUserId(req.getUserId());
            userRole.setRoleId(item);
            userRole.setCreateTime(new Date());
            userRole.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            userRole.setIsValid(1);
            list.add(userRole);
        });
        this.saveBatch(list);
    }


    public List<RoleUserDataDTO> getAllUserRoleName() {
        List<RoleUserDataDTO> roleUserDataDTOS = roleUserDao.queryAllUserRoleList();
        return roleUserDataDTOS;
    }
}
