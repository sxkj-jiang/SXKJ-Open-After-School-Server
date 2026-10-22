package com.sxkj.edu.module.system.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.LoginTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.login.domain.LoginDetailVO;
import com.sxkj.edu.module.system.login.domain.WxMiniProgramLoginVO;
import com.sxkj.edu.module.system.login.domain.WxMiniProgramPhoneLoginAO;
import com.sxkj.edu.service.CacheServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序登录 Service 实现
 *
 * @author system
 * @date 2026-05-25
 */
@Slf4j
@Service
public class WxMiniProgramLoginServiceImpl implements WxMiniProgramLoginService {

    /**
     * 微信小程序 AppID
     */
    @Value("${wx.miniprogram.appid:}")
    private String appid;

    /**
     * 微信小程序 AppSecret
     */
    @Value("${wx.miniprogram.secret:}")
    private String secret;

    /**
     * 微信获取 access_token 的 URL
     */
    private static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";

    /**
     * 微信 code2Session 的 URL
     */
    private static final String WX_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";

    /**
     * 微信获取手机号的 URL
     */
    private static final String WX_GET_PHONE_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token={access_token}";

    @Resource
    private TbaseUserService tbaseUserService;

    @Resource
    private LoginTokenService loginTokenService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private CacheServiceImpl cacheService;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public JsonResult<WxMiniProgramLoginVO> phoneLogin(WxMiniProgramPhoneLoginAO ao) {
        try {
            // 1. 通过 code 获取 openid 和 session_key
            JSONObject sessionResult = code2Session(ao.getCode());
            if (sessionResult == null) {
                return JsonResult.buildError(ResultCode.PARA_ERROR.code(), "微信登录凭证已过期");
            }

            String openid = sessionResult.getString("openid");
            if (StringUtils.isEmpty(openid)) {
                return JsonResult.buildError(ResultCode.PARA_ERROR.code(), "获取微信用户信息失败");
            }

            // 2. 通过 phoneCode 获取手机号
            String phoneNumber = getUserPhoneNumber(ao.getPhoneCode());
            if (StringUtils.isEmpty(phoneNumber)) {
                return JsonResult.buildError(ResultCode.PARA_ERROR.code(), "获取手机号失败");
            }

            // 3. 根据手机号查询或创建用户
            TBaseUserEntity user = tbaseUserService.getUserByLoginuNum(phoneNumber);

            boolean isFirstLogin = false;
            if (user == null) {
                // 用户不存在，创建新用户
                user = new TBaseUserEntity();
                user.setLoginName(phoneNumber);
                user.setPhone(phoneNumber);
                user.setUserType(ao.getUserType() != null ? ao.getUserType() : 2); // 默认为学生(家长)
                user.setApproveStatus(0); // 待审批
                user.setIsValid(1);
                user.setStatus(1);
                tbaseUserService.save(user);
                isFirstLogin = true;
                log.info("微信小程序登录创建新用户，手机号：{}，openid：{}", phoneNumber, openid);
            }

            // 4. 将 openid 和手机号的映射关系缓存到 Redis（有效期30天）
            String openidCacheKey = "wx_openid:" + openid;
            redisCache.set(openidCacheKey, phoneNumber, 30 * 24 * 3600);
            String phoneCacheKey = "wx_phone:" + phoneNumber;
            redisCache.set(phoneCacheKey, openid, 30 * 24 * 3600);

            // 5. 检查用户状态
            if (user.getIsValid() != null && user.getIsValid() == 0) {
                return JsonResult.buildError(ResultCode.IS_DISABLED.code(), "账号不存在");
            }
            if (user.getStatus() != null && user.getStatus() == 0) {
                return JsonResult.buildError(ResultCode.IS_DISABLED.code(), "账号已被禁用");
            }

            // 6. 生成 token
            EmployeeDTO employeeDTO = SmartBeanUtil.copy(user, EmployeeDTO.class);
            String token = loginTokenService.generateToken(employeeDTO);

            // 7. 构建返回结果
            WxMiniProgramLoginVO loginVO = new WxMiniProgramLoginVO();
            loginVO.setAccessToken(token);
            loginVO.setUserId(user.getId());
            loginVO.setUserType(user.getUserType());
            loginVO.setPhone(user.getPhone());
            loginVO.setActualName(user.getActualName());
            loginVO.setIsFirstLogin(isFirstLogin);
            loginVO.setApproveStatus(user.getApproveStatus());

            // 8. 缓存用户登录信息
            String loginType = LoginTypeEnum.USER_WECHAT_TYPE.getCode();
            LoginDetailVO loginDetailVO = SmartBeanUtil.copy(user, LoginDetailVO.class);
            loginDetailVO.setXAccessToken(token);
            // 将 LoginDetailVO 转换为 JSONObject 后缓存
            cacheService.setLoginUserCache(loginType, phoneNumber, JSON.parseObject(JSON.toJSONString(loginDetailVO)));

            log.info("微信小程序手机号登录成功，手机号：{}，用户ID：{}，是否首次登录：{}",
                    phoneNumber, user.getId(), isFirstLogin);

            return JsonResult.buildSuccess(loginVO);

        } catch (Exception e) {
            log.error("微信小程序手机号登录异常", e);
            return JsonResult.buildError(ResultCode.SYS_ERROR.code(), "登录失败，请稍后重试");
        }
    }

    /**
     * 通过 code 获取 openid 和 session_key
     *
     * @param code 微信登录凭证
     * @return 包含 openid 和 session_key 的 JSON 对象
     */
    private JSONObject code2Session(String code) {
        try {
            String url = WX_CODE2SESSION_URL
                    .replace("{appid}", appid)
                    .replace("{secret}", secret)
                    .replace("{code}", code);

            String result = restTemplate.getForObject(url, String.class);
            log.info("微信 code2Session 响应：{}", result);

            JSONObject jsonObject = JSON.parseObject(result);
            
            // 检查是否有错误
            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode != null && errcode != 0) {
                    String errmsg = jsonObject.getString("errmsg");
                    log.error("微信 code2Session 失败，errcode：{}，errmsg：{}", errcode, errmsg);
                    return null;
                }
            }

            return jsonObject;
        } catch (Exception e) {
            log.error("调用微信 code2Session 异常", e);
            return null;
        }
    }

    /**
     * 获取用户手机号
     *
     * @param phoneCode 手机号获取凭证
     * @return 手机号
     */
    private String getUserPhoneNumber(String phoneCode) {
        try {
            // 1. 获取 access_token
            String accessToken = getAccessToken();
            if (StringUtils.isEmpty(accessToken)) {
                log.error("获取微信 access_token 失败");
                return null;
            }

            // 2. 获取手机号
            String url = WX_GET_PHONE_URL.replace("{access_token}", accessToken);
            
            JSONObject requestParam = new JSONObject();
            requestParam.put("code", phoneCode);

            String result = restTemplate.postForObject(url, requestParam.toString(), String.class);
            log.info("微信获取手机号响应：{}", result);

            JSONObject jsonObject = JSON.parseObject(result);
            
            // 检查是否有错误
            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode != null && errcode != 0) {
                    String errmsg = jsonObject.getString("errmsg");
                    log.error("微信获取手机号失败，errcode：{}，errmsg：{}", errcode, errmsg);
                    return null;
                }
            }

            // 解析手机号
            JSONObject phoneInfo = jsonObject.getJSONObject("phone_info");
            if (phoneInfo != null) {
                return phoneInfo.getString("phoneNumber");
            }

            return null;
        } catch (Exception e) {
            log.error("获取微信用户手机号异常", e);
            return null;
        }
    }

    /**
     * 获取微信 access_token
     *
     * @return access_token
     */
    private String getAccessToken() {
        try {
            // 先从 Redis 缓存中获取
            String cacheKey = "wx_access_token";
            Object cachedTokenObj = redisCache.get(cacheKey);
            String cachedToken = cachedTokenObj != null ? cachedTokenObj.toString() : null;
            if (StringUtils.isNotEmpty(cachedToken)) {
                return cachedToken;
            }

            // 从微信服务器获取
            String url = WX_ACCESS_TOKEN_URL
                    .replace("{appid}", appid)
                    .replace("{secret}", secret);

            String result = restTemplate.getForObject(url, String.class);
            log.info("微信获取 access_token 响应：{}", result);

            JSONObject jsonObject = JSON.parseObject(result);
            
            if (jsonObject.containsKey("access_token")) {
                String accessToken = jsonObject.getString("access_token");
                Integer expiresIn = jsonObject.getInteger("expires_in");
                
                // 缓存 access_token（提前5分钟过期）
                if (StringUtils.isNotEmpty(accessToken) && expiresIn != null) {
                    redisCache.set(cacheKey, accessToken, expiresIn - 300);
                    log.info("微信 access_token 缓存成功，有效期：{}秒", expiresIn);
                }

                return accessToken;
            }

            return null;
        } catch (Exception e) {
            log.error("获取微信 access_token 异常", e);
            return null;
        }
    }
}
