package com.sxkj.edu.module.system.role.roleprivilege;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.LoginTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.Args;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.privilege.dao.UserMenuDao;
import com.sxkj.edu.module.system.privilege.domain.dto.UserMenuMetaDto;
import com.sxkj.edu.module.system.privilege.domain.entity.UserMenuEntity;
import com.sxkj.edu.module.system.privilege.service.PrivilegeEmployeeService;
import com.sxkj.edu.module.system.privilege.service.UserMenuService;
import com.sxkj.edu.module.system.role.basic.RoleDao;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import com.sxkj.edu.module.system.role.roleprivilege.domain.dto.*;
import com.sxkj.edu.module.system.role.roleprivilege.domain.entity.RolePrivilegeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 角色权限实现
 */
@Service
@Slf4j
public class RolePrivilegeService extends ServiceImpl<RolePrivilegeDao, RolePrivilegeEntity> {

    @Resource
    private UserMenuDao userMenuDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private RolePrivilegeDao rolePrivilegeDao;

    @Resource
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Resource
    private UserMenuService userMenuService;

    /**
     * 更新角色权限
     *
     * @param updateDTO
     * @return JsonResult
     */
    public JsonResult<String> updateRolePrivilege(RolePrivilegeDTO updateDTO) {
        JsonResult<String> jsonResult = JsonResult.buildSuccess("用户角色更新成功");
        log.info("call RolePrivilegeService updateRolePrivilege in request:{}",updateDTO);
        Long roleId = updateDTO.getRoleId();
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return JsonResult.buildError(ResultCode.ROLE_NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.ROLE_NOT_EXISTS.code()));
        }
        deleteRolePrivilegeByRoleId(roleId, updateDTO.getPrivilegeKeyList());
        List<RolePrivilegeEntity> rolePrivilegeList = Lists.newArrayList();
        String nodeKey = "";
        if(CollectionUtils.isNotEmpty(updateDTO.getPrivilegeKeyList()) && updateDTO.getPrivilegeKeyList().size() > 2){
            // 排除前2个前端固定菜单key
            nodeKey = updateDTO.getPrivilegeKeyList().get(2);
        }
        if(StringUtils.isBlank(nodeKey)) return JsonResult.buildError(ResultCode.ROLE_NOT_EXISTS.code(), "请选择角色菜单更新");

        // 查询上一级节点是否已存在，不存在即插入
        String parentKey = userMenuService.queryUserMenuByKeyByParentKey(nodeKey);
        if(StringUtils.isNotBlank(parentKey)){
            RolePrivilegeEntity parentData = queryParentRoleKeyInsert(parentKey,updateDTO.getRoleId());
            log.info("call updateRolePrivilege get parentData:{}",parentData);
            if(null != parentData){
                rolePrivilegeList.add(parentData);
            }
        }
        RolePrivilegeEntity rolePrivilegeEntity;
        List<String> privilList = CoreConstant.NOT_DELETE_ROLE_PRIKEY;
        for (String privilegeKey : updateDTO.getPrivilegeKeyList()) {
            rolePrivilegeEntity = new RolePrivilegeEntity();
            if(!privilList.contains(privilegeKey) && !StringUtils.equals(privilegeKey,parentKey)){
                rolePrivilegeEntity.setRoleId(roleId);
                rolePrivilegeEntity.setPrivilegeKey(privilegeKey);
                rolePrivilegeEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                rolePrivilegeEntity.setCreateTime(new Date());
                rolePrivilegeEntity.setIsValid(1);
                rolePrivilegeList.add(rolePrivilegeEntity);
            }
        }
        Boolean uptUserRoleRow = this.saveBatch(rolePrivilegeList);
        log.info("call updateRolePrivilege in exec,request:{}, result:{}",updateDTO,uptUserRoleRow);
        if(uptUserRoleRow == false){
            jsonResult = JsonResult.buildError("更新用户角色失败");
        }
        privilegeEmployeeService.updateOnlineEmployeePrivilegeByRoleId(roleId);
        return jsonResult;
    }

    public JsonResult<RolePrivilegeTreeVO> listPrivilegeByRoleId(Long roleId) {
        RolePrivilegeTreeVO rolePrivilegeTreeDTO = new RolePrivilegeTreeVO();
        rolePrivilegeTreeDTO.setRoleId(roleId);

        List<UserMenuEntity> privilegeDTOList = userMenuDao.selectAll();
        if (CollectionUtils.isEmpty(privilegeDTOList)) {
            rolePrivilegeTreeDTO.setPrivilege(Lists.newArrayList());
            rolePrivilegeTreeDTO.setSelectedKey(Lists.newArrayList());
            return JsonResult.buildSuccess(rolePrivilegeTreeDTO);
        }
        //构造权限树
        List<RolePrivilegeSimpleDTO> privilegeList = this.buildPrivilegeTree(privilegeDTOList);
        //设置选中状态
        List<UserMenuEntity> rolePrivilegeEntityList = rolePrivilegeDao.listByMenuGnTypeRoleId(roleId);
        List<String> privilegeKeyList = rolePrivilegeEntityList.stream().map(e -> e.getKey()).collect(Collectors.toList());
        rolePrivilegeTreeDTO.setPrivilege(privilegeList);
        rolePrivilegeTreeDTO.setSelectedKey(privilegeKeyList);
        return JsonResult.buildSuccess(rolePrivilegeTreeDTO);
    }


    /**
     * 查询有效的用户菜单，树结果返回-唯一入口
     * @param roleId
     * @param selectType: 1-菜单，为空查菜单和功能
     * @return
     */
    public List<UserRolePrivilegeVbenDto> queryUserPrivilegeByRoleId(Long roleId,Integer selectType) {
        List<UserRolePrivilegeVbenDto> resList = new ArrayList<>();
        RolePrivilegeTreeVO rolePrivilegeTreeDTO = new RolePrivilegeTreeVO();
        rolePrivilegeTreeDTO.setRoleId(roleId);

        //构造权限树
        List<UserMenuEntity> rolePrivilegeEntityList = userMenuService.queryUserMenuBusinessList(null);
        if(CollectionUtils.isEmpty(rolePrivilegeEntityList)) return resList;
        resList = this.buildLoginUserPrivilegeTree(rolePrivilegeEntityList,false);

        return resList;
    }

    public List<UserRolePrivilegeVbenDto> queryUserMenuTypeValidList(Integer isShow,Integer menuType) {
        List<UserRolePrivilegeVbenDto> resList = new ArrayList<>();

        //构造权限树
        List<UserMenuEntity> rolePrivilegeEntityList = userMenuService.queryUserMenuIsShowTypeList(isShow,menuType);
        if(CollectionUtils.isEmpty(rolePrivilegeEntityList)) return resList;
        resList = this.buildLoginUserPrivilegeTree(rolePrivilegeEntityList,false);

        return resList;
    }


    // 查询用户网页段的菜单
    public List<UserRolePrivilegeVbenDto> queryUserMenuLoginByRoleIdForPc(Long roleId,String loginType) {
        List<UserRolePrivilegeVbenDto> resList = new ArrayList<>();

        //构造权限树
        List<UserMenuEntity> rolePrivilegeEntityList = new ArrayList<>();
        if(StringUtils.equals(loginType, LoginTypeEnum.MANAGER_TYPE.getCode())){
            rolePrivilegeEntityList = rolePrivilegeDao.listByMenuGnTypeRoleIdForPc(roleId);
        }else{
            rolePrivilegeEntityList = rolePrivilegeDao.listByMenuGnTypeRoleId(roleId);
        }
        if(CollectionUtils.isEmpty(rolePrivilegeEntityList)) return resList;
        resList = this.buildLoginUserPrivilegeTree(rolePrivilegeEntityList,true);

        return resList;
    }


    // 保存Business、Workspace、dashboard
    public void createDefaultSysRolePrivilege(Long roleId) {
        log.info("call RolePrivilegeService createDefaultSysRolePrivilege in request:{}",roleId);
        createDefaultSysRolePrivilegeConfig(roleId,"Business");
        createDefaultSysRolePrivilegeConfig(roleId,"Workspace");
        createDefaultSysRolePrivilegeConfig(roleId,"Dashboard");


    }

    public void createDefaultSysRolePrivilegeConfig(Long roleId,String key) {
        LambdaQueryWrapper<RolePrivilegeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePrivilegeEntity::getRoleId, roleId);
        queryWrapper.eq(RolePrivilegeEntity::getIsValid, 1);
        queryWrapper.eq(RolePrivilegeEntity::getPrivilegeKey, key);
        List<RolePrivilegeEntity> rolePrivilegeEntities = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(rolePrivilegeEntities)) {
            RolePrivilegeEntity rolePrivilegeEntity = new RolePrivilegeEntity();
            rolePrivilegeEntity.setRoleId(roleId);
            rolePrivilegeEntity.setPrivilegeKey(key);
            rolePrivilegeEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            rolePrivilegeEntity.setCreateTime(new Date());
            rolePrivilegeEntity.setIsValid(1);
            this.save(rolePrivilegeEntity);
        }
    }

    /**
     * 构造菜单树形结构
     * @param privilegeEntityList
     * @return
     */
    private List<RolePrivilegeSimpleDTO> buildPrivilegeTree(List<UserMenuEntity> privilegeEntityList) {
        List<RolePrivilegeSimpleDTO> privilegeTree = Lists.newArrayList();
        // 获取父级菜单
        List<UserMenuEntity> rootPrivilege = privilegeEntityList.stream().filter(e -> StringUtils.isBlank(e.getParentKey())).collect(Collectors.toList());
        rootPrivilege.sort(Comparator.comparing(UserMenuEntity::getSort));
        if (CollectionUtils.isEmpty(rootPrivilege)) {
            return privilegeTree;
        }
        privilegeTree = SmartBeanUtil.copyList(rootPrivilege, RolePrivilegeSimpleDTO.class);
        privilegeTree.forEach(e -> e.setChildren(Lists.newArrayList()));
        this.buildChildPrivilegeList(privilegeEntityList, privilegeTree);
        return privilegeTree;
    }

    /**
     * 构造菜单树形结构
     * @param privilegeEntityList
     * @return
     */
    private List<UserRolePrivilegeVbenDto> buildLoginUserPrivilegeTree(List<UserMenuEntity> privilegeEntityList,Boolean isLogin) {
        List<UserMenuEntity> businessList = privilegeEntityList.stream().filter(e -> StringUtils.isBlank(e.getParentKey())).collect(Collectors.toList());
        // 获取一级菜单数据
        if(isLogin && CollectionUtils.isEmpty(businessList)){
            businessList = privilegeEntityList.stream().filter(e -> StringUtils.equals(e.getParentKey(),"Business")).collect(Collectors.toList());
        }
        List<UserRolePrivilegeVbenDto> privilegeTree = SmartBeanUtil.copyList(businessList, UserRolePrivilegeVbenDto.class);
        if (CollectionUtils.isEmpty(privilegeEntityList)) {
            return privilegeTree;
        }
        privilegeTree.forEach(e -> e.setChildren(Lists.newArrayList()));
        this.buildChildPrivilegeListLogin(privilegeEntityList, privilegeTree);
        return privilegeTree;
    }

    /**
     * 获取子级菜单
     *
     * @param privilegeEntityList
     * @param parentMenuList
     */
    private void buildChildPrivilegeList(List<UserMenuEntity> privilegeEntityList, List<RolePrivilegeSimpleDTO> parentMenuList) {
        List<String> parentKeyList = parentMenuList.stream().map(RolePrivilegeSimpleDTO::getKey).collect(Collectors.toList());
        List<UserMenuEntity> childEntityList = privilegeEntityList.stream().filter(e -> parentKeyList.contains(e.getParentKey())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childEntityList)) {
            return;
        }
        Map<String, List<UserMenuEntity>> listMap = childEntityList.stream().collect(Collectors.groupingBy(UserMenuEntity::getParentKey));
        for (RolePrivilegeSimpleDTO rolePrivilegeSimpleDTO : parentMenuList) {
            String key = rolePrivilegeSimpleDTO.getKey();
            List<UserMenuEntity> privilegeEntities = listMap.get(key);
            if (CollectionUtils.isEmpty(privilegeEntities)) {
                continue;
            }
            privilegeEntities.sort(Comparator.comparing(UserMenuEntity::getSort));
            List<RolePrivilegeSimpleDTO> privilegeList = SmartBeanUtil.copyList(privilegeEntities, RolePrivilegeSimpleDTO.class);
            privilegeList.forEach(e -> e.setChildren(Lists.newArrayList()));
            rolePrivilegeSimpleDTO.setChildren(privilegeList);
            this.buildChildPrivilegeList(privilegeEntityList, privilegeList);
        }
    }

    /**
     * 获取子级菜单-登录场景-唯一入口
     *
     * @param privilegeEntityList
     * @param parentMenuList
     */
    private void buildChildPrivilegeListLogin(List<UserMenuEntity> privilegeEntityList, List<UserRolePrivilegeVbenDto> parentMenuList) {
        List<String> parentKeyList = parentMenuList.stream().map(UserRolePrivilegeVbenDto::getKey).collect(Collectors.toList());
        List<UserMenuEntity> childEntityList = privilegeEntityList.stream().filter(e -> parentKeyList.contains(e.getParentKey())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childEntityList)) {
            return;
        }
        Map<String, List<UserMenuEntity>> listMap = childEntityList.stream().collect(Collectors.groupingBy(UserMenuEntity::getParentKey));
        Map<String,UserMenuEntity> keyMap = privilegeEntityList.stream()
                .collect(Collectors.toMap(UserMenuEntity::getKey, v -> v, (p1, p2) -> p1));

        for (UserRolePrivilegeVbenDto rsd : parentMenuList) {
            String key = rsd.getKey();
            List<UserMenuEntity> privilegeEntities = listMap.get(key);
            UserMenuEntity umePo = keyMap.get(key);
            rsd.setPath(umePo.getUrl());
            UserMenuMetaDto metaDto = new UserMenuMetaDto();
            metaDto.setOrder(umePo.getMetaOrder());
            metaDto.setActivePath(umePo.getMetaActivePath());
            metaDto.setHideInMenu(AssembleUtil.getMenuHideForBooleanResult(umePo.getHideInMenu()));
            metaDto.setIcon(umePo.getIcon());
            metaDto.setTitle(umePo.getMetaTitle());
            rsd.setMeta(metaDto);
            if (CollectionUtils.isEmpty(privilegeEntities)) {
                continue;
            }

            privilegeEntities.sort(Comparator.comparing(UserMenuEntity::getSort));
            List<UserRolePrivilegeVbenDto> privilegeList = new ArrayList<>();
            for(UserMenuEntity vd : privilegeEntities ){
                UserRolePrivilegeVbenDto resPri = new UserRolePrivilegeVbenDto();
                SmartBeanUtil.copyProperties(vd,resPri);
                resPri.setPath(vd.getUrl());
                UserMenuMetaDto metaDto2 = new UserMenuMetaDto();
                metaDto2.setOrder(vd.getMetaOrder());
                metaDto2.setActivePath(vd.getMetaActivePath());
                metaDto2.setHideInMenu(AssembleUtil.getMenuHideForBooleanResult(vd.getHideInMenu()));
                metaDto2.setIcon(vd.getIcon());
                metaDto2.setTitle(vd.getMetaTitle());

                resPri.setMeta(metaDto2);
                privilegeList.add(resPri);
            }
            privilegeList.forEach(e -> e.setChildren(Lists.newArrayList()));
            rsd.setChildren(privilegeList);
            this.buildChildPrivilegeListLogin(privilegeEntityList, privilegeList);
        }
    }


    /**
     * 逻辑删除用户角色，需要根据角色删除所有数据，否则数据存在脏读
     *
     * @param roleId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRolePrivilegeByRoleId(Long roleId, List<String> roleKeyList) {
        if (null == roleId) {
            return;
        }
        List<String> notDeltePriKeyList = CoreConstant.NOT_DELETE_ROLE_PRIKEY;
        LambdaUpdateWrapper<RolePrivilegeEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RolePrivilegeEntity::getRoleId, roleId);
        wrapper.eq(RolePrivilegeEntity::getIsValid, 1);
        // 忽略以下菜单
        wrapper.notIn(RolePrivilegeEntity::getPrivilegeKey, notDeltePriKeyList);
        wrapper.set(RolePrivilegeEntity::getIsValid, 0);
        wrapper.set(RolePrivilegeEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RolePrivilegeEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);

        createDefaultSysRolePrivilege(roleId);
        log.info("call deleteRolePrivilegeByRoleId roleId:{},roleKey:{},result:{}", roleId, roleKeyList, uptRoleRes);
    }

    public void deleteRolePrivilegeByRoleKey(List<String> roleKeyList) {
        LambdaUpdateWrapper<RolePrivilegeEntity> wrapper = new LambdaUpdateWrapper<>();
        if (CollectionUtils.isEmpty(roleKeyList)) {
            return;
        }
        wrapper.in(RolePrivilegeEntity::getPrivilegeKey, roleKeyList);
        wrapper.eq(RolePrivilegeEntity::getIsValid, 1);
        wrapper.set(RolePrivilegeEntity::getIsValid, 0);
        wrapper.set(RolePrivilegeEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(RolePrivilegeEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deleteRolePrivilegeByRoleId roleKey:{},result:{}", roleKeyList, uptRoleRes);
    }

    /**
     * 查询某个用户拥有的角色菜单
     *
     * @param userId
     * @return
     */
    public List<String> queryUserMenuListByUserId(Long userId) {
        List<String> menuKeyList = rolePrivilegeDao.queryUserMenuListByUserId(userId);
        return menuKeyList;
    }

    /**
     * 查询菜单是否已经有对应的角色使用
     * @param key
     * @return
     */
    public List<RolePrivilegeEntity> queryPrivilegeCheckByPrivilegeKey(String key,Long roleId) {
        LambdaUpdateWrapper<RolePrivilegeEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RolePrivilegeEntity::getPrivilegeKey, key);
        wrapper.eq(Args.isNotNull(roleId),RolePrivilegeEntity::getRoleId, roleId);
        wrapper.eq(RolePrivilegeEntity::getIsValid, 1);
        List<RolePrivilegeEntity> list = this.list(wrapper);
        return list;
    }

    private RolePrivilegeEntity queryParentRoleKeyInsert(String parentKey,Long roleId){
        RolePrivilegeEntity rolePrivilegeEntity = null;
        List<RolePrivilegeEntity> parentList = queryPrivilegeCheckByPrivilegeKey(parentKey,roleId);
        if(CollectionUtils.isEmpty(parentList)){
            rolePrivilegeEntity = new RolePrivilegeEntity();
            rolePrivilegeEntity.setRoleId(roleId);
            rolePrivilegeEntity.setPrivilegeKey(parentKey);
            rolePrivilegeEntity.setIsValid(1);
            rolePrivilegeEntity.setCreateTime(new Date());
            rolePrivilegeEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        }
        return rolePrivilegeEntity;
    }
}
