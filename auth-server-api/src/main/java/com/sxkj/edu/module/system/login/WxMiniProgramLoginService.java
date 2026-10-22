package com.sxkj.edu.module.system.login;

import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.system.login.domain.WxMiniProgramLoginVO;
import com.sxkj.edu.module.system.login.domain.WxMiniProgramPhoneLoginAO;

/**
 * 微信小程序登录 Service
 *
 * @author system
 * @date 2026-05-25
 */
public interface WxMiniProgramLoginService {

    /**
     * 微信小程序手机号一键登录
     *
     * @param ao 登录请求参数
     * @return 登录结果
     */
    JsonResult<WxMiniProgramLoginVO> phoneLogin(WxMiniProgramPhoneLoginAO ao);
}
