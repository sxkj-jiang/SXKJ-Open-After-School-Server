package com.sxkj.edu.module.system.login;

import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.enums.EmployeeStatusEnum;
import com.sxkj.edu.common.base.enums.JudgeEnum;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.bo.EmployeeBO;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.privilege.service.PrivilegeEmployeeService;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class LoginTokenService {


    /**
     * jwt加密字段
     */
    private static final String CLAIM_ID_KEY = "id";

    //    @Value("${jwt.key}")
    private static String jwtKey = "sxkj-edu-jwt-key";


    private static TbaseUserService initUserService;
    private static PrivilegeEmployeeService privilegeEmployeeService;
    private static LoginService initLoginService;
    private static TBaseUserDao initUserDao;

    @Resource
    private TbaseUserService userService;

    @Resource
    private LoginService loginService;

    @Resource
    private PrivilegeEmployeeService privilegeEmployeeServiceComp;

    @Resource
    private TBaseUserDao userDao;


    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) throw new BaseBusinessException("获取用户凭证出错");
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //需要做token校验, 消息头的token优先于请求query参数的token
        String xHeaderToken = request.getHeader(CoreConstant.TOKEN_NAME);
        String xRequestToken = request.getParameter(CoreConstant.TOKEN_NAME);
        return null != xHeaderToken ? xHeaderToken : xRequestToken;
    }


    //判断是否为超管
    public static boolean isAdmin() {
        return privilegeEmployeeService.isSuperman(getEmployeeId());
    }

    // 查询用户角色信息  这里需要做个缓存
    public static List<RoleSelectedVO> listRole() {
        return initUserDao.selectByEmployee(getEmployeeId());
    }

    /**
     * 获取用户id
     * 注意token有效期后的异常问题
     * @return
     */
    public static Long getEmployeeId() {
        String xAccessToken = getToken();
        if (StringUtils.isEmpty(xAccessToken))  return null;
        Long employeeId = null;
        try {
            if (xAccessToken.contains(".")) {
                Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(xAccessToken).getBody();
                String idStr = claims.get(CLAIM_ID_KEY).toString();
                if (StringUtils.isNotEmpty(idStr)) {
                    employeeId = Long.valueOf(idStr);
                }
            }
        } catch (Exception e) {
            log.warn("getEmployeeTokenInfo error in timeout,token:{},", xAccessToken);
            throw new BaseBusinessException("获取用户凭证出错");
        }
        return employeeId;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static RequestTokenBO getEmployeeTokenInfo() {
        String xAccessToken = getToken();
        RequestTokenBO employeeTokenInfo = getEmployeeTokenInfo(xAccessToken);
        if (employeeTokenInfo == null) throw new BaseBusinessException("获取用户凭证出错");
        return employeeTokenInfo;
    }

    /**
     * 功能描述: 生成JWT TOKEN
     *
     * @param employeeDTO
     * @return
     * @auther jiangjj
     * @date 2018/9/12 0012 上午 10:08
     */
    public static String generateToken(EmployeeDTO employeeDTO) {
        Long id = employeeDTO.getId();
        /**将token设置为jwt格式*/
        String baseToken = UUID.randomUUID().toString();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeExpire = localDateTimeNow.plusSeconds(CoreConstant.USER_LOGIN_CACHE_EXPIRE);
        Date from = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        Date expire = Date.from(localDateTimeExpire.atZone(ZoneId.systemDefault()).toInstant());

        Claims jwtClaims = Jwts.claims().setSubject(baseToken);
        jwtClaims.put(CLAIM_ID_KEY, id);
        String compactJws = Jwts.builder().setClaims(jwtClaims).setNotBefore(from).setExpiration(expire).signWith(SignatureAlgorithm.HS512, jwtKey).compact();

        return compactJws;
    }

    /**
     * @Description : 根据登陆token获取登陆信息
     * @Author : JIANGJJ
     * @Date : 2025-5-8 11:56
     * @Company : SXKJ
     * @Version : 1.0
     **/
    public static RequestTokenBO getEmployeeTokenInfo(String token) {
        RequestTokenBO requestTokenBO = null;
        Long employeeId = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
                String idStr = claims.get(CLAIM_ID_KEY).toString();
                if (StringUtils.isNotEmpty(idStr)) {
                    employeeId = Long.valueOf(idStr);
                }
            }
        } catch (Exception e) {
            log.info("getEmployeeTokenInfo error in timeout,token:{},", token);
            return null;
        }
        if (null == employeeId) {
            return requestTokenBO;
        }

        EmployeeBO employeeBO = initUserService.getById(employeeId);
        if (employeeBO == null) {
            return null;
        }

        if (EmployeeStatusEnum.DISABLED.getValue().equals(employeeBO.getIsValid())) {
            return null;
        }

        if (JudgeEnum.YES.getValue().equals(employeeBO.getIsLeave())) {
            return null;
        }

        if (!JudgeEnum.YES.getValue().equals(employeeBO.getIsValid())) {
            return null;
        }
        return new RequestTokenBO(employeeBO);
    }

    /**
     * 获取用户token中的id与当前用户id是否一致，不一致直接访问无权限
     * @return
     */
    public static Boolean checkUserIsPass(){
        String xAccessToken = getToken();
        Boolean isPass = false;
        Long employeeId= null;
        try {
            if(StringUtils.isNotEmpty(xAccessToken)){
                Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(xAccessToken).getBody();
                String idStr = claims.get(CLAIM_ID_KEY).toString();
                if(StringUtils.isNotEmpty(idStr)){
                    employeeId = Long.valueOf(idStr);
                }
            }
        } catch (Exception e) {
            log.info("getEmployeeTokenInfo error in timeout,token:{},", xAccessToken);
            throw new BaseBusinessException("获取用户凭证出错");
        }
        if(null == employeeId) throw new BaseBusinessException("获取用户凭证出错");
        Long currUserId = SmartRequestTokenUtil.getRequestUserId();
        if(employeeId.equals(currUserId)){
            isPass = true;
        }
        return isPass;
    }


    @PostConstruct
    public void init() {
        initUserService = userService;
        initLoginService = loginService;
        privilegeEmployeeService = privilegeEmployeeServiceComp;
        initUserDao = userDao;
    }


}
