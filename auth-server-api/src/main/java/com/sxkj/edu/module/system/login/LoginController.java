package com.sxkj.edu.module.system.login;

import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.dto.LoginCodeCheckAo;
import com.sxkj.edu.domain.sys.dto.RetrieveDTO;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeLoginFormDTO;
import com.sxkj.edu.module.system.login.domain.KaptchaVO;
import com.sxkj.edu.module.system.login.domain.LoginDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 后台登录
 *
 * @author jiangjj
 * @date 2017年12月19日上午11:46:04
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_LOGIN})
@OperateLog
public class LoginController {


    @Resource
    RedisCache redisCache;

    @Autowired
    private LoginService loginService;


    @PostMapping("/session/login")
    @ApiOperation(value = "登录", notes = "登录")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.LOGIN, value = "登录", businessType = "登录")
    @AccessLimit(seconds = 5, maxCount = 6, desc = "登录操作频率过快")
    public JsonResult<LoginDetailVO> login(@Valid @RequestBody EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
        return loginService.login(loginForm, request);
    }

    @GetMapping("/session/get")
    @ApiOperation(value = "获取session判断是否已过期", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.LOGIN, value = "获取session判断是否已过期", businessType = "登录")
    public JsonResult<LoginDetailVO> getSession() {
        RequestTokenBO requestUser = SmartRequestTokenUtil.getRequestUser();
        return loginService.getSession(requestUser);
    }

    @GetMapping("/session/logOut")
    @ApiOperation(value = "退出登陆", notes = "退出登陆")
    @OperateLog(type = AmqpConstant.LOGIN, value = "退出登陆", businessType = "登录")
    public JsonResult<Boolean> logOut(HttpServletRequest request) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if (null == requestToken) {
            return JsonResult.buildError(ResultCode.TOKEN_NULL.code(), "用户登录信息有误");
        }
        return loginService.logoutByToken(request, requestToken);
    }



    @GetMapping("/session/verificationCode")
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.LOGIN, value = "获取验证码", businessType = "登录")
    public JsonResult<KaptchaVO> verificationCode() {
        return loginService.verificationCode();
    }

    @PostMapping("/session/checkVerificationCode")
    @ApiOperation(value = "校验图形验证码", notes = "校验图形验证码")
    @NoNeedLogin
    @AccessLimit(seconds = 10, maxCount = 8, desc = "获取验证码操作频率过快")
    @OperateLog(type = AmqpConstant.LOGIN, value = "校验图形验证码", businessType = "登录")
    public JsonResult<String> checkVerificationCode(@Valid @RequestBody LoginCodeCheckAo loginCodeCheckAo) {
        return loginService.checkVerificationCode(loginCodeCheckAo);
    }


    @PostMapping("retrieve")
    @ApiOperation("找回密码")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.LOGIN, value = "找回密码", businessType = "登录")
    @AccessLimit(seconds = 5, maxCount = 2, desc = "您的操作频率过快，请稍微在试")
    public JsonResult<String> retrieve(@RequestBody @Valid RetrieveDTO dto) {
        if (!dto.getLoginPwd().equals(dto.getPwd())) {
            return JsonResult.buildError(ResultCode.PARA_ERROR.code(), "俩次密码输入不一致");
        }
        return loginService.retrieve(dto);
    }



}
