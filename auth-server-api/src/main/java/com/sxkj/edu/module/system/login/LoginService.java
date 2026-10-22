package com.sxkj.edu.module.system.login;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.EmployeeStatusEnum;
import com.sxkj.edu.common.base.enums.JudgeEnum;
import com.sxkj.edu.common.base.enums.LoginTypeEnum;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.common.utils.SmartDigestUtil;
import com.sxkj.edu.common.utils.SmartIPUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.SchoolManageSwithAO;
import com.sxkj.edu.domain.sys.ao.RetrieveByPhoneAo;
import com.sxkj.edu.domain.sys.dto.LoginCodeCheckAo;
import com.sxkj.edu.domain.sys.dto.LoginPhoneReq;
import com.sxkj.edu.domain.sys.dto.RegisterDTO;
import com.sxkj.edu.domain.sys.dto.RetrieveDTO;
import com.sxkj.edu.domain.sys.entity.UserLoginLogEntity;
import com.sxkj.edu.module.afterschool.service.AsGuardianStudentService;
import com.sxkj.edu.module.system.department.DepartmentService;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.ao.SwitchUserRolePriAO;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeLoginFormDTO;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.login.domain.*;
import com.sxkj.edu.module.system.privilege.service.PrivilegeEmployeeService;
import com.sxkj.edu.module.system.role.basic.RoleDao;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserDao;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserEntity;
import com.sxkj.edu.module.system.role.roleprivilege.RolePrivilegeService;
import com.sxkj.edu.service.CacheServiceImpl;
import com.sxkj.edu.service.LogService;
import com.sxkj.edu.service.SchoolManageService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @Description : 登录实现
 * @Author : JIANGJJ
 * @Date : 2022-9-7 11:43
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Slf4j
@Service
public class LoginService {

    private static final String VERIFICATION_CODE_REDIS_PREFIX = "vc_%s";

    @Autowired
    private TBaseUserDao userDao;

    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private LoginTokenService loginTokenService;

    @Autowired
    private LogService logService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private RedisCache redisCache;

    @Autowired
    private RoleDao roleDao;

    @Resource
    private CacheServiceImpl cacheService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private TbaseUserService tbaseUserService;

    @Resource
    private SchoolManageService schoolManageService;

    @Resource
    private AsGuardianStudentService asGuardianStudentService;

    /**
     * 登陆
     *
     * @param loginForm 登录名 密码
     * @return 登录用户基本信息
     */
    public JsonResult<LoginDetailVO> login(@Valid EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
        EmployeeDTO employeeDTO = null;
        LoginDetailVO loginDTO = null;
        try {
            String loginType = AssembleUtil.getUserAgentLoginType();
            log.info("call login user exec,loginName:{},loginType:{}", loginForm.getLoginName(),loginType);
            if(StringUtils.isEmpty(loginType)) loginType = LoginTypeEnum.USER_WECHAT_TYPE.getCode();

            String redisVerificationCode = (String) redisCache.get(loginForm.getCodeUuid());
            //增加删除已使用的验证码方式 频繁登录
//            redisCache.delete(loginForm.getCodeUuid());
//            if (StringUtils.isEmpty(redisVerificationCode)) {
//                return JsonResult.buildError(ResultCode.VERIFICATION_CODE_INVALID.code(), "验证码无效");
//            }
//            if (!redisVerificationCode.equalsIgnoreCase(loginForm.getCode())) {
//                return JsonResult.buildError(ResultCode.VERIFICATION_CODE_INVALID.code(), "验证码无效");
//            }
            String loginPwd = null;
            try {
                loginPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, loginForm.getLoginPwd());
            } catch (Exception e) {
                return JsonResult.buildError(ResultCode.LOGIN_FAILED.code(), "用户名密码错误");
            }
            List<EmployeeDTO> employeeDTOList = userDao.userLoginMethod(loginForm.getLoginName(), loginPwd, null);
            if(CollectionUtil.isEmpty(employeeDTOList)) return JsonResult.buildError(ResultCode.LOGIN_FAILED.code(), "用户名密码错误");

            // 兼容一个账号同时要登录移动端和管理端
            List<EmployeeDTO> sortUserList = null;
            if(loginType.equals(LoginTypeEnum.MANAGER_TYPE.getCode())) {
                sortUserList = employeeDTOList.stream().filter(employeeDTO1 -> employeeDTO1.getUserType() == 0 ||
                        employeeDTO1.getUserType() == 3).collect(Collectors.toList());
            }else{
                sortUserList = employeeDTOList.stream().filter(employeeDTO1 -> employeeDTO1.getUserType() >= 1).collect(Collectors.toList());
            }
            if(CollectionUtil.isNotEmpty(sortUserList)){
                employeeDTO = sortUserList.get(0);
            }
            log.info("call login user exec,loginName:{},sortUserList:{}", loginForm.getLoginName(),sortUserList);
            if (null == employeeDTO) {
                log.warn("call login exec fail,request:{},pwd:{}", loginForm, loginPwd);
                return JsonResult.buildError(ResultCode.LOGIN_FAILED.code(), "用户名密码错误");
            }
            if (StringUtils.isNotEmpty(loginType) && loginType.equals(LoginTypeEnum.MANAGER_TYPE.getCode())
                    && (employeeDTO.getUserType() == 1 || employeeDTO.getUserType() == 2
                    || employeeDTO.getUserType() == 4)) {
                return JsonResult.buildError(ResultCode.USER_SYS_ERROR.code(), "普通用户无此访问权限");
            } else if (StringUtils.isNotEmpty(loginType)
                    && (loginType.equals(LoginTypeEnum.USER_PC_TYPE.getCode()) || loginType.equals(LoginTypeEnum.USER_WECHAT_TYPE.getCode()))
                    && (employeeDTO.getUserType() == 0 || employeeDTO.getUserType() == 3)) {
                return JsonResult.buildError(ResultCode.USER_SYS_ERROR.code(), "管理员无用户端访问权限");
            }else if (StringUtils.isEmpty(loginType)) {
                loginType = LoginTypeEnum.MANAGER_TYPE.getCode();
            }
            //jwt token赋值
            String userToken = loginTokenService.generateToken(employeeDTO);
            loginDTO = SmartBeanUtil.copy(employeeDTO, LoginDetailVO.class);
            // 设置用户时的学校信息
            if (null != employeeDTO.getSchoolId()) {
                LoginStuSchoolDetailVO schoolDetailVO = schoolManageService.querySchoolNameById(employeeDTO.getSchoolId());
                log.info("call login user get schoolDetailVO in resp:{}",schoolDetailVO);
                if (null != schoolDetailVO) {
                    loginDTO.setSchoolInfo(schoolDetailVO);
                    // 初始化设置用户-学校id到全局线程缓存
                    redisCache.hset("set_user_school_info", employeeDTO.getLoginName(), String.valueOf(schoolDetailVO.getSchoolId()));
                }
            }
            // 设置家长小孩到全局线程缓存
            if(employeeDTO.getUserType() == 1){
                List<LoginStuParentDetailVO> parentDetailVOList = asGuardianStudentService.queryAllStudentByParent(employeeDTO.getPhone(),employeeDTO.getId(), employeeDTO.getSchoolId());
                loginDTO.setChildInfo(parentDetailVOList);
            }

            // 先从redis里获取缓存信息
            LoginDetailVO redisDtoResult = cacheService.getLoginUserCache(loginType, employeeDTO.getLoginName());
            if(null != redisDtoResult && AssembleUtil.checkTokenInValid(redisDtoResult.getXAccessToken())){
                // 返回缓存结果
                loginDTO = redisDtoResult;
                log.info("call user in loginByPwd loginName:{},isTokenValid:{},success",employeeDTO.getLoginName(),true);
            } else {
                // 获取用户数据并设置到缓存中
                setUserDataForRedisDb(loginDTO,employeeDTO,userToken,loginType,1);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("用户登录失败");
        }

        //登陆操作日志
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        UserLoginLogEntity logEntity =
                UserLoginLogEntity.builder()
                        .userId(employeeDTO.getId())
                        .userName(employeeDTO.getActualName())
                        .remoteIp(SmartIPUtil.getClientIp(request))
                        .remotePort(request.getRemotePort())
                        .remoteBrowser(userAgent.getBrowser().getName())
                        .remoteOs(userAgent.getOperatingSystem().getName())
                        .loginType(1)
                        .loginStatus(JudgeEnum.YES.getValue()).build();

        LoginGrobInfoVO sysGrobInfo = AssembleUtil.getSysGrobInfo();
        if(ObjectUtil.isNotNull(sysGrobInfo)){
            logEntity.setAccessTerminal(sysGrobInfo.getLoginType());
        }

        logService.addLog(logEntity);

        return JsonResult.buildSuccess(loginDTO);
    }


    /**
     * 手机端退出登陆，清除token缓存
     *
     * @param requestToken
     * @return 退出登陆是否成功，bool
     */
    public JsonResult<Boolean> logoutByToken(HttpServletRequest request, RequestTokenBO requestToken) {
        //退出登陆操作日志
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        UserLoginLogEntity logEntity =
                UserLoginLogEntity.builder()
                        .userId(requestToken.getEmployeeBO().getId())
                        .userName(requestToken.getEmployeeBO().getActualName())
                        .remoteIp(SmartIPUtil.getClientIp(request))
                        .remotePort(request.getRemotePort())
                        .remoteBrowser(userAgent.getBrowser().getName())
                        .remoteOs(userAgent.getOperatingSystem().getName())
                        .loginStatus(JudgeEnum.YES.getValue())
                        .loginType(2).build();
        LoginGrobInfoVO sysGrobInfo = AssembleUtil.getSysGrobInfo();
        if(ObjectUtil.isNotNull(sysGrobInfo)){
            logEntity.setAccessTerminal(sysGrobInfo.getLoginType());
        }
        logService.addLog(logEntity);
        // 清空学校配置缓存
        SchoolManageSwithAO schoolManageSwithAO = new SchoolManageSwithAO(null,null);
        schoolManageService.swithSchoolData(schoolManageSwithAO,request);
        // 清空redis缓存
        cacheService.removeLoginUserCache(AssembleUtil.getSysGrobTypeInfo(), requestToken.getEmployeeBO().getLoginName());
        privilegeEmployeeService.removeCache(requestToken.getRequestUserId());
        redisCache.hdel("set_user_school_info", requestToken.getEmployeeBO().getLoginName());
        log.info("call logoutByToken in exec success,loginName:{}",requestToken.getEmployeeBO().getLoginName());
        return JsonResult.buildSuccess();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public JsonResult<KaptchaVO> verificationCode() {
        KaptchaVO kaptchaVO = new KaptchaVO();
        String uuid = buildVerificationCodeRedisKey(UUID.randomUUID().toString());
        String kaptchaText = defaultKaptcha.createText();

        String base64Code = "";
        BufferedImage image = defaultKaptcha.createImage(kaptchaText);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("verificationCode exception .{}", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.error("verificationCode outputStream close exception .{}", e);
                }
            }
        }
        kaptchaVO.setUuid(uuid);
        kaptchaVO.setCode("data:image/png;base64," + base64Code);
        redisCache.set(uuid, kaptchaText, 120);
        return JsonResult.buildSuccess(kaptchaVO);
    }

    // 校验图形认证码
    public JsonResult<String> checkVerificationCode(LoginCodeCheckAo ao) {
        String redisVerificationCode = (String) redisCache.get(ao.getCodeUuid());
        //增加删除已使用的验证码方式 频繁登录
        redisCache.delete(ao.getCodeUuid());
        if (StringUtils.isEmpty(redisVerificationCode)) {
            return JsonResult.buildError(ResultCode.VERIFICATION_CODE_INVALID.code(), "验证码无效");
        }
        if (!redisVerificationCode.equalsIgnoreCase(ao.getCode())) {
            return JsonResult.buildError(ResultCode.VERIFICATION_CODE_INVALID.code(), "验证码无效");
        }
        return JsonResult.buildSuccess("验证码正确");
    }

    private String buildVerificationCodeRedisKey(String uuid) {
        return String.format(VERIFICATION_CODE_REDIS_PREFIX, uuid);
    }

    public JsonResult<LoginDetailVO> getSession(RequestTokenBO requestUser) {
        JsonResult<LoginDetailVO> jsonResult = null;
        LoginDetailVO loginDTO = SmartBeanUtil.copy(requestUser.getEmployeeBO(), LoginDetailVO.class);
        LoginGrobInfoVO loginGrobInfoVO = AssembleUtil.getSysGrobInfo();
        String loginType = null;
        if (null != loginGrobInfoVO) {
            loginType = loginGrobInfoVO.getLoginType();
        }
        if (StringUtils.isEmpty(loginType)) loginType = LoginTypeEnum.MANAGER_TYPE.getCode();
        // 先从redis里获取缓存信息
        LoginDetailVO redisDtoResult = cacheService.getLoginUserCache(loginType, loginDTO.getLoginName());
        if(redisDtoResult == null){
            return JsonResult.buildError(ResultCode.LOGIN_ERROR.code(),"当前用户登录已失效，请重新登录");
        }

        Boolean isTokenValid = AssembleUtil.checkTokenInValid(redisDtoResult.getXAccessToken());
        log.info("call user in loginByPwd loginName:{},isTokenValid:{}",loginDTO.getLoginName(),isTokenValid);
        if(isTokenValid == false && null != redisDtoResult){
            return JsonResult.buildError(ResultCode.LOGIN_ERROR.code(),"当前用户登录已失效，请重新登录");
        }

        if (null != redisDtoResult) {
            // 返回缓存结果
            loginDTO = redisDtoResult;
        } else {
            Long userRoleId = getUserMaxPriRole(loginDTO.getRoleEntityList());
            loginDTO.setPrivilegeList(rolePrivilegeService.queryUserMenuLoginByRoleIdForPc(userRoleId,loginType));
            // 查询部门名称
            loginDTO.setDepartmentName(departmentService.getDeptNameById(loginDTO.getDepartmentId()));
            //判断是否为超管
            Boolean isSuperman = privilegeEmployeeService.isSuperman(loginDTO.getId());
            loginDTO.setIsSuperMan(isSuperman);
        }
        jsonResult = JsonResult.buildSuccess(loginDTO);
        return jsonResult;
    }

    public JsonResult<List<RoleEntity>> roleList() {
        return JsonResult.buildSuccess(roleDao.selectList(new QueryWrapper<RoleEntity>().ne("id", "1")));
    }

    public JsonResult<String> retrieve(RetrieveDTO dto) {

        TBaseUserEntity employee = userDao.selectOne(new QueryWrapper<TBaseUserEntity>().eq("phone", dto.getPhone()));
        if (employee == null) {
            return JsonResult.buildError(ResultCode.PARA_ERROR.code(), "手机号填写错误");
        }
        String encrptPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, dto.getLoginPwd());
        employee.setLoginPwd(encrptPwd);
        employee.setUpdateTime(new Date());
        employee.setUpdateUser(employee.getId());
        int row = userDao.updateById(employee);
        log.info("call retrieve user phone:{},pwd:{},exec result:{}",dto.getPhone(),encrptPwd,row);
        return JsonResult.buildSuccess("找回密码成功,请重新登录");
    }


    /**
     * 获取用户角色里权限最大的角色
     * @param roleSelectedVOS
     * @return
     */
    private Long getUserMaxPriRole(List<RoleSelectedVO> roleSelectedVOS){
        Long roleId = 0L;
        if(CollectionUtil.isNotEmpty(roleSelectedVOS)){
            if(roleSelectedVOS.size() == 1){
                roleId = roleSelectedVOS.get(0).getId();
                return roleId;
            }else{
                for(RoleSelectedVO ro: roleSelectedVOS){
                    if(StringUtils.equals(ro.getCode(), RoleEnum.PORTAL_ROLE_ADMIN.getCode())){
                        roleId = ro.getId();
                        break;
                    }else if(StringUtils.equals(ro.getCode(), RoleEnum.PORTAL_ROLE_SCHOOL_ADMIN.getCode())){
                        roleId = ro.getId();
                        break;
                    }else if(StringUtils.equals(ro.getCode(), RoleEnum.PORTAL_ROLE_TEACHER.getCode())){
                        roleId = ro.getId();
                        break;
                    }else if(StringUtils.equals(ro.getCode(), RoleEnum.PORTAL_ROLE_STU.getCode())){
                        roleId = ro.getId();
                        break;
                    }else{
                        roleId = ro.getId();
                        break;
                    }
                }
            }
        }
        return roleId;
    }

    /**
     * 重新加载用户缓存
     * @param loginName
     */
    public void reloadUserCacheDataForRedis(String loginName,String loginType){
        TBaseUserEntity userEntity = tbaseUserService.getUserByLoginuNum(loginName);
        if(null != userEntity){
            EmployeeDTO userVo = new EmployeeDTO();
            SmartBeanUtil.copyProperties(userEntity,userVo);
            //jwt token赋值
            String userToken = loginTokenService.generateToken(userVo);

            LoginDetailVO loginDetailVO = getUserDataDetailVo(userVo);
            // 设置缓存
            setUserDataForRedisDb(loginDetailVO,userVo,userToken,loginType,2);
        }
    }


    private LoginDetailVO getUserDataDetailVo(EmployeeDTO employeeDTO){
        LoginDetailVO loginDTO = SmartBeanUtil.copy(employeeDTO, LoginDetailVO.class);
        return loginDTO;
    }

    /**
     * 具体设置用户信息到redis
     * @param loginDTO
     * @param employeeDTO
     * @param token
     * @param loginType
     * initStatus : 1-初次登录，2-覆盖更新
     */
    private LoginDetailVO setUserDataForRedisDb(LoginDetailVO loginDTO,EmployeeDTO employeeDTO,String token,String loginType,int initStatus){

        //获取前端功能权限
        QueryMaxLoginRoleIdDTO roleIdDTO = operRoleSelect(userDao.selectByAllUserRole(employeeDTO.getLoginName(),employeeDTO.getSchoolId(),loginType),loginDTO.getUserType());
        if(null != roleIdDTO && CollectionUtil.isNotEmpty(roleIdDTO.getRoleSelectedVOList())){
            loginDTO.setRoleEntityList(roleIdDTO.getRoleSelectedVOList());
            Long userRoleId = roleIdDTO.getRoleId();
            log.info("call user login in userName:{},userRoleId:{}",employeeDTO.getLoginName(),userRoleId);
            loginDTO.setPrivilegeList(rolePrivilegeService.queryUserMenuLoginByRoleIdForPc(userRoleId,loginType));
            //判断是否为超管
            if(roleIdDTO.getRoleId() == RoleEnum.PORTAL_ROLE_ADMIN.getRoleId()){
                loginDTO.setIsSuperMan(true);
            }
            loginDTO.setLinkName(employeeDTO.getLoginName());
            loginDTO.setLoginTime(new Date());
        }
        loginDTO.setXAccessToken(token);

        if(initStatus == 2){
            cacheService.reloadLoginUserCache(loginType, employeeDTO.getLoginName(),
                    JSON.parseObject(JSONObject.toJSONString(loginDTO)));
        }else{
            cacheService.setLoginUserCache(loginType, employeeDTO.getLoginName(),
                    JSON.parseObject(JSONObject.toJSONString(loginDTO)));
        }
        return loginDTO;
    }


    // 设置角色优先级，用于角色切换使用: 管理员》学校管理员》班主任》授课教师》学生
    private QueryMaxLoginRoleIdDTO operRoleSelect(List<RoleSelectedVO> roleSelectedVOS,Integer userType){
        QueryMaxLoginRoleIdDTO roleIdDTO = new QueryMaxLoginRoleIdDTO();
        List<RoleSelectedVO> resList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(roleSelectedVOS)){
            for(RoleSelectedVO roleSelectedVO: roleSelectedVOS){
                Integer currUserType = AssembleUtil.getUserTypeByRoleId(roleSelectedVO.getId());
                // 优先取用户表的类型
                if(userType == currUserType){
                    roleSelectedVO.setSelected(true);
                    roleIdDTO.setRoleId(roleSelectedVO.getId());
                }else{
                    roleSelectedVO.setSelected(false);
                }
                resList.add(roleSelectedVO);
            }
        }
        roleIdDTO.setRoleSelectedVOList(resList);
        return roleIdDTO;
    }

}
