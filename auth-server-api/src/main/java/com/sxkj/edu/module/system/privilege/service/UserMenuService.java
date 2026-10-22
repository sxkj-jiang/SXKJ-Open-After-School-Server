package com.sxkj.edu.module.system.privilege.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.LoginTypeEnum;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.Args;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.common.utils.SmartFileUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import com.sxkj.edu.module.system.privilege.dao.UserMenuDao;
import com.sxkj.edu.module.system.privilege.domain.ao.UserMenuQueryAo;
import com.sxkj.edu.module.system.privilege.domain.dto.*;
import com.sxkj.edu.module.system.privilege.domain.entity.UserMenuEntity;
import com.sxkj.edu.module.system.role.basic.RoleService;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import com.sxkj.edu.module.system.role.roleprivilege.RolePrivilegeService;
import com.sxkj.edu.module.system.role.roleprivilege.domain.entity.RolePrivilegeEntity;
import com.sxkj.edu.service.CacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserMenuService extends ServiceImpl<UserMenuDao, UserMenuEntity> {

    @Resource
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Resource
    private RoleService roleService;

    @Resource
    CacheServiceImpl cacheService;

    /**
     * 批量保存权限菜单项
     *
     * @param menuList
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public JsonResult<String> menuBatchSave(List<UserPrivilegeMenuDTO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return JsonResult.buildSuccess();
        }
        //使用前端发送权限的排序
        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setSort(i);
        }

        List<UserMenuEntity> privilegeList = queryUserMenuBusinessList(PrivilegeTypeEnum.MENU.getValue());
        //若数据库无数据 直接全部保存
        if (CollectionUtils.isEmpty(privilegeList)) {
            List<UserMenuEntity> menuSaveEntity = this.buildPrivilegeMenuEntity(menuList);
            this.saveBatch(menuSaveEntity);
            return JsonResult.buildSuccess();
        }
        //处理需更新的菜单项
        Map<String, UserPrivilegeMenuDTO> storageMap = menuList.stream().collect(Collectors.toMap(UserPrivilegeMenuDTO::getKey, e -> e));
        Set<String> menuKeyList = storageMap.keySet();
        List<UserMenuEntity> updatePrivilegeList = privilegeList.stream().filter(e -> menuKeyList.contains(e.getKey())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updatePrivilegeList)) {
            this.rebuildPrivilegeMenuEntity(storageMap, updatePrivilegeList);
            batchUserMenuUpdate(updatePrivilegeList);
        }
        //处理需删除的菜单项
        List<String> delKeyList = privilegeList.stream().filter(e -> !menuKeyList.contains(e.getKey())).map(UserMenuEntity::getKey).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(delKeyList)) {
            deletePrivilegeByPrikeyList(delKeyList, null);
            //处理需删除的功能点
            deletePrivilegeByPrikeyList(null, delKeyList);
            rolePrivilegeService.deleteRolePrivilegeByRoleKey(delKeyList);
        }

        //处理需新增的菜单项
        List<String> dbKeyList = privilegeList.stream().map(UserMenuEntity::getKey).collect(Collectors.toList());
        List<UserPrivilegeMenuDTO> addPrivilegeList = menuList.stream().filter(e -> !dbKeyList.contains(e.getKey())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addPrivilegeList)) {
            List<UserMenuEntity> menuAddEntityList = this.buildPrivilegeMenuEntity(addPrivilegeList);
            this.saveBatch(menuAddEntityList);
        }
        return JsonResult.buildSuccess();
    }

    /**
     * 构建权限菜单项类别
     *
     * @param menuList
     * @return
     */
    private List<UserMenuEntity> buildPrivilegeMenuEntity(List<UserPrivilegeMenuDTO> menuList) {
        List<UserMenuEntity> privilegeList = Lists.newArrayList();
        UserMenuEntity userMenuEntity;
        for (UserPrivilegeMenuDTO menuDTO : menuList) {
            userMenuEntity = new UserMenuEntity();
            SmartBeanUtil.copyProperties(menuDTO,userMenuEntity);
            userMenuEntity.setUrl(menuDTO.getPath());
            userMenuEntity.setIcon(menuDTO.getMeta().getIcon());
            userMenuEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            userMenuEntity.setCreateTime(new Date());
            if(menuDTO.getMeta() != null){
                UserMenuMetaDto userMenuMetaDto = menuDTO.getMeta();
                userMenuEntity.setMetaTitle(userMenuMetaDto.getTitle());
                userMenuEntity.setMetaActivePath(userMenuMetaDto.getActivePath());
                userMenuEntity.setHideInMenu(AssembleUtil.getMenuHideResult(userMenuMetaDto.getHideInMenu()));
                userMenuEntity.setMetaOrder(userMenuMetaDto.getOrder());
                userMenuEntity.setIcon(userMenuMetaDto.getIcon());
            }
            userMenuEntity.setIsValid(1);
            privilegeList.add(userMenuEntity);
        }
        return privilegeList;
    }


    /**
     * @param menuName
     * @param key
     * @return
     */
    private UserMenuEntity checkPrivlegeEntity(String menuName, String key) {
        UserMenuEntity userMenuEntity = null;
        List<UserMenuEntity> list = new ArrayList<>();
        LambdaUpdateWrapper<UserMenuEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserMenuEntity::getName, menuName);
        lambdaUpdateWrapper.or().eq(UserMenuEntity::getKey, key);
        lambdaUpdateWrapper.eq(UserMenuEntity::getIsValid, 1);
        list = this.list(lambdaUpdateWrapper);
        if (CollectionUtils.isNotEmpty(list)) userMenuEntity = list.get(0);
        return userMenuEntity;
    }

    private UserMenuEntity checkPrivlegeEntityByKey(String key) {
        UserMenuEntity userMenuEntity = null;
        List<UserMenuEntity> list = new ArrayList<>();
        LambdaUpdateWrapper<UserMenuEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserMenuEntity::getKey, key);
        lambdaUpdateWrapper.eq(UserMenuEntity::getIsValid, 1);
        list = this.list(lambdaUpdateWrapper);
        if (CollectionUtils.isNotEmpty(list)) userMenuEntity = list.get(0);
        return userMenuEntity;
    }

    /**
     * 更新权限菜单项
     *
     * @param menuMap
     * @param menuEntityList
     */
    private void rebuildPrivilegeMenuEntity(Map<String, UserPrivilegeMenuDTO> menuMap, List<UserMenuEntity> menuEntityList) {
        for (UserMenuEntity menuEntity : menuEntityList) {
            UserPrivilegeMenuDTO menuDTO = menuMap.get(menuEntity.getKey());
            menuEntity.setName(menuDTO.getName());
            menuEntity.setParentKey(menuDTO.getParentKey());
            menuEntity.setType(menuDTO.getType());
            menuEntity.setSort(menuDTO.getSort());
            menuEntity.setIcon(menuDTO.getMeta().getIcon());
            menuEntity.setIsShow(menuDTO.getIsShow());
            menuEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        }

    }


    /**
     * 保存更新功能点
     *
     * @param privilegeFunctionDTO
     * @return
     */
    public JsonResult<String> functionSaveOrUpdate(PrivilegeFunctionDTO privilegeFunctionDTO) {
        JsonResult<String> jsonResult = JsonResult.buildError("更新菜单功能失败");
        String functionKey = privilegeFunctionDTO.getFunctionKey();
        UserMenuEntity functionEntity = queryUserMenuByKey(functionKey);
        if (functionEntity == null) {
            return JsonResult.buildError(ResultCode.POINT_NOT_EXIST.code(), ResultCode.getMapByCode(ResultCode.POINT_NOT_EXIST.code()));
        }
        UserMenuEntity uptEntity = new UserMenuEntity();
        SmartBeanUtil.copyProperties(privilegeFunctionDTO,uptEntity);
        uptEntity.setId(functionEntity.getId());
        uptEntity.setName(privilegeFunctionDTO.getFunctionName());
        uptEntity.setParentKey(privilegeFunctionDTO.getMenuKey());
        uptEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        uptEntity.setUpdateTime(new Date());
        boolean rower = this.updateById(uptEntity);
        if (rower) {
            jsonResult = JsonResult.buildSuccess();
        }

        return jsonResult;
    }

    /**
     * 查询功能点
     *
     * @param menuKey
     * @return
     */
    public JsonResult<List<PrivilegeFunctionVO>> functionQuery(String menuKey) {
        List<UserMenuEntity> functionPrivilegeList = queryUserMenuByKeyForList(menuKey);
        if (CollectionUtils.isEmpty(functionPrivilegeList)) {
            return JsonResult.buildSuccess(Lists.newArrayList());
        }
        List<PrivilegeFunctionVO> functionList = Lists.newArrayList();
        for (UserMenuEntity functionEntity : functionPrivilegeList) {
            PrivilegeFunctionVO functionDTO = new PrivilegeFunctionVO();
            functionDTO.setFunctionKey(functionEntity.getKey());
            functionDTO.setFunctionName(functionEntity.getName());
            functionDTO.setMenuKey(functionEntity.getParentKey());
            functionDTO.setUrl(functionEntity.getUrl());
            functionDTO.setSort(functionEntity.getSort());
            functionList.add(functionDTO);
        }
        return JsonResult.buildSuccess(functionList);
    }


    private UserMenuEntity function2Privilege(PrivilegeFunctionDTO privilegeFunction) {
        UserMenuEntity UserMenuEntity = new UserMenuEntity();
        UserMenuEntity.setKey(privilegeFunction.getFunctionKey());
        UserMenuEntity.setName(privilegeFunction.getFunctionName());
        UserMenuEntity.setParentKey(privilegeFunction.getMenuKey());
        UserMenuEntity.setType(PrivilegeTypeEnum.POINTS.getValue());
        UserMenuEntity.setSort(privilegeFunction.getSort());
        UserMenuEntity.setUrl("");
        UserMenuEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        UserMenuEntity.setCreateTime(new Date());
        UserMenuEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        UserMenuEntity.setUpdateTime(new Date());
        UserMenuEntity.setIsValid(1);
        return UserMenuEntity;
    }


    public JsonResult<String> menuSave(UserPrivilegeMenuDTO userPrivilegeMenuDTO) {
        JsonResult<String> jsonResult = null;
        Boolean rower = false;
        if (StringUtils.isNotEmpty(userPrivilegeMenuDTO.getParentKey())) {
            if (null == checkPrivlegeEntityByKey(userPrivilegeMenuDTO.getParentKey())) {
                return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "上级菜单key不存在");
            }
        }
        UserMenuEntity UserMenuEntity = checkPrivlegeEntity(userPrivilegeMenuDTO.getName(), userPrivilegeMenuDTO.getKey());
        if (null != UserMenuEntity) {
            return JsonResult.buildError(ResultCode.DATA_EXIST.code(), "菜单名称或编码已经存在");
        } else {
            //新增
            UserMenuEntity privilege = new UserMenuEntity();
            SmartBeanUtil.copyProperties(userPrivilegeMenuDTO,privilege);
            privilege.setUrl(userPrivilegeMenuDTO.getPath());
            privilege.setMenuSource(LoginTypeEnum.MANAGER_TYPE.getCode());
            if(null != userPrivilegeMenuDTO.getMeta()){
                UserMenuMetaDto metaDto = userPrivilegeMenuDTO.getMeta();
                privilege.setMetaTitle(metaDto.getTitle());
                privilege.setMetaOrder(metaDto.getOrder());
                privilege.setHideInMenu(AssembleUtil.getMenuHideResult(metaDto.getHideInMenu()));
                privilege.setMetaActivePath(metaDto.getActivePath());
                privilege.setIcon(metaDto.getIcon());
            }

            // 父级key为空时默认执行开发菜单
            if (StringUtils.isEmpty(userPrivilegeMenuDTO.getParentKey())) {
                privilege.setParentKey("Support");
            }
            privilege.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            privilege.setCreateTime(new Date());
            rower = this.save(privilege);
        }
        log.info("call menuSave in request UserPrivilegeMenuDTO:{},exec result:{}", userPrivilegeMenuDTO, rower);
        if (rower) {
            jsonResult = JsonResult.buildSuccess("保存成功");
        } else {
            jsonResult = JsonResult.buildError("保存失败");
        }
        return jsonResult;
    }

    public JsonResult<String> sysMenuUpt(UserPrivilegeMenuDTO userPrivilegeMenuDTO) {
        JsonResult<String> jsonResult = null;
        Boolean rower = false;
        if (StringUtils.isNotEmpty(userPrivilegeMenuDTO.getParentKey())) {
            if (null == checkPrivlegeEntityByKey(userPrivilegeMenuDTO.getParentKey())) {
                return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "上级菜单key不存在");
            }
        }
        UserMenuEntity origin = this.getById(userPrivilegeMenuDTO.getId());

        if (null == origin) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "数据不存在");
        }
        UserMenuEntity uptEntity = new UserMenuEntity();
        SmartBeanUtil.copyProperties(userPrivilegeMenuDTO,uptEntity);
        uptEntity.setId(origin.getId());
        uptEntity.setUrl(userPrivilegeMenuDTO.getPath());
        if(null != userPrivilegeMenuDTO.getMeta()){
            UserMenuMetaDto metaDto = userPrivilegeMenuDTO.getMeta();
            uptEntity.setMetaTitle(metaDto.getTitle());
            uptEntity.setMetaOrder(metaDto.getOrder());
            uptEntity.setHideInMenu(AssembleUtil.getMenuHideResult(metaDto.getHideInMenu()));
            uptEntity.setMetaActivePath(metaDto.getActivePath());
            uptEntity.setIcon(metaDto.getIcon());
        }
        uptEntity.setMenuSource(LoginTypeEnum.MANAGER_TYPE.getCode());

        //更新
        if (StringUtils.isNotBlank(userPrivilegeMenuDTO.getParentKey())) {
            uptEntity.setParentKey(userPrivilegeMenuDTO.getParentKey());
        }
        uptEntity.setUpdateTime(new Date());
        uptEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        rower = this.updateById(uptEntity);
        log.info("call menuSaveOrUpdate in request UserPrivilegeMenuDTO:{},exec result:{}", userPrivilegeMenuDTO, rower);

        if (rower) {
            jsonResult = JsonResult.buildSuccess("更新成功");
            // 同步清空角色菜单缓存
            cacheService.removeLoginUserCacheLikeKey(AssembleUtil.getSysGrobTypeInfo());

        } else {
            jsonResult = JsonResult.buildError("更新失败");
        }
        return jsonResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePrivilegeByPrikeyList(List<String> currKeyList, List<String> parentKeyList) {
        LambdaUpdateWrapper<UserMenuEntity> wrapper = new LambdaUpdateWrapper<>();
        if (CollectionUtil.isNotEmpty(currKeyList)) {
            wrapper.in(UserMenuEntity::getKey, currKeyList);
        }
        if (CollectionUtil.isNotEmpty(parentKeyList)) {
            wrapper.in(UserMenuEntity::getParentKey, parentKeyList);
        }
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.set(UserMenuEntity::getIsValid, 0);
        wrapper.set(UserMenuEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(UserMenuEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deletePrivilegeByPrikeyList currKeyList:{},parentKeyList,result:{}", currKeyList, parentKeyList, uptRoleRes);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePrivilegeByIdList(List<Long> idList) {
        LambdaUpdateWrapper<UserMenuEntity> wrapper = new LambdaUpdateWrapper<>();
        if (CollectionUtil.isNotEmpty(idList)) {
            wrapper.in(UserMenuEntity::getId, idList);
        }
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.set(UserMenuEntity::getIsValid, 0);
        wrapper.set(UserMenuEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        wrapper.set(UserMenuEntity::getUpdateTime, new Date());
        boolean uptRoleRes = this.update(wrapper);
        log.info("call deletePrivilegeByIdList idList:{},result:{}", idList, uptRoleRes);
    }

    /**
     * 禁用菜单
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> disablePrivilegeData(Long id) {
        JsonResult<String> result = JsonResult.buildError("菜单禁用失败");
        UserMenuEntity UserMenuEntity = this.getById(id);
        if(null == UserMenuEntity) return JsonResult.buildError("菜单查无数据");

        List<RolePrivilegeEntity> roleUserMenuEntityList = rolePrivilegeService.queryPrivilegeCheckByPrivilegeKey(UserMenuEntity.getKey(),null);
        if (CollectionUtil.isNotEmpty(roleUserMenuEntityList)) {
            return JsonResult.buildError("当前菜单正在使用，不能被禁用");
        }
        List<UserMenuEntity> childList = queryUserMenuByKeyForList(UserMenuEntity.getKey());
        if(CollectionUtil.isNotEmpty(childList)){
            return JsonResult.buildError("当前菜单存在正在使用的子菜单，请先禁用子菜单");
        }
        UserMenuEntity.setIsValid(0);
        UserMenuEntity.setIsShow(0);
        UserMenuEntity.setUpdateTime(new Date());
        UserMenuEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());

        boolean uptRoleRes = this.updateById(UserMenuEntity);
        if(uptRoleRes){
            result = JsonResult.buildSuccess("禁用菜单成功");
        }
        return result;
    }


    /**
     * 初始化菜单
     *
     * @return
     */
    public List<UserMenuEntity> getPrivilegeListByRole() {
        LambdaQueryWrapper<UserMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.eq(UserMenuEntity::getIsShow, 1);
        List<UserMenuEntity> list = this.list(wrapper);
        return list;
    }

    /**
     * 初始化管理员角色对应的菜单
     *
     * @return
     */
    public JsonResult<String> initUserRoleAndPrivilegeData() {
        JsonResult<String> jsonResult = JsonResult.buildError("初始化用户角色失败");
        List<UserMenuEntity> UserMenuEntityList = getPrivilegeListByRole();
        List<RolePrivilegeEntity> uptList = new ArrayList<>();
        List<RoleEntity> roleEntityList = roleService.getRoleListByRole(RoleEnum.PORTAL_ROLE_ADMIN.getCode());
        if (CollectionUtils.isNotEmpty(roleEntityList)) {
            for (RoleEntity role : roleEntityList) {
                for (UserMenuEntity p : UserMenuEntityList) {
                    RolePrivilegeEntity re = new RolePrivilegeEntity(role.getId(), p.getKey());
                    uptList.add(re);
                }
            }
            if (CollectionUtils.isNotEmpty(uptList)) {
                rolePrivilegeService.saveBatch(uptList);
                jsonResult = JsonResult.buildSuccess();
            }
        }
        return jsonResult;
    }

    /**
     * 获取当前菜单key的子菜单
     * @param key
     * @return
     */
    private List<UserMenuEntity> queryUserMenuByKeyForList(String key){
        LambdaQueryWrapper<UserMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.eq(UserMenuEntity::getParentKey, key);
        List<UserMenuEntity> list = this.list(wrapper);
        return list;
    }

    public String queryUserMenuByKeyByParentKey(String key){
        LambdaQueryWrapper<UserMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.eq(UserMenuEntity::getKey, key);
        List<UserMenuEntity> list = this.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)) return list.get(0).getParentKey();
        return null;
    }

    private UserMenuEntity queryUserMenuByKey(String key){
        LambdaQueryWrapper<UserMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.eq(UserMenuEntity::getKey, key);
        List<UserMenuEntity> list = this.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)) return list.get(0);
        return null;
    }


    public List<UserMenuEntity> queryUserMenuBusinessList(Integer type){
        LambdaQueryWrapper<UserMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.eq(Args.isNotNull(type),UserMenuEntity::getType, type);
        List<UserMenuEntity> list = this.list(wrapper);
        return list;
    }

    public List<UserMenuEntity> queryUserMenuIsShowTypeList(Integer isShow,Integer type){
        LambdaQueryWrapper<UserMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMenuEntity::getIsValid, 1);
        wrapper.eq(UserMenuEntity::getIsShow, isShow);
        wrapper.eq(Args.isNotNull(type),UserMenuEntity::getType, type);
        List<UserMenuEntity> list = this.list(wrapper);
        return list;
    }

    private Boolean batchUserMenuUpdate(List<UserMenuEntity> menuList){
        List<UserMenuEntity> uptList = new ArrayList<>();
        Boolean rsRower = false;
        if(CollectionUtils.isNotEmpty(menuList)){
            for(UserMenuEntity me: menuList){
                UserMenuEntity old = checkPrivlegeEntity(me.getName(),me.getKey());
                if(old != null){
                    me.setId(old.getId());
                    me.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
                    me.setUpdateTime(new Date());
                }else{
                    me.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                    me.setCreateTime(new Date());
                    me.setIsValid(1);
                }
                uptList.add(me);
            }
            rsRower = this.saveOrUpdateBatch(uptList);
            log.info("call batchUserMenuUpdate in list.size:{},result:{}",menuList.size(),rsRower);
        }
        return rsRower;
    }



    /**
     * 查询所有的权限菜单
     * isSystemMenu: 查询系统菜单-1,2-菜单管理
     *
     * @return
     */
    public JsonResult<List<PrivilegeMenuVO>> menuQueryAll(UserMenuQueryAo userMenuQueryAo, Integer isSystemMenu) {
        List<UserMenuEntity> privilegeEntityList = null;
        // 非管理员时查询用户角色对应的菜单
        Long requestUserId = SmartRequestTokenUtil.getRequestUserId();
        List<String> menukeyList = new ArrayList<>();
        if (userMenuQueryAo.getType() == null) {
            userMenuQueryAo.setIsTrue(2);
        } else {
            userMenuQueryAo.setIsTrue(1);
        }
        String menuSource = null;
        if (isSystemMenu == 1 && StringUtils.isEmpty(userMenuQueryAo.getMenuSource())) {// 兼容系统菜单默认查管理端
            menuSource = LoginTypeEnum.MANAGER_TYPE.getCode();// 默认查管理端
            userMenuQueryAo.setMenuSource(menuSource);
        }
        userMenuQueryAo.setType(PrivilegeTypeEnum.MENU.getValue());
        if (!privilegeEmployeeService.isSuperman(requestUserId)) {
            menukeyList = rolePrivilegeService.queryUserMenuListByUserId(requestUserId);
            // 非管理员时查询用户角色对应的菜单
            userMenuQueryAo.setMenuKeyList(menukeyList);
        }
        privilegeEntityList = this.baseMapper.selectAllMenuByType(userMenuQueryAo);
        if (CollectionUtils.isEmpty(privilegeEntityList)) {
            return JsonResult.buildSuccess(Lists.newArrayList());
        }

        List<PrivilegeMenuVO> voList = privilegeEntityList.stream().map(e -> {
            PrivilegeMenuVO vo = new PrivilegeMenuVO();
            SmartBeanUtil.copyProperties(e, vo);
            vo.setMenuKey(e.getKey());
            vo.setMenuName(e.getName());
            return vo;
        }).collect(Collectors.toList());

        return JsonResult.buildSuccess(voList);
    }

    private void setOperMenuChildData(List<UserMenuDto> dtoList,List<UserMenuEntity> pastList,String parentKey,Integer count){
        if(CollectionUtils.isNotEmpty(dtoList)){
            count++;
            log.info("call setOperMenuChildData foreach number:{}",count);
            for(UserMenuDto dto: dtoList){
                UserMenuEntity userMenuEntity = new UserMenuEntity();
                SmartBeanUtil.copyProperties(dto,userMenuEntity);
                setMenuDetailInfo(userMenuEntity,dto,parentKey);
                userMenuEntity.setSort(count);
                pastList.add(userMenuEntity);

                // 获取子级
                List<UserMenuDto> childList = dto.getChildren();
                if(CollectionUtils.isNotEmpty(childList)){
                    setOperMenuChildData(childList,pastList,userMenuEntity.getKey(),count);
                }
            }
        }
    }

    private void setMenuDetailInfo(UserMenuEntity origin,UserMenuDto dto,String parentKey){
        origin.setUrl(dto.getPath());
        UserMenuMetaDto menuMetaDto = dto.getMeta();
        origin.setIsShow(1);
        origin.setUrl(dto.getPath());
        origin.setParentKey(parentKey);
        origin.setMenuSource(LoginTypeEnum.MANAGER_TYPE.getCode());
        origin.setMetaTitle(menuMetaDto.getTitle());
        origin.setMetaOrder(menuMetaDto.getOrder());
        origin.setMetaActivePath(menuMetaDto.getActivePath());
        origin.setHideInMenu(AssembleUtil.getMenuHideResult(menuMetaDto.getHideInMenu()));
        origin.setIcon(menuMetaDto.getIcon());
    }
}
