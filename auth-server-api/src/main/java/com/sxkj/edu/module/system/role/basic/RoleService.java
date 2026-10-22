package com.sxkj.edu.module.system.role.basic;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.Args;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.module.system.role.basic.domain.ao.QueryRolePageAo;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleAddDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RolePageVO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleUpdateDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleVO;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserService;
import com.sxkj.edu.module.system.role.roleprivilege.RolePrivilegeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色管理业务
 *
 * @author jiangjj
 * @date 2024/7/28 09:37
 */
@Slf4j
@Service
public class RoleService extends ServiceImpl<RoleDao, RoleEntity> {

    @Autowired
    private RoleDao roleDao;

    @Resource
    private RoleUserService userRoleService;

    @Resource
    private RolePrivilegeService rolePrivilegeService;

    /**
     * 新增添加角色
     *
     * @param roleAddDTO
     * @return JsonResult
     */
    public JsonResult addRole(RoleAddDTO roleAddDTO) {
        List<RoleEntity> employeeRoleEntityList = getRoleListCheckExist(roleAddDTO.getCode(), roleAddDTO.getRoleName());
        if (CollectionUtils.isNotEmpty(employeeRoleEntityList)) {
            return JsonResult.buildError(ResultCode.ROLE_NAME_EXISTS.code(), ResultCode.getMapByCode(ResultCode.ROLE_NAME_EXISTS.code()));
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleAddDTO, RoleEntity.class);
        roleEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        roleDao.insert(roleEntity);

        // 创建默认用户菜单

        return JsonResult.buildSuccess();
    }

    /**
     * 根据角色id 删除
     *
     * @param roleId
     * @return JsonResult
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult deleteRole(Long roleId) {
        JsonResult result = JsonResult.buildError("删除角色失败");
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return JsonResult.buildError(ResultCode.ROLE_NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.ROLE_NOT_EXISTS.code()));
        }
        // 查询当前角色下是否存在用户
        List<EmployeeVO> userRoleList = userRoleService.getUserByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            return JsonResult.buildError(ResultCode.DATA_EXIST.code(), "当前角色下存在使用的用户不能删除");
        }
        roleEntity.setIsValid(0);
        roleEntity.setStatus(0);
        roleEntity.setUpdateTime(new Date());
        roleEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        Integer rower = roleDao.updateById(roleEntity);

        rolePrivilegeService.deleteRolePrivilegeByRoleId(roleId, null);
        userRoleService.deleteUserRoleByRoleId(null, roleId);
        if(null != rower && rower > 0){
            result = JsonResult.buildSuccess("删除角色成功");
        }
        return result;
    }

    /**
     * 更新角色，更新时不做校验
     *
     * @param roleUpdateDTO
     * @return JsonResult
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateRole(RoleUpdateDTO roleUpdateDTO) {
        if (null == roleDao.selectById(roleUpdateDTO.getId())) {
            return JsonResult.buildError(ResultCode.ROLE_NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.ROLE_NOT_EXISTS.code()));
        }
//        List<RoleEntity> roleList = checkRoleExist(roleUpdateDTO.getRoleName(),roleUpdateDTO.getCode());
//        if (CollectionUtils.isNotEmpty(roleList)) {
//            return JsonResult.buildError(ResultCode.ROLE_NAME_EXISTS.code(),ResultCode.getMapByCode(ResultCode.ROLE_NAME_EXISTS.code()));
//        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleUpdateDTO, RoleEntity.class);
        roleEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        roleEntity.setUpdateTime(new Date());
        roleDao.updateById(roleEntity);
        return JsonResult.buildSuccess();
    }

    private List<RoleEntity> checkRoleExist(String roleName, String roleCode) {
        List<RoleEntity> list = new ArrayList<>();
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleEntity::getIsValid, 1);
        queryWrapper.and(wq -> wq
                .eq(RoleEntity::getRoleName, roleName)
                .or()
                .eq(RoleEntity::getCode, roleCode));
        list = this.list(queryWrapper);
        return list;
    }

    /**
     * 根据id获取角色数据
     *
     * @param roleId
     * @return JsonResult<RoleDTO>
     */
    public JsonResult<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return JsonResult.buildError(ResultCode.ROLE_NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.ROLE_NAME_EXISTS.code()));
        }
        RoleVO role = SmartBeanUtil.copy(roleEntity, RoleVO.class);
        return JsonResult.buildSuccess(role);
    }

    /**
     * 获取所有角色列表
     *
     * @return JsonResult
     */
    public JsonResult<List<RoleVO>> getAllRole() {
        LambdaQueryWrapper<RoleEntity> roleEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleEntityLambdaQueryWrapper.eq(RoleEntity::getIsValid, 1);
        roleEntityLambdaQueryWrapper.eq(RoleEntity::getStatus,1);
        List<RoleEntity> roleEntityList = roleDao.selectList(roleEntityLambdaQueryWrapper);
        List<RoleVO> roleList = SmartBeanUtil.copyList(roleEntityList, RoleVO.class);
        return JsonResult.buildSuccess(roleList);
    }

    // 只返回学生、老师、学校管理员
    public JsonResult<List<RoleVO>> getTaskRoleList() {
        LambdaQueryWrapper<RoleEntity> roleEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleEntityLambdaQueryWrapper.eq(RoleEntity::getIsValid, 1);
        roleEntityLambdaQueryWrapper.eq(RoleEntity::getStatus,1);
        roleEntityLambdaQueryWrapper.in(RoleEntity::getId, Arrays.asList(2,3,4));
        List<RoleEntity> roleEntityList = roleDao.selectList(roleEntityLambdaQueryWrapper);
        List<RoleVO> roleList = SmartBeanUtil.copyList(roleEntityList, RoleVO.class);
        return JsonResult.buildSuccess(roleList);
    }

    public JsonResult<Page<RolePageVO>> rolePage(Page<RolePageVO> page, QueryRolePageAo ao) {
        return JsonResult.buildSuccess(roleDao.rolePage(page, ao.getRoleName(),ao.getRoleIdList()));
    }

    /**
     * @param list
     * @return
     */
    public List<RoleEntity> getRoleList(List<Long> list) {
        List<RoleEntity> roleEntityList = new ArrayList<>();
        LambdaQueryWrapper<RoleEntity> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.in(RoleEntity::getId, list);
        roleWrapper.eq(RoleEntity::getStatus, 1);
        roleEntityList = this.list(roleWrapper);
        return roleEntityList;
    }

    public Long roleIdByCode(String code) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getCode, code);
        wrapper.eq(RoleEntity::getStatus, 1);
        RoleEntity role = getOne(wrapper);
        return ObjectUtil.isNotNull(role) ? role.getId() : null;
    }

    /**
     * 根据code查询角色
     *
     * @param roleCode
     * @return
     */
    public RoleEntity getRolePoByCode(String roleCode) {
        RoleEntity roleEntity = null;
        List<RoleEntity> roleEntityList = new ArrayList<>();
        LambdaQueryWrapper<RoleEntity> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(RoleEntity::getCode, roleCode);
        roleWrapper.eq(RoleEntity::getStatus, 1);
        roleEntityList = this.list(roleWrapper);
        if (CollectionUtils.isNotEmpty(roleEntityList)) {
            roleEntity = roleEntityList.get(0);
        }
        return roleEntity;
    }

    /**
     * 初始化菜单
     *
     * @return
     */
    public List<RoleEntity> getRoleListByRole(String roleCode) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getIsValid, 1);
        wrapper.eq(RoleEntity::getStatus, 1);
        wrapper.eq(RoleEntity::getCode, roleCode);
        List<RoleEntity> list = this.list(wrapper);
        return list;
    }


    /**
     * 查询是否存在
     *
     * @return
     */
    public List<RoleEntity> getRoleListCheckExist(String roleCode, String roleName) {
        List<RoleEntity> list = new ArrayList<>();
        if (StringUtils.isEmpty(roleCode) && StringUtils.isEmpty(roleName)) {
            return list;
        }
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Args.isNotEmpty(roleCode), RoleEntity::getCode, roleCode);
        wrapper.or().eq(Args.isNotEmpty(roleName), RoleEntity::getRoleName, roleName);
        wrapper.eq(RoleEntity::getIsValid, 1);
        wrapper.eq(RoleEntity::getStatus, 1);
        list = this.list(wrapper);
        return list;
    }

}
