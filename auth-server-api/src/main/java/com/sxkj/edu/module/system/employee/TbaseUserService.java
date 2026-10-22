package com.sxkj.edu.module.system.employee;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.base.enums.EmployeeStatusEnum;
import com.sxkj.edu.common.base.enums.JudgeEnum;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.base.enums.UserTypeEnum;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.ListPageUtils;
import com.sxkj.edu.common.page.SmartPageUtil;
import com.sxkj.edu.common.utils.*;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.base.UptUserLogoAo;
import com.sxkj.edu.module.afterschool.domain.dto.ParentRegisterAO;
import com.sxkj.edu.module.afterschool.domain.dto.TeacherRegisterAO;
import com.sxkj.edu.module.system.department.DepartmentDao;
import com.sxkj.edu.module.system.department.domain.entity.DepartmentEntity;
import com.sxkj.edu.module.system.employee.domain.ao.UserQueryAO;
import com.sxkj.edu.module.system.employee.domain.ao.UserQuerySingAO;
import com.sxkj.edu.module.system.employee.domain.ao.UserUpdateStatusAo;
import com.sxkj.edu.module.system.employee.domain.ao.QueryRolesBySchoolAndPhoneAO;
import com.sxkj.edu.module.system.employee.domain.bo.EmployeeBO;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.employee.domain.dto.*;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeSingVO;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.module.system.employee.domain.vo.RoleAccountHierarchyVO;
import com.sxkj.edu.module.system.employee.domain.vo.AccountInfoVO;
import com.sxkj.edu.module.system.login.LoginService;
import com.sxkj.edu.module.system.privilege.service.PrivilegeEmployeeService;
import com.sxkj.edu.module.system.role.basic.RoleService;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserService;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserDataDTO;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserEntity;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.CacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description : 用户管理
 * @Author : JIANGJJ
 * @Date : 2023-5-10 16:03
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Service
@Slf4j
public class TbaseUserService extends ServiceImpl<TBaseUserDao, TBaseUserEntity> {

    /**
     * 员工基本信息的缓存
     */
    private static final ConcurrentHashMap<Long, EmployeeBO> employeeCache = new ConcurrentHashMap<>();
    @Resource
    RoleUserService userRoleService;
    @Resource
    RoleService roleService;
    @Value("${base.defaultPassword}")
    private String defaultPassword;
    @Autowired
    private TBaseUserDao userDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Resource
    private CacheServiceImpl cacheService;

    @Resource
    LoginService loginService;
    @Resource
    BaseFileService baseFileService;



    @Value("${default.logo.url}")
    private String defaultLogoUrl;

    public List<EmployeeVO> getAllEmployee(Integer schoolId) {
        List<EmployeeVO> listVo = new ArrayList<>();
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1);
        queryWrapper.eq(TBaseUserEntity::getStatus, 1);
        queryWrapper.eq(schoolId != null && schoolId > 0,TBaseUserEntity::getSchoolId, schoolId);
        queryWrapper.eq(TBaseUserEntity::getApproveStatus, 1);

        List<TBaseUserEntity> tBaseUserEntityList = this.list(queryWrapper);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tBaseUserEntityList)) {
            tBaseUserEntityList.forEach(f -> {
                EmployeeVO employeeVO = new EmployeeVO();
                BeanUtils.copyProperties(f, employeeVO);
                listVo.add(employeeVO);
            });
        }
        return listVo;
    }

    public List<EmployeeSingVO> getPassUserAll(UserQuerySingAO querySingAO) {
        List<EmployeeSingVO> listVo = new ArrayList<>();
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1);
        queryWrapper.eq(TBaseUserEntity::getStatus, 1);
        queryWrapper.eq(TBaseUserEntity::getApproveStatus, 1);
        queryWrapper.like(Args.isNotEmpty(querySingAO.getActualName()),TBaseUserEntity::getActualName, querySingAO.getActualName());
        queryWrapper.like(Args.isNotEmpty(querySingAO.getPhone()),TBaseUserEntity::getPhone, querySingAO.getPhone());

        List<TBaseUserEntity> tBaseUserEntityList = this.list(queryWrapper);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tBaseUserEntityList)) {
            tBaseUserEntityList.forEach(f -> {
                EmployeeSingVO employeeVO = new EmployeeSingVO();
                BeanUtils.copyProperties(f, employeeVO);
                listVo.add(employeeVO);
            });
        }
        return listVo;
    }

    public EmployeeBO getById(Long id) {
        EmployeeBO employeeBO = employeeCache.get(id);
        if (employeeBO == null) {
            TBaseUserEntity TBaseUserEntity = userDao.selectById(id);
            if (TBaseUserEntity != null) {
                Set<Long> roles = privilegeEmployeeService.queryRoleByEmployeeId(id);
                boolean superman = roles.contains(RoleEnum.PORTAL_ROLE_ADMIN.getRoleId());

                employeeBO = new EmployeeBO(TBaseUserEntity, superman,roles);
                employeeCache.put(TBaseUserEntity.getId(), employeeBO);
            }
        }
        return employeeBO;
    }

    /**
     * 查询员工列表
     *
     * @param queryAO
     * @return
     */
    public JsonResult<PageResultDTO<EmployeeVO>> selectEmployeeList(UserQueryAO queryAO) {
        Page pageParam = SmartPageUtil.convert2QueryPage(queryAO);
        List<EmployeeDTO> employeeList = userDao.selectEmployeeList(pageParam, queryAO, null);
        List<EmployeeVO> resultList = new ArrayList<>();
        // 单独查询角色信息
        if (CollectionUtils.isNotEmpty(employeeList)) {
            List<RoleUserDataDTO> roleUserList = userRoleService.getAllUserRoleName();
            Map<Long,List<RoleUserDataDTO>> roleUserMap = roleUserList.stream().distinct().collect(Collectors.groupingBy(RoleUserDataDTO::getUserId));
            for (EmployeeDTO f : employeeList) {
                EmployeeVO employeeVO = new EmployeeVO();
                List<RoleUserDataDTO> roleUserGsList = new ArrayList<>();
                if(CollectionUtil.isNotEmpty(roleUserMap) && roleUserMap.containsKey(f.getId())){
                    roleUserGsList = roleUserMap.get(f.getId());
                }
                f.setUserRoleCurrList(roleUserGsList);
                String roleName = "";
                if (CollectionUtils.isNotEmpty(roleUserGsList)) {
                    StringBuffer sf = new StringBuffer(1024);
                    int idx = 0;
                    for (RoleUserDataDTO ro : roleUserGsList) {
                        sf.append(ro.getRoleName());
                        if (roleUserGsList.size() != 1 && idx < roleUserGsList.size() - 1) {
                            sf.append(",");
                        }
                        idx++;
                    }
                    roleName = sf.toString();

                }
                SmartBeanUtil.copyProperties(f, employeeVO);
                employeeVO.setUserRoleName(roleName);
                employeeVO.setUserRoleList(roleUserGsList);
                resultList.add(employeeVO);
            }
        }

        ListPageUtils<EmployeeVO> listSub = new ListPageUtils(queryAO.getPageIndex(), queryAO.getPageSize(), pageParam.getTotal());
        // 手动分页
        List<EmployeeVO> subArrList = listSub.setListSplitForSizePage(resultList, listSub);
        PageResultDTO pageResultDTO = new PageResultDTO<>();
        SmartBeanUtil.copyProperties(listSub, pageResultDTO);
        pageResultDTO.setList(subArrList);
        return JsonResult.buildSuccess(pageResultDTO);
    }


    /**
     * 新增员工
     * 密码加密：AES+MD5双重加密
     *
     * @param employeeAddDto
     * @return
     */
    public JsonResult<String> addEmployee(EmployeeAddDTO employeeAddDto) {
        JsonResult<String> result = JsonResult.buildError("新增用户失败");
        TBaseUserEntity entity = SmartBeanUtil.copy(employeeAddDto, TBaseUserEntity.class);
        if (StringUtils.isNotEmpty(employeeAddDto.getIdCard())) {
            boolean checkResult = Pattern.matches(SmartVerificationUtil.ID_CARD, employeeAddDto.getIdCard());
            if (!checkResult) {
                return JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户信息校验失败");
            }
        }
        //同名员工
        EmployeeDTO sameNameEmployee = userDao.getByLoginName(entity.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
        if (null != sameNameEmployee) {
            return JsonResult.buildError(ResultCode.USER_EXISTS.code(), "用户已存在");
        }
        EmployeeDTO samePhoneEmployee = queryUserByPhone(entity.getPhone(),null);
        if (null != samePhoneEmployee) {
            return JsonResult.buildError(ResultCode.USER_EXISTS.code(), "用户手机号已存在");
        }
        if (ObjectUtil.isNotNull(entity.getDepartmentId())) {
            Long departmentId = entity.getDepartmentId();
            DepartmentEntity department = departmentDao.selectById(departmentId);
            if (department == null) {
                return JsonResult.buildError(ResultCode.USER_EXISTS.code(), "部门不存在");
            }
        }
        // 前端已经AES加密过的密码
        String pwd = entity.getLoginPwd();
        entity.setLoginPwd(AssembleUtil.getUserEncrptPwd(pwd, defaultPassword));

        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());

        entity.setApproveStatus(1);
        entity.setStatus(EmployeeStatusEnum.NORMAL.getValue());
        if(StringUtils.isEmpty(employeeAddDto.getUserLogo())){
            entity.setLogoUrl(defaultLogoUrl);
        }else{
            entity.setLogoUrl(employeeAddDto.getUserLogo());
        }

        // 根据角色设置用户类型
        List<RoleEntity> roleList = roleService.getRoleList(employeeAddDto.getRoleIds());
        entity.setUserType(AssembleUtil.getUserTypeByRoleList(roleList));
        Integer rower = userDao.insert(entity);

        // 设置初始化角色
        List<Long> userIds = new ArrayList<>();
        userIds.add(entity.getId());
        userRoleService.setInitUserRoleListBatch(userIds, employeeAddDto.getRoleIds());
        return result;
    }

    /**
     * 更新禁用状态
     *
     * @param userUpdateStatusAo
     * @return
     */
    public JsonResult<String> updateStatus(UserUpdateStatusAo userUpdateStatusAo) {
        Long userId = userUpdateStatusAo.getUserId();
        if (null == userId) {
            return JsonResult.buildError(ResultCode.UPDATE_FAILED.code(), "用户更新失败");
        }
        EmployeeBO entity = getById(userId);
        if (null == entity) {
            return JsonResult.buildError(ResultCode.UPDATE_FAILED.code(), "用户更新失败");
        }
        List<Long> empIds = Lists.newArrayList();
        empIds.add(userId);
        batchDisableUpdateStatus(empIds, userUpdateStatusAo.getUserStatus());
        employeeCache.remove(userId);
        return JsonResult.buildSuccess();
    }

    public void batchDisableUpdateStatus(List<Long> userIds, Integer status) {
        UpdateWrapper<TBaseUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", userIds);
        updateWrapper.eq("is_valid", 1);
        updateWrapper.set("status", status);
        updateWrapper.set("update_time", new Date());
        updateWrapper.set("update_user", SmartRequestTokenUtil.getRequestUserId());
        boolean uptRow = this.update(updateWrapper);
        log.info("call user batchDisableUpdateStatus in result:{}", uptRow);
    }

    /**
     * 批量更新员工状态
     *
     * @param batchUpdateStatusDTO
     * @return
     */
    public JsonResult<String> batchUpdateStatus(EmployeeBatchUpdateStatusDTO batchUpdateStatusDTO) {
        batchDisableUpdateStatus(batchUpdateStatusDTO.getUserIds(), batchUpdateStatusDTO.getStatus());
        if (batchUpdateStatusDTO.getUserIds() != null) {
            batchUpdateStatusDTO.getUserIds().forEach(e -> employeeCache.remove(e));
        }
        return JsonResult.buildSuccess();
    }

    /**
     * 更新员工
     *
     * @param updateDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateEmployee(EmployeeUpdateDTO updateDTO) {
        JsonResult<String> responseDTO = JsonResult.buildError("更新用户失败");
        try {
            Long employeeId = updateDTO.getId();
            TBaseUserEntity userEntity = userDao.selectById(employeeId);
            if (null == userEntity) {
                responseDTO = JsonResult.buildError(ResultCode.NOT_FOUND.code(), "用户不存在");
                return responseDTO;
            }
            if (StringUtils.isNotBlank(updateDTO.getIdCard())) {
                boolean checkResult = Pattern.matches(SmartVerificationUtil.ID_CARD, updateDTO.getIdCard());
                if (!checkResult) {
                    responseDTO = JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户信息校验失败");
                    return responseDTO;
                }
            }
            if (StringUtils.isNotEmpty(updateDTO.getBirthday())) {
                boolean checkResult = Pattern.matches(SmartVerificationUtil.DATE, updateDTO.getBirthday());
                if (!checkResult) {
                    responseDTO = JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户生日信息校验失败");
                    return responseDTO;
                }
            }
            // 修改登录名的情况需要校验登录名规则
            if (!userEntity.getLoginName().equals(updateDTO.getLoginName())) {
                if (!Pattern.matches(SmartVerificationUtil.LOGIN_NAME_REGEXP, userEntity.getLoginName())) {
                    return JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户登录名校验失败");
                }
            }
            EmployeeDTO sameNameEmployee = userDao.getByLoginName(updateDTO.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
            if (null != sameNameEmployee && !sameNameEmployee.getId().equals(employeeId)) {
                return JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户已存在");
            }
            EmployeeDTO samePhoneEmployee = queryUserByPhone(updateDTO.getLoginName(),null);
            if (null != samePhoneEmployee && !samePhoneEmployee.getId().equals(employeeId)) {
                return JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "手机已存在");
            }
//            String newPwd = updateDTO.getLoginPwd();
//            if (!StringUtils.isBlank(newPwd)) {
//                updateDTO.setLoginPwd(SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, newPwd));
//            } else {
//                updateDTO.setLoginPwd(userEntity.getLoginPwd());
//            }
            TBaseUserEntity entity = SmartBeanUtil.copy(updateDTO, TBaseUserEntity.class);
            entity.setUpdateTime(new Date());
            if (StringUtils.isEmpty(entity.getBirthday())) {
                entity.setBirthday(null);
            }
            entity.setIsValid(userEntity.getIsValid());
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            if(StringUtils.isEmpty(updateDTO.getUserLogo())){
                entity.setLogoUrl(defaultLogoUrl);
            }else{
                entity.setLogoUrl(updateDTO.getUserLogo());
            }
            Integer rower = userDao.updateById(entity);

            //更新用户角色信息
            EmployeeUpdateRolesDTO updateRolesDTO = new EmployeeUpdateRolesDTO();
            updateRolesDTO.setRoleIds(updateDTO.getRoleIds());
            updateRolesDTO.setUserId(updateDTO.getId());
            JsonResult<String> result = this.updateRoles(updateRolesDTO);
            employeeCache.remove(employeeId);
            if(rower >0 && 200 == result.getCode()){
                // 清空redis缓存
                removeUserRedisCache(entity.getLoginName());
                responseDTO = JsonResult.buildSuccess("更新用户成功");
            }

            log.info("call EmployeeService updateEmployee exec success,in request:{},", updateDTO);
        } catch (Exception e) {
            log.error("call EmployeeService updateEmployee exec error,in request:{},", updateDTO);
        }

        return responseDTO;
    }

    /**
     * 删除员工
     *
     * @param employeeId 员工ID
     * @return
     */
    public JsonResult<String> deleteEmployeeById(Long employeeId) {
        TBaseUserEntity tBaseUser = userDao.selectById(employeeId);
        if (null == tBaseUser) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "用户不存在");
        }
        //假删
        tBaseUser.setIsValid(JudgeEnum.NO.getValue());
        tBaseUser.setUpdateTime(new Date());
        tBaseUser.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        userDao.updateById(tBaseUser);
        employeeCache.remove(employeeId);
        return JsonResult.buildSuccess();
    }

    /**
     * 更新用户角色
     *
     * @param updateRolesDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateRoles(EmployeeUpdateRolesDTO updateRolesDTO) {
        userRoleService.deleteUserRoleByRoleId(updateRolesDTO.getUserId(), updateRolesDTO.getRoleIds());
        if (CollectionUtils.isNotEmpty(updateRolesDTO.getRoleIds())) {
            List<RoleUserEntity> roleEmployeeEntities = Lists.newArrayList();
            RoleUserEntity roleTBaseUserEntity;
            for (Long roleId : updateRolesDTO.getRoleIds()) {
                roleTBaseUserEntity = new RoleUserEntity();
                roleTBaseUserEntity.setUserId(updateRolesDTO.getUserId());
                roleTBaseUserEntity.setRoleId(roleId);
                roleTBaseUserEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                roleTBaseUserEntity.setCreateTime(new Date());
                roleTBaseUserEntity.setIsValid(1);
                roleEmployeeEntities.add(roleTBaseUserEntity);
            }
            userRoleService.saveBatch(roleEmployeeEntities);

            //同步更新用户类型
            TBaseUserEntity userEntity = baseMapper.selectById(updateRolesDTO.getUserId());
            List<RoleEntity> roleList = roleService.getRoleList(updateRolesDTO.getRoleIds());
            userEntity.setUserType(AssembleUtil.getUserTypeByRoleList(roleList));
            userEntity.setUpdateTime(new Date());
            userEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            userDao.updateById(userEntity);
        }
        return JsonResult.buildSuccess();
    }

    /**
     * 更新密码
     *
     * @param updatePwdDTO
     * @param requestToken
     * @return
     */
    public JsonResult<String> updatePwd(EmployeeUpdatePwdDTO updatePwdDTO, RequestTokenBO requestToken) {
        JsonResult<String> uptPwdRes = JsonResult.buildError("更新密码失败");
        log.info("call baseUser updatePwd in request value:{}", updatePwdDTO);
        Long employeeId = requestToken.getRequestUserId();
        TBaseUserEntity employee = userDao.selectById(employeeId);
        if (employee == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "用户不存在");
        }
        String oldAesPwd = "", currAesPwd = "";
        try {
            oldAesPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, updatePwdDTO.getOldPwd());
            currAesPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, updatePwdDTO.getConfirmPwd());
            if (!employee.getLoginPwd().equals(oldAesPwd)) {
                return JsonResult.buildError(ResultCode.AUTH_ERROR.code(), "用户密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!updatePwdDTO.getPwd().equals(updatePwdDTO.getConfirmPwd())) {
            return JsonResult.buildError(ResultCode.AUTH_ERROR.code(), "两次密码不一致");
        }
        employee.setLoginPwd(currAesPwd);
        employee.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        employee.setUpdateTime(new Date());
        Integer uptRow = userDao.updateById(employee);
        employeeCache.remove(employeeId);
        if(CheckParamUtil.checkIntegerStatusParam(uptRow)){
            uptPwdRes = JsonResult.buildSuccess("更新密码成功");
        }
        return uptPwdRes;
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateUserPhone(BaseUserUpdatePhoneAo phoneAo, RequestTokenBO requestToken) {
        JsonResult<String> jsonResult = JsonResult.buildError("更新用户绑定手机失败");
        log.info("call baseUser BaseUserUpdatePhoneAo in request value:{}", phoneAo);
        Long employeeId = requestToken.getRequestUserId();
        TBaseUserEntity employee = userDao.selectById(employeeId);
        if (employee == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "用户不存在");
        }
        TBaseUserEntity origin = checkUserByPhoneExist(phoneAo.getUserPhone());
        if(null != origin && origin.getId() != employeeId){
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "当前手机号已存在");
        }
        String currPhone = employee.getPhone();
        employee.setPhone(phoneAo.getUserPhone());
        employee.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        employee.setUpdateTime(new Date());
        Boolean uptRow = this.updateById(employee);
        if(uptRow){
            // 清空redis缓存
            removeUserRedisCache(employee.getLoginName());
            jsonResult = JsonResult.buildSuccess("更新用户绑定手机成功");
            employeeCache.remove(employeeId);
        }
        return jsonResult;
    }

    public JsonResult<List<EmployeeVO>> getEmployeeByDeptId(Long departmentId) {
        List<EmployeeVO> list = userDao.getEmployeeIdByDeptId(departmentId);
        return JsonResult.buildSuccess(list);
    }

    /**
     * 重置密码
     *
     * @param employeeId
     * @return
     */
    public JsonResult resetPasswd(Integer employeeId) {
        JsonResult responseDTO = null;
        try {
            String md5Password = GrobProperties.getInstance().USER_ENCRPT;
            userDao.updatePassword(employeeId, md5Password, SmartRequestTokenUtil.getRequestUserId());
            employeeCache.remove(employeeId);

            responseDTO = JsonResult.buildSuccess();
            log.info("call EmployeeService resetPwd exec success,userId:{},", employeeId);
        } catch (Exception e) {
            log.error("call EmployeeService resetPwd exec error,userId:{},", employeeId);
            responseDTO = JsonResult.buildError("重置密码失败，请联系管理员");
        }
        return responseDTO;
    }

    public JsonResult<String> batchAddEmployee(List<EmployeeExcelDTO> list) {
        List<TBaseUserEntity> listEmp = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (EmployeeExcelDTO emp : list) {
                TBaseUserEntity entity = SmartBeanUtil.copy(emp, TBaseUserEntity.class);
                if (StringUtils.isEmpty(entity.getLoginName())) {
                    return JsonResult.buildError(ResultCode.LOGIN_NAME_IS_NULL.code(), "登录名不能为空"); // 校验："登录名不能为空！"
                } else {
                    if (!Pattern.matches(SmartVerificationUtil.LOGIN_NAME_REGEXP, entity.getLoginName())) {
                        return JsonResult.buildError(ResultCode.LOGIN_NAME_IS_NULL.code(), "登录名格式不正确"); // 校验："登录名格式不正确！"
                    }
                }
                if (StringUtils.isEmpty(entity.getActualName())) {
                    return JsonResult.buildError(ResultCode.LOGIN_NAME_IS_NULL.code(), "姓名不能为空"); // 校验："姓名不能为空！"
                }
                if (StringUtils.isEmpty(entity.getPhone())) {
                    return JsonResult.buildError(ResultCode.LOGIN_NAME_IS_NULL.code(), "手机号不能为空"); // 校验："手机号不能为空！"
                }
                if (entity.getUserType() != null && entity.getUserType() > 5) {
                    return JsonResult.buildError(ResultCode.LOGIN_NAME_IS_NULL.code(), "用户类型数值只能为0至5");
                }

                if (entity.getDepartmentId() == null) {
                    // 设置初始部门
                    entity.setDepartmentId(43L);
                }
                if (StringUtils.isNotEmpty(entity.getIdCard())) {
                    boolean checkResult = Pattern.matches(SmartVerificationUtil.ID_CARD, entity.getIdCard());
                    if (!checkResult) {
                        return JsonResult.buildError(ResultCode.LOGIN_NAME_IS_NULL.code(), "请输入正确的身份证号"); // 校验："请输入正确的身份证号"
                    }
                }
                //同名员工
                EmployeeDTO sameNameEmployee = userDao.getByLoginName(entity.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
                if (null != sameNameEmployee) {
                    return JsonResult.buildError(ResultCode.USER_EXISTS.code(), "用户已存在"); // 校验："登录名已存在!"
                }
                //同电话员工
                EmployeeDTO samePhoneEmployee = queryUserByPhone(entity.getPhone(),null);
                if (null != samePhoneEmployee) {
                    return JsonResult.buildError(ResultCode.USER_EXISTS.code(), "手机号已经存在"); // 检验："手机号已经存在"
                }

                //如果没有密码  设置默认密码
                String pwd = entity.getLoginPwd();
                entity.setLoginPwd(AssembleUtil.getUserEncrptPwd(pwd, defaultPassword));

                entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                if (StringUtils.isEmpty(entity.getBirthday())) {
                    entity.setBirthday(null);
                }
                entity.setCreateTime(new Date());
                entity.setApproveStatus(1);
                listEmp.add(entity);
            }
        }
        // 校验无误新增数据
        if (!listEmp.isEmpty()) {
            for (TBaseUserEntity employee : listEmp) {
                employee.setStatus(EmployeeStatusEnum.NORMAL.getValue());
                userDao.insert(employee);
                // 设置初始化教师角色
                List<Long> userIds = new ArrayList<>();
                userIds.add(employee.getId());
                userRoleService.setInitUserRoleBatch(userIds, AssembleUtil.getRoleCodeByUserType(employee.getUserType()));

            }
        }
        return JsonResult.buildSuccess("操作成功");
    }

    public void batchSetRole(EmployeeBatchUpdateRolesDTO employeeBatchUpdateRolesDTO) {
        List<Long> employeeIds = employeeBatchUpdateRolesDTO.getUserIds();
        if (employeeIds != null && employeeIds.size() > 0) {
            EmployeeUpdateRolesDTO dto = new EmployeeUpdateRolesDTO();
            dto.setRoleIds(employeeBatchUpdateRolesDTO.getRoleIds());
            for (Long id : employeeIds) {
                dto.setUserId(id);
                updateRoles(dto);
            }
        }
    }

    public JsonResult approve(Long id, Integer approveStatus) {
        TBaseUserEntity baseUserEntity = userDao.selectById(id);
        if (baseUserEntity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "用户不存在");
        }
        if(2 == approveStatus && 1 == baseUserEntity.getApproveStatus()) return JsonResult.buildError(ResultCode.OPER_ERROR.code(), "用户已经审核通过,不允许驳回");
        baseUserEntity.setApproveStatus(approveStatus);
        baseUserEntity.setUpdateTime(new Date());
        baseUserEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        userDao.updateById(baseUserEntity);
        return JsonResult.buildSuccess();
    }



    public JsonResult<String> batchUserDeptById(List<Long> userIdList) {
        JsonResult<String> responseDTO = JsonResult.buildError("操作失败");
        UpdateWrapper<TBaseUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", userIdList);
        updateWrapper.eq("is_valid", 1);
        updateWrapper.set("department_id", null);
        updateWrapper.set("update_user", SmartRequestTokenUtil.getRequestUserId());
        updateWrapper.set("update_time", new Date());
        Boolean uptRow = this.update(updateWrapper);
        if (uptRow) {
            responseDTO = JsonResult.buildSuccess();
        }
        return responseDTO;
    }


    // 教师注册，手机号唯一识别
    public EmployeeDTO queryTeacherUserByPhoneForRegister(String phone,Long schoolId) {
        List<Integer> userTypeList = new ArrayList<>();
        userTypeList.add(2);
        userTypeList.add(4);
        EmployeeDTO userDto = new EmployeeDTO();
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getPhone, phone);
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1);
        queryWrapper.in(TBaseUserEntity::getUserType, userTypeList);
        queryWrapper.eq(Args.isNotNull(schoolId),TBaseUserEntity::getSchoolId, schoolId);
        List<TBaseUserEntity> userList = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userList)) {
            TBaseUserEntity userEntity = userList.get(0);
            SmartBeanUtil.copyProperties(userEntity, userDto);
        }else {
            userDto = null;
        }
        return userDto;
    }

    public EmployeeDTO queryUserByPhone(String phone,Long schoolId) {
        EmployeeDTO userDto = new EmployeeDTO();
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getPhone, phone);
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1);
        queryWrapper.eq(Args.isNotNull(schoolId),TBaseUserEntity::getSchoolId, schoolId);
        List<TBaseUserEntity> userList = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userList)) {
            TBaseUserEntity userEntity = userList.get(0);
            SmartBeanUtil.copyProperties(userEntity, userDto);
        }else {
            userDto = null;
        }
        return userDto;
    }

    public JsonResult<String> uptUserLogo(UptUserLogoAo logoAo) {
        JsonResult<String> responseDTO = JsonResult.buildError("操作失败");
        UpdateWrapper<TBaseUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", logoAo.getUserIds());
        updateWrapper.eq("is_valid", 1);
        updateWrapper.set("logo_url", logoAo.getLogoUrl());
        updateWrapper.set("update_user", SmartRequestTokenUtil.getRequestUserId());
        updateWrapper.set("update_time", new Date());
        Boolean uptRow = this.update(updateWrapper);
        if (uptRow) {
            responseDTO = JsonResult.buildSuccess();
        }
        return responseDTO;
    }

    public Map<Long, String> getActualNameMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyMap();
        List<TBaseUserEntity> entities = listByIds(ids);
        if (CollectionUtils.isEmpty(entities)) return Collections.emptyMap();
        return entities.stream().collect(Collectors.toMap(TBaseUserEntity::getId, TBaseUserEntity::getActualName));
    }

    // 查询用户账号是否都已经审核通过
    public Boolean getUserFinishPass(Long schoolId,String phone,Integer userType) {
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getSchoolId, schoolId);
        queryWrapper.eq(TBaseUserEntity::getLoginName, phone);
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1);
        queryWrapper.eq(TBaseUserEntity::getUserType, userType);
        queryWrapper.eq(TBaseUserEntity::getApproveStatus, 0);
        List<TBaseUserEntity> userList = this.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(userList)){
            // 存在待审核或驳回的数据
            return true;
        }
        return false;
    }

    // 批量更新用户的状态
    public Boolean updateUserAuditStatusFinishPass(Long schoolId,String phone,Integer auditStatus,Integer userType) {
        LambdaUpdateWrapper<TBaseUserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TBaseUserEntity::getSchoolId, schoolId);
        updateWrapper.eq(TBaseUserEntity::getLoginName, phone);
        updateWrapper.eq(TBaseUserEntity::getIsValid, 1);
        updateWrapper.eq(TBaseUserEntity::getUserType, userType);
        updateWrapper.set(TBaseUserEntity::getApproveStatus, auditStatus);
        updateWrapper.set(TBaseUserEntity::getUpdateUser, SmartRequestTokenUtil.getRequestUserId());
        updateWrapper.set(TBaseUserEntity::getUpdateTime, new Date());
        Boolean updateRow = this.update(updateWrapper);
        return updateRow;
    }


    public List<TBaseUserEntity> checkUserExistByIdcardOrPhoneData(String idCard,String phone, Long schoolId) {
        return list(Wrappers.lambdaQuery(TBaseUserEntity.class)
                .eq(TBaseUserEntity::getIsValid,1)
                .eq(TBaseUserEntity::getSchoolId, schoolId)
                .eq(TBaseUserEntity::getIdCard, idCard)
                .or().eq(Args.isNotEmpty(phone), TBaseUserEntity::getPhone, phone));
    }

    public TBaseUserEntity checkUserByPhoneExist(String phone) {
        TBaseUserEntity userEntity = null;
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getPhone, phone);
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1);
        List<TBaseUserEntity> userList = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userList)) {
            userEntity = userList.get(0);
        }
        return userEntity;
    }

    public void removeUserRedisCache(String loginName){
        // 清空redis缓存
        String loginType = AssembleUtil.getUserAgentLoginType();
        cacheService.removeLoginUserCache(loginType,loginName);
        log.info("call removeUserRedisCache in loginName:{},loginType:{}",loginName,loginType);
    }

    // 根据登录账号模糊删除redis的数据
    public void removeLikeUserNameRedisCache(String loginName){
        // 清空redis缓存
        String loginType = AssembleUtil.getUserAgentLoginType();
        cacheService.removeLoginUserNameCacheLikeKey(loginName);
        log.info("call removeLoginUserNameCacheLikeKey in loginName:{},loginType:{}",loginName,loginType);
    }

    /**
     * 更新用户
     *
     * @param updateDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updatePlatfromUser(EmployeePlatformUpdateDTO updateDTO) {
        JsonResult<String> responseDTO = null;
        try {
            Long employeeId = updateDTO.getId();
            TBaseUserEntity userEntity = userDao.selectById(employeeId);
            if (null == userEntity) {
                responseDTO = JsonResult.buildError(ResultCode.NOT_FOUND.code(), "用户不存在");
                return responseDTO;
            }
            if (StringUtils.isNotBlank(updateDTO.getIdCard())) {
                boolean checkResult = Pattern.matches(SmartVerificationUtil.ID_CARD, updateDTO.getIdCard());
                if (!checkResult) {
                    responseDTO = JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户信息校验失败");
                    return responseDTO;
                }
            }
            if (StringUtils.isNotEmpty(updateDTO.getBirthday())) {
                boolean checkResult = Pattern.matches(SmartVerificationUtil.DATE, updateDTO.getBirthday());
                if (!checkResult) {
                    responseDTO = JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户生日信息校验失败");
                    return responseDTO;
                }
            }
            if(StringUtils.isNotBlank(updateDTO.getPhone())){
                EmployeeDTO samePhoneEmployee = userDao.getUserLoginByPhone(updateDTO.getPhone(), EmployeeStatusEnum.NORMAL.getValue(),userEntity.getSchoolId());
                if (null != samePhoneEmployee && !samePhoneEmployee.getId().equals(employeeId)) {
                    return JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "手机已存在");
                }
            }


            userEntity.setUpdateTime(new Date());
            userEntity.setRemark(updateDTO.getUserDesc());
            String userType = AssembleUtil.getSysGrobTypeInfo();
            // 重新加载用户登录缓存信息
            loginService.reloadUserCacheDataForRedis(userEntity.getLoginName(),userType);

            userEntity.setUpdateTime(new Date());
            userEntity.setUpdateUser(employeeId);
            userDao.updateById(userEntity);
            employeeCache.remove(employeeId);
            responseDTO = JsonResult.buildSuccess();

            log.info("call BaseUserService updatePlatfromUser exec success,in request:{},", updateDTO);
        } catch (Exception e) {
            log.error("call BaseUserService updatePlatfromUser exec error,in request:{},", updateDTO);
            responseDTO = JsonResult.buildError(ResultCode.VERIFICATION_USER_INVALID.code(), "用户更新失败");
        }

        return responseDTO;
    }

    // 教师注册 isAdd为1时为新增用户身份
    public Long createBaseUserByTeacher(TeacherRegisterAO ao,Boolean teacherExists,Integer isAdd){
        // 检查用户是否已存在（根据手机号）
        // 判断教师是否为班主任
        Integer userType = null;
        if(ao.getIsHeadTeacher() != null && ao.getIsHeadTeacher() == 1){
            userType = UserTypeEnum.LEADER.getValue();
        }else{
            userType = UserTypeEnum.TEACH.getValue();
        }
        TBaseUserEntity existingUser = getTeacherUserByLoginPhone(ao.getPhone(), ao.getSchoolId(),userType);

        // 创建或更新用户信息
        TBaseUserEntity user = new TBaseUserEntity();
        if (existingUser != null) {
            SmartBeanUtil.copyProperties(existingUser,user);
            // 如果教师信息已存在，则审批状态为已通过，否则为待审批
            user.setApproveStatus(teacherExists ? 1 : 0);
            user.setUpdateTime(new Date());
            user.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            user.setUserType(userType);
        } else {
            user.setLoginName(ao.getPhone());
            user.setPhone(ao.getPhone());
            user.setActualName(ao.getTeacherName());
            user.setSex(ao.getGender());
            user.setIdCard(ao.getIdCard());
            String logoUrl = baseFileService.getFileUrlById(ao.getAvatarFileId());
            if(StringUtils.isBlank(logoUrl)){
                logoUrl = defaultLogoUrl;
            }
            user.setLogoUrl(logoUrl);
            user.setSchoolId(ao.getSchoolId());
            user.setUserType(userType);
            user.setStatus(1); // 启用
            // 如果教师信息已存在，则审批状态为已通过，否则为待审批
            user.setApproveStatus(teacherExists ? 1 : 0);
            user.setIsValid(1);
            user.setCreateTime(new Date());
            user.setCreateUser(SmartRequestTokenUtil.getRequestUserId() == null ? 1 : SmartRequestTokenUtil.getRequestUserId());
            // 密码加密
            if(isAdd == 1){
                user.setLoginPwd(ao.getPassword());
            }else{
                String encryptedPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, ao.getPassword());
                user.setLoginPwd(encryptedPwd);
            }

        }
        Boolean uptRow = false;
        // 保存或更新用户
        if (existingUser != null) {
            uptRow = this.updateById(user);
        } else {
            uptRow = this.save(user);
        }
        log.info("call createBaseUserByTeacher exec success,in request:{},updateRow:{}", ao, uptRow);
        if(uptRow){
            //同步创建用户角色,区分是班主任角色
            if(ao.getIsHeadTeacher() != null && ao.getIsHeadTeacher() == 1){
                updateUserRoleAppData(user.getId(),RoleEnum.PORTAL_ROLE_TEACH_LEADER);
            }else{
                updateUserRoleAppData(user.getId(),RoleEnum.PORTAL_ROLE_TEACHER);
            }

            return user.getId();
        }
        return null;
    }

    // 学生家长注册
    public Long createBaseUserByParent(ParentRegisterAO ao, Boolean studentExists,Integer isAdd){
        // 检查用户是否已存在（根据手机号）
        TBaseUserEntity existingUser = getParentUserByLoginPhoneAndUser(ao.getSchoolId(),ao.getPhone(),ao.getParentName(),1);
        // 创建或更新用户信息
        TBaseUserEntity user = new TBaseUserEntity();
        if (existingUser != null) {
            SmartBeanUtil.copyProperties(existingUser,user);
            // 如果学生信息已存在，则审批状态为已通过，否则为待审批
            user.setApproveStatus(studentExists ? 1 : 0);
            user.setUpdateTime(new Date());
            user.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        } else {
            user.setLoginName(ao.getPhone());
            user.setPhone(ao.getPhone());
            user.setActualName(ao.getParentName());
            user.setSex(ao.getStudentGender());
            String logoUrl = baseFileService.getFileUrlById(ao.getAvatarFileId());
            if(StringUtils.isBlank(logoUrl)){
                logoUrl = defaultLogoUrl;
            }
            user.setLogoUrl(logoUrl);
            user.setSchoolId(ao.getSchoolId());
            user.setUserType(1); // 学生(家长)
            user.setStatus(1); // 启用
            // 如果学生信息已存在，则审批状态为已通过，否则为待审批
            user.setApproveStatus(studentExists ? 1 : 0);
            user.setIsValid(1);
            user.setCreateTime(new Date());
            user.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            // 密码加密
            if(isAdd == 1){
                user.setLoginPwd(ao.getPassword());
            }else{
                String encryptedPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, ao.getPassword());
                user.setLoginPwd(encryptedPwd);
            }
        }

        Boolean uptRow = false;
        // 保存或更新用户
        if (existingUser != null) {
            uptRow = this.updateById(user);
        } else {
            uptRow = this.save(user);
        }
        log.info("call createBaseUserByParent exec success,in request:{},updateRow:{}", ao, uptRow);
        if(uptRow){
            updateUserRoleAppData(user.getId(),RoleEnum.PORTAL_ROLE_STU);
            return user.getId();
        }
        return user.getId();
    }


    // 小程序中新增角色身份
    private Boolean updateUserRoleAppData(Long userId,RoleEnum roleEnum){
        userRoleService.updateUserRoleBatchForApp(userId, roleEnum);
        return true;
    }

    public TBaseUserEntity getUserByLoginuNum(String loginName) {
        LambdaQueryWrapper<TBaseUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TBaseUserEntity::getIsValid, 1);
        wrapper.eq(TBaseUserEntity::getLoginName, loginName);
        TBaseUserEntity user = this.getOne(wrapper);
        return user;
    }

    // 查询教师、班主任的用户信息是否存在，存在即更新
    public TBaseUserEntity getTeacherUserByLoginPhone(String phone,Long schoolId,Integer userType) {
        LambdaQueryWrapper<TBaseUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TBaseUserEntity::getIsValid, 1);
        wrapper.eq(TBaseUserEntity::getPhone, phone);
        wrapper.eq(TBaseUserEntity::getUserType, userType);
        wrapper.eq(TBaseUserEntity::getSchoolId, schoolId);
        TBaseUserEntity user = this.getOne(wrapper);
        return user;
    }

    // 用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师
    public TBaseUserEntity getParentUserByLoginPhoneAndUser(Long schoolId,String phone,String userName,Integer userType) {
        LambdaQueryWrapper<TBaseUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TBaseUserEntity::getIsValid, 1);
        wrapper.eq(TBaseUserEntity::getPhone, phone);
        wrapper.eq(TBaseUserEntity::getSchoolId, schoolId);
        wrapper.eq(TBaseUserEntity::getUserType, userType);
        wrapper.eq(TBaseUserEntity::getActualName, userName);
        TBaseUserEntity user = this.getOne(wrapper);
        return user;
    }

    public List<TBaseUserEntity> querySysUserByIds(Long schoolId,List<Long> ids) {
        LambdaQueryWrapper<TBaseUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TBaseUserEntity::getIsValid, 1);
        wrapper.in(TBaseUserEntity::getId, ids);
        wrapper.eq(TBaseUserEntity::getSchoolId, schoolId);
        List<TBaseUserEntity> users = this.list(wrapper);
        return users;
    }

    /**
     * 根据学校ID+手机号查询用户角色及账号信息
     * 返回层级结构：一级为角色名称，二级为该角色下的账号列表
     *
     * @param queryAO 查询参数
     * @return 角色账号层级结构列表
     */
    public JsonResult<List<RoleAccountHierarchyVO>> queryRolesBySchoolAndPhone(QueryRolesBySchoolAndPhoneAO queryAO) {
        // 1. 根据手机号和学校ID查询所有匹配的用户
        LambdaQueryWrapper<TBaseUserEntity> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(TBaseUserEntity::getPhone, queryAO.getPhone());
        userWrapper.eq(TBaseUserEntity::getSchoolId, queryAO.getSchoolId());
        userWrapper.eq(TBaseUserEntity::getIsValid, 1);
        userWrapper.eq(TBaseUserEntity::getStatus, 1);
        userWrapper.eq(TBaseUserEntity::getApproveStatus, 1);
        List<TBaseUserEntity> userList = this.list(userWrapper);

        if (CollectionUtils.isEmpty(userList)) {
            return JsonResult.buildSuccess(new ArrayList<>());
        }

        // 2. 获取所有用户的ID列表
        List<Long> userIds = userList.stream()
                .map(TBaseUserEntity::getId)
                .collect(Collectors.toList());

        // 3. 查询这些用户的所有角色关联信息
        LambdaQueryWrapper<RoleUserEntity> roleUserWrapper = new LambdaQueryWrapper<>();
        roleUserWrapper.in(RoleUserEntity::getUserId, userIds);
        roleUserWrapper.eq(RoleUserEntity::getIsValid, 1);
        List<RoleUserEntity> roleUserList = userRoleService.list(roleUserWrapper);

        if (CollectionUtils.isEmpty(roleUserList)) {
            return JsonResult.buildSuccess(new ArrayList<>());
        }

        // 4. 获取所有角色ID
        Set<Long> roleIds = roleUserList.stream()
                .map(RoleUserEntity::getRoleId)
                .collect(Collectors.toSet());

        // 5. 查询角色信息
        LambdaQueryWrapper<RoleEntity> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.in(RoleEntity::getId, roleIds);
        roleWrapper.eq(RoleEntity::getIsValid, 1);
        List<RoleEntity> roleList = roleService.list(roleWrapper);

        // 6. 构建角色ID到角色实体的映射
        Map<Long, RoleEntity> roleMap = roleList.stream()
                .collect(Collectors.toMap(RoleEntity::getId, role -> role));

        // 7. 构建用户ID到用户实体的映射
        Map<Long, TBaseUserEntity> userMap = userList.stream()
                .collect(Collectors.toMap(TBaseUserEntity::getId, user -> user));

        // 8. 按角色分组，构建层级结构
        Map<Long, List<Long>> roleToUsersMap = roleUserList.stream()
                .collect(Collectors.groupingBy(
                        RoleUserEntity::getRoleId,
                        Collectors.mapping(RoleUserEntity::getUserId, Collectors.toList())
                ));

        // 9. 构建返回结果
        List<RoleAccountHierarchyVO> result = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> entry : roleToUsersMap.entrySet()) {
            Long roleId = entry.getKey();
            List<Long> uIds = entry.getValue();

            RoleEntity role = roleMap.get(roleId);
            if (role == null) {
                continue;
            }

            RoleAccountHierarchyVO hierarchyVO = new RoleAccountHierarchyVO();
            hierarchyVO.setRoleId(roleId);
            hierarchyVO.setRoleName(role.getRoleName());

            // 构建账号列表
            List<AccountInfoVO> accounts = new ArrayList<>();
            for (Long userId : uIds) {
                TBaseUserEntity user = userMap.get(userId);
                if (user != null) {
                    AccountInfoVO accountVO = new AccountInfoVO();
                    accountVO.setUserId(userId);
                    accountVO.setActualName(user.getActualName());
                    accountVO.setPhone(user.getPhone());
                    accountVO.setLoginName(user.getLoginName());
                    accounts.add(accountVO);
                }
            }

            hierarchyVO.setAccounts(accounts);
            result.add(hierarchyVO);
        }

        return JsonResult.buildSuccess(result);
    }
}
