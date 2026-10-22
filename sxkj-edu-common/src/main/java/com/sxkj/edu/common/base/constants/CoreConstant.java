package com.sxkj.edu.common.base.constants;

import java.util.*;

/**
 * 框架核心的一些常量定义
 *
 */
public class CoreConstant {

    /**
     * 默认时区
     */
    public static final String DEFAULT_TIME_ZONE = "Asia/Shanghai";

    /**
     * 统一的日期格式
     */
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    /**
     * 统一的日期时间格式
     */
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 统一的时间格式
     */
    public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";

    /**
     * 格林威治时间格式
     */
    public static final String GMT_FORMAT_PATTERN = "EEE MMM dd HH:mm:ss z yyyy";

    // 函授站状态
    public static final Map<Integer,String> CORR_MAP = new HashMap<>();

    // 考试类型
    public static final Map<Integer,String> SCORE_MAP = new HashMap<>();

    // 考试方式
    public static final Map<Integer,String> SCORE_MODULE_MAP = new HashMap<>();

    // 课程类型
    public static final Map<Integer,String> SUBJECT_TYPE_MAP = new HashMap<>();

    public static final String TOKEN_NAME = "x-access-token";

    public static final String edu_bus_base = "EDU_BUS_BASE";

    // redis key BASE-基础数据、BUS-业务，命名规范为: EDU_BASE_模块_KEY
    public static final String edu_bus_pk_code = "EDU_BUS_PK_CODE";

    // 课程班编号
    public static final String edu_bus_pk_code_class = "EDU_BUS_PK_CODE_CLASS";

    public static final String SHARE_PREFIX = "site/share/";

    public static final String edu_user_school = "EDU_USER_SCHOOL";

    public static final String Redis_logistics_ip_key = "logistics_ip_count_";

    // 删除、更新时忽略的菜单
    public static final List<String> NOT_DELETE_ROLE_PRIKEY = new ArrayList<>();


    //redis
    public static final String edu_base_njgl = "EDU_BASE_NJGL";
    public static final String edu_base_bjgl = "EDU_BASE_BJGL";
    public static final String edu_base_khlbgl = "EDU_BASE_KHLBGL";
    public static final String edu_base_xlgl = "EDU_BASE_XLGL";
    public static final String edu_base_dbrw = "EDU_BASE_DBRW";


    public static final int SERVICE_STATUS_DRAFT = 0;
    /** 报名中 */
    public static final int SERVICE_STATUS_ENROLLING = 1;
    /** 进行中 */
    public static final int SERVICE_STATUS_RUNNING = 2;
    /** 已结束 */
    public static final int SERVICE_STATUS_FINISHED = 3;

    /** 启用状态 */
    public static final int ENABLED_STATUS = 1;

    /** 学期启用状态 */
    public static final int TERM_STATUS_ENABLED = 1;

    /** 学生与业务学期不一致提示 */
    public static final String AS_STUDENT_TERM_MISMATCH_MESSAGE =
            "学生与业务不属于同一学期，请重新选择";

    /** 学校当前日期有效学期配置异常提示 */
    public static final String AS_ACTIVE_TERM_NOT_FOUND_MESSAGE =
            "学校当前日期不存在唯一有效学期，请联系管理员";

    /** 学生同学期学号重复提示 */
    public static final String AS_STUDENT_TERM_DUPLICATED_MESSAGE =
            "当前学期已存在该学号学生，请勿重复新增";

    /** 学生跨学期调课提示 */
    public static final String AS_ADJUST_CROSS_TERM_MESSAGE =
            "原去向与新去向不属于同一学期，请重新选择";

    /** 学生导入模式：仅新增 */
    public static final String AS_STUDENT_IMPORT_MODE_ADD_ONLY = "ADD_ONLY";

    /** 学生导入模式：更新已有 */
    public static final String AS_STUDENT_IMPORT_MODE_UPDATE_EXISTING =
            "UPDATE_EXISTING";

    /** 学生学期资料变更待生效 */
    public static final int AS_STUDENT_TERM_CHANGE_PENDING = 0;

    /** 学生学期资料变更已生效 */
    public static final int AS_STUDENT_TERM_CHANGE_APPLIED = 1;

    /** 学生学期资料变更生效失败 */
    public static final int AS_STUDENT_TERM_CHANGE_FAILED = 2;

    /** 学生学期资料变更正在生效 */
    public static final int AS_STUDENT_TERM_CHANGE_PROCESSING = 3;

    /** 教师注册审核通过状态 */
    public static final int TEACHER_REGISTER_STATUS_APPROVED = 1;

    /** 教师在职状态 */
    public static final int TEACHER_EMPLOYEE_STATUS_ACTIVE = 1;

    /** 课后时间方案类型 */
    public static final int SCHEDULE_TEMPLATE_TYPE_AFTER_SCHOOL = 2;

    /** 课后服务复制缓存键前缀 */
    public static final String AS_SERVICE_COPY_REDIS_PREFIX = "edu:after_school:service_copy:";

    /** 课后服务复制结果缓存时间，单位为秒 */
    public static final long AS_SERVICE_COPY_RESULT_CACHE_SECONDS = 7 * 24 * 60 * 60L;

    /** 课后服务复制锁等待时间，单位为秒 */
    public static final long AS_SERVICE_COPY_LOCK_WAIT_SECONDS = 3L;

    /** 家长公开报名分布式锁缓存键前缀 */
    public static final String AS_ENROLLMENT_APPLY_REDIS_PREFIX = "edu:after_school:enrollment_apply:";

    /** 家长公开报名分布式锁等待时间，单位为秒 */
    public static final long AS_ENROLLMENT_APPLY_LOCK_WAIT_SECONDS = 3L;

    public static final int ENROLL_TYPE_PUBLIC = 1;
    public static final int ENROLL_TYPE_INVITE = 2;
    public static final int ENROLL_TYPE_CLASS_ASSIGN = 3;

    public static final int CLASS_STATUS_NOT_STARTED = 1;
    public static final int CLASS_STATUS_RUNNING = 2;
    public static final int CLASS_STATUS_FINISHED = 3;

    public static final int RULE_TYPE_WEEKLY = 1;
    public static final int RULE_TYPE_DATE = 2;

    public static final int SESSION_STATUS_NOT_STARTED = 0;
    public static final int SESSION_STATUS_CANCELLED = 3;

    public static final int ENROLL_SOURCE_CLASS_ASSIGN = 3;
    public static final int ENROLL_SOURCE_MANUAL_ASSIGN = 4;

    public static final int ENROLL_STATUS_PENDING = 0;
    public static final int ENROLL_STATUS_ENROLLED = 1;
    public static final int ENROLL_STATUS_CANCELLED = 2;
    public static final int ENROLL_STATUS_ASSIGNED = 3;
    public static final int ENROLL_STATUS_UNASSIGNED = 4;

    public static final int ACTION_CLASS_ASSIGN = 6;

    /** 选课初始化未开始 */
    public static final int SELECTION_INIT_STATUS_NOT_STARTED = 0;
    /** 选课初始化进行中 */
    public static final int SELECTION_INIT_STATUS_PROCESSING = 1;
    /** 选课初始化完成状态 */
    public static final int SELECTION_INIT_STATUS_DONE = 2;

    /** 选课日志操作人：家长 */
    public static final int ENROLLMENT_OPERATOR_GUARDIAN = 1;
    /** 选课日志操作人：教师 */
    public static final int ENROLLMENT_OPERATOR_TEACHER = 2;
    /** 选课日志操作人：管理员 */
    public static final int ENROLLMENT_OPERATOR_ADMIN = 3;
    /** 选课日志操作人：系统 */
    public static final int ENROLLMENT_OPERATOR_SYSTEM = 4;

    /** 选课日志动作：初始化 */
    public static final int ENROLL_ACTION_INIT = 1;
    /** 选课日志动作：公开报名 */
    public static final int ENROLL_ACTION_PUBLIC_APPLY = 2;
    /** 选课日志动作：邀请确认 */
    public static final int ENROLL_ACTION_INVITE_CONFIRM = 4;
    /** 选课日志动作：邀请拒绝 */
    public static final int ENROLL_ACTION_INVITE_REJECT = 5;
    /** 选课日志动作：手动分配 */
    public static final int ENROLL_ACTION_MANUAL_ASSIGN = 7;
    /** 选课日志动作：换课 */
    public static final int ENROLL_ACTION_CHANGE_COURSE = 8;
    /** 选课日志动作：取消 */
    public static final int ENROLL_ACTION_CANCEL = 9;
    /** 选课日志动作：学生调课 */
    public static final int ENROLL_ACTION_STUDENT_ADJUST = 10;

    /** 学生注册审核待审核 */
    public static final int STUDENT_REGISTER_STATUS_PENDING = 0;
    /** 学生注册审核通过 */
    public static final int STUDENT_REGISTER_STATUS_APPROVED = 1;

    /** 考勤对象：学生 */
    public static final int ATTENDANCE_TARGET_STUDENT = 1;

    /** 已学课时考勤状态：正常、迟到、早退。 */
    public static final List<Integer> LEARNED_ATTENDANCE_STATUSES = Arrays.asList(1, 2, 3);

    /** 时间段类型：课后普通节次 */
    public static final int PERIOD_TYPE_AFTER_SCHOOL = 2;

    /** 调课通用状态：待处理 */
    public static final int ADJUST_STATUS_PENDING = 0;
    /** 调课通用状态：已通过 */
    public static final int ADJUST_STATUS_APPROVED = 1;
    /** 调课通用状态：已拒绝 */
    public static final int ADJUST_STATUS_REJECTED = 2;
    /** 调课通用状态：已撤回 */
    public static final int ADJUST_STATUS_WITHDRAWN = 3;

    /** 调课审核操作：同意 */
    public static final int ADJUST_HANDLE_APPROVE = 1;
    /** 调课审核操作：拒绝 */
    public static final int ADJUST_HANDLE_REJECT = 2;

    /** 学生调课操作人：管理员 */
    public static final int STUDENT_ADJUST_OPERATOR_ADMIN = 2;
    /** 学生调课动作：同意 */
    public static final int STUDENT_ADJUST_ACTION_APPROVE = 3;
    /** 学生调课动作：拒绝 */
    public static final int STUDENT_ADJUST_ACTION_REJECT = 4;
    /** 学生调课动作：撤回 */
    public static final int STUDENT_ADJUST_ACTION_WITHDRAW = 5;

    /** 教师调课操作人：教师 */
    public static final int TEACHER_ADJUST_OPERATOR_TEACHER = 1;
    /** 教师调课操作人：管理员 */
    public static final int TEACHER_ADJUST_OPERATOR_ADMIN = 2;
    /** 教师调课操作人：系统 */
    public static final int TEACHER_ADJUST_OPERATOR_SYSTEM = 3;

    /** 教师调课动作：提交 */
    public static final int TEACHER_ADJUST_ACTION_SUBMIT = 1;
    /** 教师调课动作：撤回 */
    public static final int TEACHER_ADJUST_ACTION_WITHDRAW = 2;
    /** 教师调课动作：同意 */
    public static final int TEACHER_ADJUST_ACTION_APPROVE = 3;
    /** 教师调课动作：拒绝 */
    public static final int TEACHER_ADJUST_ACTION_REJECT = 4;
    /** 教师调课动作：更新课次 */
    public static final int TEACHER_ADJUST_ACTION_UPDATE_SESSION = 5;
    /** 教师调课动作：作废 */
    public static final int TEACHER_ADJUST_ACTION_VOID = 6;

    /** 调课待办状态：待处理 */
    public static final int TODO_STATUS_PENDING = 0;
    /** 调课待办状态：已处理 */
    public static final int TODO_STATUS_HANDLED = 1;
    /** 调课待办状态：已忽略 */
    public static final int TODO_STATUS_IGNORED = 2;

    /** 调课课次状态：未开始 */
    public static final int ADJUST_SESSION_STATUS_NOT_STARTED = 0;
    /** 调课课次状态：进行中 */
    public static final int ADJUST_SESSION_STATUS_IN_PROGRESS = 1;
    /** 调课课次状态：已结束 */
    public static final int ADJUST_SESSION_STATUS_FINISHED = 2;
    /** 调课课次状态：已取消 */
    public static final int ADJUST_SESSION_STATUS_CANCELLED = 3;

    /** 学生调课去向：课程班 */
    public static final int DESTINATION_TYPE_COURSE_CLASS = 1;
    /** 学生调课去向：回家 */
    public static final int DESTINATION_TYPE_GO_HOME = 2;
    /** 学生调课去向：培训机构 */
    public static final int DESTINATION_TYPE_TRAINING_INSTITUTION = 3;
    /** 学生调课去向：其他 */
    public static final int DESTINATION_TYPE_OTHER = 4;

    /** 导入：表头行数 */
    public static final int IMPORT_HEADER_LINE_NUM = 1;
    /** 导入：节次上限 */
    public static final int IMPORT_MAX_SECTION_NO = 99;
    /** 导入：作息方案类型-排课 */
    public static final int IMPORT_TEMPLATE_TYPE_SCHEDULE = 1;
    /** 导入：时间段类型-普通节次 */
    public static final int IMPORT_PERIOD_TYPE_NORMAL = 1;
    /** 导入：服务状态-未发布 */
    public static final int IMPORT_SERVICE_STATUS_UNPUBLISHED = 0;
    /** 导入：课程班编码 Redis 序列号格式 */
    public static final String IMPORT_COURSE_CLASS_CODE_FORMAT = "%s%s%03d";
    /** 导入：课程班编码 Redis key 格式 */
    public static final String IMPORT_COURSE_CLASS_CODE_REDIS_KEY = "%s:%s";
    /** 发布无课次提示 */
    public static final String PUBLISH_NO_SESSION_MESSAGE = "未能生成任何课次，请检查排课规则和开课周期";

    /** 选课Tab：全部 */
    public static final String TAB_ALL = "all";
    /** 选课Tab：已报名 */
    public static final String TAB_REGISTERED = "registered";
    /** 选课Tab：已分配 */
    public static final String TAB_ASSIGNED = "assigned";
    /** 选课Tab：未分配 */
    public static final String TAB_UNASSIGNED = "unassigned";

    /** 邀请选课状态：已报名 */
    public static final String INVITE_ENROLLMENT_STATUS_ENROLLED = "enrolled";
    /** 邀请选课状态：未报名 */
    public static final String INVITE_ENROLLMENT_STATUS_NOT_ENROLLED = "not_enrolled";

    /** 占位补偿批大小 */
    public static final int PLACEHOLDER_COMPENSATION_BATCH_SIZE = 500;

    /** 邀请二维码尺寸 */
    public static final int QR_CODE_SIZE = 320;

    /** 选课来源：公开报名 */
    public static final int ENROLL_SOURCE_PUBLIC = 1;
    /** 选课来源：邀请 */
    public static final int ENROLL_SOURCE_INVITE = 2;

    public static final int OPERATOR_GUARDIAN = 1;
    public static final int OPERATOR_TEACHER = 2;
    public static final int OPERATOR_ADMIN = 3;
    public static final int OPERATOR_SYSTEM = 4;

    /** 邀请状态：有效 */
    public static final int INVITE_STATUS_ACTIVE = 1;
    /** 邀请状态：已停用 */
    public static final int INVITE_STATUS_DISABLED = 2;

    /** 邀请操作人：教师 */
    public static final int INVITE_OPERATOR_TEACHER = 2;
    /** 邀请日志动作：发起邀请 */
    public static final int INVITE_ACTION_INIT = 3;
    /** 邀请日志动作：取消 */
    public static final int INVITE_ACTION_CANCEL = 9;

    /** 教师邀请仅允许注册已通过的学生 */
    public static final List<Integer> STUDENT_INVITABLE_REGISTER_STATUSES =
            Collections.singletonList(STUDENT_REGISTER_STATUS_APPROVED);

    /** 评价打分精度 */
    public static final int EVALUATION_SCALE = 1;
    /** 评价题型：单选题 */
    public static final int EVALUATION_QUESTION_TYPE_SINGLE = 1;
    /** 评价题型：多选题 */
    public static final int EVALUATION_QUESTION_TYPE_MULTI = 2;
    /** 评价题型：文本题 */
    public static final int EVALUATION_QUESTION_TYPE_TEXT = 3;

    public static final int ACTION_STUDENT_ADJUST = 10;


    /** 班级分配学生注册状态列表 */
    public static final List<Integer> CLASS_ASSIGN_STUDENT_REGISTER_STATUSES =
            Arrays.asList(STUDENT_REGISTER_STATUS_PENDING, STUDENT_REGISTER_STATUS_APPROVED);

    /** 有效选课状态列表 */
    public static final List<Integer> ACTIVE_COURSE_ENROLL_STATUSES =
            Arrays.asList(ENROLL_STATUS_ENROLLED, ENROLL_STATUS_ASSIGNED);

    // 用户缓存token
    public static final Long USER_LOGIN_CACHE_EXPIRE = 259200L;

    // 用户缓存token
    public static final Long USER_LOGIN_PARENT_CACHE_EXPIRE = 85600L;

    public static final Map<Integer, String> weekDayNameMap = new HashMap<>();

    public static final String PARENT_MAP_KEY = "PARENT_MAP";
    public static final String PARENT_FIELD_PREFIX = "PARENT_";


    /**
     * 上传文件类型：视频文件类型限制
     */
    public static final List<String> UPLOAD_VIDEO_TYPES = new ArrayList<>();

    // 系统默认角色不允许操作
    public static final List<Long> SYS_DEFAULT_ROLE = new ArrayList<>();

    // 每节课分钟限制
    public static final Integer SUBJECT_MIN_MINUTES = 30;

    /**
     * 文件类型映射：key=文件后缀（小写），value=文件类型分类（视频/图片/文档）
     */
    public static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();


    static {
        CORR_MAP.put(1,"已审核");
        CORR_MAP.put(2,"待审核");
        CORR_MAP.put(3,"已拒绝");
        CORR_MAP.put(4,"草稿");

        SCORE_MAP.put(1,"期末正常考");
        SCORE_MAP.put(2,"期末补考");
        SCORE_MAP.put(3,"期末缓考");
        SCORE_MAP.put(4,"期末重修");
        SCORE_MAP.put(5,"小考");
        SCORE_MAP.put(6,"中考");
        SCORE_MAP.put(7,"随堂联系");
        SCORE_MAP.put(null,"其他");

        SCORE_MODULE_MAP.put(1,"线上");
        SCORE_MODULE_MAP.put(2,"线下");

        SUBJECT_TYPE_MAP.put(1,"必修");
        SUBJECT_TYPE_MAP.put(2,"选修");

        UPLOAD_VIDEO_TYPES.add("mp4");
        UPLOAD_VIDEO_TYPES.add("flv");

        NOT_DELETE_ROLE_PRIKEY.add("Workspace");
        NOT_DELETE_ROLE_PRIKEY.add("Business");
        NOT_DELETE_ROLE_PRIKEY.add("dashboard");

        weekDayNameMap.put(1, "周一");
        weekDayNameMap.put(2, "周二");
        weekDayNameMap.put(3, "周三");
        weekDayNameMap.put(4, "周四");
        weekDayNameMap.put(5, "周五");
        weekDayNameMap.put(6, "周六");
        weekDayNameMap.put(7, "周日");

        SYS_DEFAULT_ROLE.add(1L);
        SYS_DEFAULT_ROLE.add(2L);
        SYS_DEFAULT_ROLE.add(3L);
        SYS_DEFAULT_ROLE.add(4L);
        SYS_DEFAULT_ROLE.add(5L);
        SYS_DEFAULT_ROLE.add(6L);

        // ===== 视频 =====
        FILE_TYPE_MAP.put("mp4", "视频");
        FILE_TYPE_MAP.put("avi", "视频");
        FILE_TYPE_MAP.put("mov", "视频");
        FILE_TYPE_MAP.put("wmv", "视频");
        FILE_TYPE_MAP.put("flv", "视频");
        FILE_TYPE_MAP.put("mkv", "视频");
        FILE_TYPE_MAP.put("webm", "视频");
        FILE_TYPE_MAP.put("mpg", "视频");
        FILE_TYPE_MAP.put("mpeg", "视频");
        FILE_TYPE_MAP.put("m4v", "视频");
        FILE_TYPE_MAP.put("3gp", "视频");
        FILE_TYPE_MAP.put("rmvb", "视频");
        FILE_TYPE_MAP.put("rm", "视频");
        FILE_TYPE_MAP.put("ts", "视频");

        // ===== 图片 =====
        FILE_TYPE_MAP.put("jpg", "图片");
        FILE_TYPE_MAP.put("jpeg", "图片");
        FILE_TYPE_MAP.put("png", "图片");
        FILE_TYPE_MAP.put("gif", "图片");
        FILE_TYPE_MAP.put("bmp", "图片");
        FILE_TYPE_MAP.put("svg", "图片");
        FILE_TYPE_MAP.put("webp", "图片");
        FILE_TYPE_MAP.put("ico", "图片");
        FILE_TYPE_MAP.put("tiff", "图片");
        FILE_TYPE_MAP.put("tif", "图片");
        FILE_TYPE_MAP.put("psd", "图片");
        // ===== 文档 =====
        FILE_TYPE_MAP.put("pdf", "文档");
        FILE_TYPE_MAP.put("doc", "文档");
        FILE_TYPE_MAP.put("docx", "文档");
        FILE_TYPE_MAP.put("xls", "文档");
        FILE_TYPE_MAP.put("xlsx", "文档");
        FILE_TYPE_MAP.put("ppt", "文档");
        FILE_TYPE_MAP.put("pptx", "文档");
        FILE_TYPE_MAP.put("txt", "文档");
        FILE_TYPE_MAP.put("csv", "文档");
        FILE_TYPE_MAP.put("rtf", "文档");
        FILE_TYPE_MAP.put("wps", "文档");
        FILE_TYPE_MAP.put("wpt", "文档");
        FILE_TYPE_MAP.put("odt", "文档");
        FILE_TYPE_MAP.put("ods", "文档");
        FILE_TYPE_MAP.put("odp", "文档");
        FILE_TYPE_MAP.put("md", "文档");
        FILE_TYPE_MAP.put("json", "文档");
    }

}
