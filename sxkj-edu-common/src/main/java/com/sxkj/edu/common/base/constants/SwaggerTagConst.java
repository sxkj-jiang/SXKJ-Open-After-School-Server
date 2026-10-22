package com.sxkj.edu.common.base.constants;

/**
 * @Description : 角色标签
 * @Author : JIANGJJ
 * @Date : 2022-9-7 14:54
 * @Company : SXKJ
 * @Version : 1.0
 **/
public class SwaggerTagConst {


    public static class Admin {
        public static final String MANAGER_SYSTEM_CONFIG = "管理端-系统配置";

        public static final String MANAGER_USER = "管理端-用户";

        public static final String MANAGER_USER_LOGIN = "管理端-用户登录";

        public static final String MANAGER_DEPARTMENT = "管理端-部门";

        public static final String MANAGER_ROLE = "管理端-角色";

        public static final String MANAGER_ROLE_USER = "管理端-角色用户";

        public static final String MANAGER_ROLE_PRIVILEGE = "管理端-角色权限";

        public static final String MANAGER_USER_LOGIN_LOG = "管理端-用户登录日志";

        public static final String MANAGER_USER_OPERATE_LOG = "管理端-用户操作日志";

        public static final String MANAGER_DATA_SCOPE = "管理端-数据范围";


        public static final String MANAGER_HEART_BEAT = "通用-心跳服务";

        public static final String FILE_UPLOAD = "文件上传";

        public static final String FILE = "文件管理";


        public static final String MANAGER_DICT = "系统管理-字典管理";

        public static final String MULT_MANAGER = "综合管理";

        public static final String SYS_MANAGER_PRIVILEGE = "系统-动态权限";

        public static final String BURIAL_POINT = "埋点";

        public static final String SCHOOL_MANAGE = "学校管理";

    }

    /**
     * 课后服务
     */
    public static class KHFW {
        public static final String STU_MANAGE = "学生档案管理";
        public static final String TEACH_MANAGE = "教师档案管理";
        public static final String GRADE_MANAGEMENT = "课后服务-年级管理";
        public static final String CLASS_MANAGEMENT = "课后服务-班级管理";
        public static final String XQGL = "课后服务-校历管理";
        public static final String KHLB = "课后服务-课后类别";
        public static final String KCBGL = "课后服务-课程班管理";
        public static final String KCGL = "课后服务-课次管理";
        public static final String XSBM = "课后服务-学生报名";
        public static final String ZXKHGL = "课后服务-课表作息管理";

        public static final String JSPK = "课后服务-教师排课调课";
    }

    // 家长端
    public static class PARENT {
        public static final String PAR_ZHGL = "家长端综合管理";
        public static final String PAR_MAIN = "家长端首页";
        public static final String PAR_PERSON = "家长端我的";
    }

    // 教师端
    public static class TEACHER {
        public static final String TEA_QJGL = "教师端综合管理";
        public static final String TEA_YQXK = "教师端-邀请选课";
        public static final String TEA_MAIN = "教师端首页";
        public static final String TEA_PERSON = "教师端我的";

    }

    // 班主任端
    public static class TEA_LEADER {
        public static final String LEAD_ZHGL = "班主任端综合管理";
        public static final String LEAD_MAIN = "班主任端首页";
        public static final String LEAD_PERSON = "班主任端我的";
    }

    public static class MULT {

        public static final String MULT_ARTICLE = "文章管理";
        public static final String MULT_PROTOCOL = "协议管理";
        public static final String TEMPLATE = "模板管理";
        public static final String WORKBENCH = "工作台";
        public static final String EXAM_ROOM_MANAGEMENT = "综合-教室管理";
        public static final String BASE_CLASS_MANAGEMENT = "教务-自然班级管理";
        public static final String BUS_COMMON = "公共管理";
        public static final String SCHEDULE_TASK = "定时任务测试";
        public static final String SUBJECT_MANAGER = "学科管理";

    }

    public static class WP {
        public static final String WP_MANAGER = "网盘管理";
        public static final String WP_SHARE = "网盘-文件分享";
        public static final String WP_RECOVERY = "网盘-回收站";
        public static final String WP_TAG = "网盘-标签管理";
        public static final String ZSKGL = "知识库";
    }


    public static class STATISTICS {
        public static final String STATISTICS_MANAGER = "统计报表";
    }

}
