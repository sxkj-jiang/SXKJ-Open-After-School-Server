package com.sxkj.edu.common.base.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {

    /**
     * token为空
     */
    TOKEN_NULL("100"),

    /**
     * 有权限
     */
    SUCC_PRIVILEGE("101"),
    /**
     * 无权限
     */
    FAIL_PRIVILEGE("103"),

    /**
     * 有权限但是超时
     */
    TIMEOUT_PRIVILEGE("102"),

    /**
     * 平台管理-行业操作异常
     */
    PTGL_INDUSTER_ERROR("104"),

    /**
     * 平台管理-职业操作异常
     */
    PTGL_CAREER_ERROR("105"),

    /**
     * 平台管理-轮播图操作异常
     */
    PTGL_BGT_ERROR("110"),

    /**
     * 平台管理-底部链接操作异常
     */
    PTGL_LINK_ERROR("111"),
    /**
     * 学生管理
     */
    STU_MANAGER_ERROR("112"),
    /**
     * 教师管理
     */
    TEACH_MANAGER_ERROR("113"),

    /**
     * 教师排课
     */
    PTGL_JSPK_ERROR("114"),

    /**
     * 平台管理-学生选课
     */
    PTGL_XSXK_ERROR("115"),

    /**
     * 教师调课
     */
    PTGL_JSTK_ERROR("116"),

    /**
     * 学生调课
     */
    PTGL_XSTK_ERROR("117"),
    /**
     * 考勤管理
     */
    KQGL_ERROR("118"),

    /**
     * 学籍管理操作异常
     */
    XJGL_ERROR("119"),

    /**
     * 教学管理操作异常
     */
    JXGL_ERROR("120"),

    /**
     * 学生请假操作异常
     */
    XSQJ_ERROR("121"),

    /**
     * 教师管理操作异常
     */
    JSGL_ERROR("122"),



    /**
     * 基础管理-文件上传操作异常
     */
    BASE_FILE_ERROR("133"),



    /**
     * 试卷管理-取消
     */
    SJQX_ERROR("135"),

    /**
     * 毕业证书打印
     */
    ZSDY_ERROR("136"),



    /**
     * 教室操作异常
     */
    TETB_ERROR("139"),

    /**
     * 平台管理-文章管理新闻操作异常
     */
    PTGL_WZGLXW_ERROR("140"),

    /**
     * 平台管理-文章管理法律法规操作异常
     */
    PTGL_WZGLFL_ERROR("141"),

    /**
     * 平台管理-调查问卷操作异常
     */
    PTGL_DCWJ_ERROR("142"),

    /**
     * 平台管理-短信管理操作异常
     */
    PTGL_DXGL_ERROR("143"),
    /**
     * 模板管理
     */
    MBGL_ERROR("144"),


    /**
     * 成功
     */
    OK("200"),
    /**
     * 业务异常
     */
    PARA_ERROR("400"),

    /**
     * 认证错误
     */
    AUTH_ERROR("401"),

    /**
     * 用户停用
     */
    FORBIDDEN("403"),
    /**
     * 用户不存在
     */
    NOT_FOUND("404"),

    /**
     * 参数类型错误
     */
    PARA_CONVERT_ERROR("405"),


    /**
     * 超时
     */
    TIMEOUT("408"),

    /**
     * 参数异常
     */
    ERROR_PARAM("409"),

    /**
     * 数据不存在
     */
    NOT_EXISTS("410"),

    /**
     * 请求方式错误
     */
    REQUEST_METHOD_ERROR("411"),

    /**
     * JSON格式错误
     */
    JSON_FORMAT_ERROR("412"),

    /**
     * 操作异常，请联系管理员
     */
    OPER_ERROR("413"),

    /**
     * 失败次数过多，请稍后重试
     */
    AUTH_MORE_ERROR("443"),

    /**
     * 查无数据
     */
    QUERY_ERROR("444"),

    /**
     * 系统错误
     */
    SYS_ERROR("500"),

    /**
     * 数据已存在
     */
    DATA_EXIST("501"),

    /**
     * 100x 登录code
     * 您还未登录或登录失效，请重新登录
     */
    LOGIN_ERROR("1001"),

    /**
     * 100x 登录code
     * 对不起，您没有权限哦
     */
    NOT_HAVE_PRIVILEGES("1002"),

    /**
     * 300x 登录code
     */
    UPDATE_FAILED("3001"),

    /**
     * 部门信息不存在
     */
    DEPT_NOT_EXIST("3002"),


    /**
     * 账号已被禁用，不得登录系统
     */
    IS_DISABLED("3003"),

    /**
     * 用户已存在
     */
    USER_EXISTS("3004"),

    /**
     * 用户信息校验失败
     */
    VERIFICATION_USER_INVALID("3005"),

    /**
     * 验证码无效
     */
    VERIFICATION_CODE_INVALID("3006"),

    /**
     * 登录名不能为空
     */
    LOGIN_NAME_IS_NULL("3007"),

    /**
     * 姓名不能为空
     */
    ACTUAL_NAME_IS_NULL("3008"),

    /**
     * 手机号不能为空
     */
    PHONE_IS_NULL("3009"),


    /**
     * 您的账号正在审核, 请耐心等待
     */
    LOGIN_EMPLOYEE_APPROVE("3010"),

    /**
     * 用户名或密码错误
     */
    LOGIN_FAILED("3011"),

    /**
     * 您的账号审核未通过, 请联系管理员
     */
    LOGIN_EMPLOYEE_REJECT("3012"),

    /**
     * 企业编号不能为空
     */
    LOGIN_SCHOOL_IS_NULL("3013"),

    /**
     * 角色、权限菜单 code
     * 模块和页面的“功能Key”值不能重复
     */
    ROUTER_KEY_NO_REPEAT("4001"),

    /**
     * 权限菜单 code
     * 菜单不存在，清先保存菜单
     */
    MENU_NOT_EXIST("4002"),

    /**
     * 权限菜单 code
     * 功能点不存在，清先保存功能点
     */
    POINT_NOT_EXIST("4003"),

    /**
     * 角色名称已存在
     */
    ROLE_NAME_EXISTS("4004"),

    /**
     * 角色不存在
     */
    ROLE_NOT_EXISTS("4005"),

    /**
     * 部门管理菜单 500x
     * 部门不存在
     */
    DEPT_NOT_EXISTS("5001"),

    /**
     * 部门管理菜单 code
     * 当前部门有子级部门,无法删除
     */
    CANNOT_DEL_DEPARTMENT_WITH_CHILD("5002"),

    /**
     * 部门管理菜单 code
     * 当前部门有员工,无法删除
     */
    CANNOT_DEL_DEPARTMENT_WITH_EMPLOYEE("5003"),

    /**
     * 部门管理菜单 code
     * 上级部门id不能等于当前部门id
     */
    PARENT_ID_ERROR("5004"),
    DEPT_ID_ERROR("5005"),
    /**
     * 用户无此系统访问权限
     */
    USER_SYS_ERROR("5006");

    private static final Map<String, String> messageMap = new HashMap<>();

    static {
        messageMap.put("200", "成功");
        messageMap.put("400", "业务操作异常，请联系管理员");
        messageMap.put("401", "登录失败");
        messageMap.put("402", "用户停用");
        messageMap.put("403", "操作超时");
        messageMap.put("404", "用户不存在");
        messageMap.put("405", "参数类型异常");
        messageMap.put("409", "参数错误");
        messageMap.put("410", "数据不存在");
        messageMap.put("411", "请求方式错误");
        messageMap.put("412", "JSON格式错误");

        messageMap.put("443", "失败次数过多，请稍后重试");
        messageMap.put("444", "查无数据");
        messageMap.put("500", "系统错误");
        // 登录
        messageMap.put("1001", "您还未登录或登录失效，请重新登录");
        messageMap.put("1002", "对不起，您没有权限，请联系管理员授权");

        // 用户信息
        messageMap.put("3001", "用户更新失败");
        messageMap.put("3002", "部门信息不存在");
        messageMap.put("3003", "账号已被禁用");
        messageMap.put("3004", "用户已存在");
        messageMap.put("3005", "用户信息校验失败");
        messageMap.put("3006", "验证码无效");
        messageMap.put("3007", "登录名不能为空");
        messageMap.put("3008", "姓名不能为空");
        messageMap.put("3009", "手机号不能为空");
        messageMap.put("3010", "您的账号正在审核, 请耐心等待");
        messageMap.put("3011", "您的账号审核未通过, 请联系管理员");

        messageMap.put("4001", "模块和页面的“功能Key”值不能重复");
        messageMap.put("4002", "菜单不存在，清先保存菜单");
        messageMap.put("4003", "功能点不存在，清先保存功能点");
        messageMap.put("4004", "角色名称或编码已存在");
        messageMap.put("4005", "角色不存在");

        messageMap.put("5001", "部门不存在");
        messageMap.put("5002", "当前部门有子级部门,无法删除");
        messageMap.put("5003", "当前部门有员工,无法删除");
        messageMap.put("5004", "上级部门id不能等于当前部门id");
        messageMap.put("5005", "部门编号不能为空");
    }

    private final String code;

    ResultCode(String code) {
        this.code = code;
    }

    public static String getMapByCode(String code) {
        return messageMap.get(code);
    }

    public String code() {
        return code;
    }

}
