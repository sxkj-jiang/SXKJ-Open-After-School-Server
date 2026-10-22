package com.sxkj.edu.config;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.enums.LoginTypeEnum;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.utils.DateUtils;
import com.sxkj.edu.common.utils.SmartBigDecimalUtil;
import com.sxkj.edu.common.utils.SmartDigestUtil;
import com.sxkj.edu.module.system.login.domain.LoginGrobInfoVO;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description : 公共业务组装类
 * @Author : jjj$
 * @Date : $ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Slf4j
public final class AssembleUtil {

    private static String sessionGrobKey = "X-info";

    /**
     * jwt加密字段
     */
    private static final String CLAIM_ID_KEY = "id";

    //    @Value("${jwt.key}")
    private static String jwtKey = "sxkj-edu-jwt-key";

    /**
     * @Description : 从全局参数里获取账号
     * @Author : JIANGJJ
     * @Date : 2023-3-7 14:21
     * @Company : SXKJ
     * @Version : 1.0
     **/
    public static LoginGrobInfoVO getSysGrobInfo() {
        LoginGrobInfoVO loginGrobInfoVO = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grobInfo = request.getHeader(sessionGrobKey);
        if (StringUtils.isNotEmpty(grobInfo) && grobInfo.contains("loginType")) {
            loginGrobInfoVO = JSONObject.parseObject(grobInfo, LoginGrobInfoVO.class);
        }
        return loginGrobInfoVO;
    }

    public static String getSysGrobInfoByType() {
        String type = "";
        LoginGrobInfoVO loginGrobInfoVO = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grobInfo = request.getHeader(sessionGrobKey);
        if (StringUtils.isNotEmpty(grobInfo) && grobInfo.contains("loginType")) {
            loginGrobInfoVO = JSONObject.parseObject(grobInfo, LoginGrobInfoVO.class);
            if (null != loginGrobInfoVO) type = loginGrobInfoVO.getLoginType();
        }
        return type;
    }

    /**
     * @Description : 从全局参数里获取账号信息 {“X-INFO”:{"loginType":"JWPC","userType":1}}
     * @Author : JIANGJJ
     * @Date : 2023-3-7 14:21
     * @Company : SXKJ
     * @Version : 1.0
     **/
    public static String getSysGrobTypeInfo() {
        LoginGrobInfoVO loginGrobInfoVO = null;
        String loginType = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grobInfo = request.getHeader(sessionGrobKey);
        if (StringUtils.isNotEmpty(grobInfo) && grobInfo.contains("loginType")) {
            loginGrobInfoVO = JSONObject.parseObject(grobInfo, LoginGrobInfoVO.class);
            loginType = loginGrobInfoVO.getLoginType() == null ? LoginTypeEnum.MANAGER_TYPE.getCode() : loginGrobInfoVO.getLoginType();
        }else{
            loginType = LoginTypeEnum.MANAGER_TYPE.getCode();
        }
        return loginType;
    }

    /**
     * 新增用户加密
     *
     * @param loginPwd
     * @param defaultPwd
     * @return
     */
    public static String getUserEncrptPwd(String loginPwd, String defaultPwd) {
        String pwd = null;
        if (StringUtils.isBlank(loginPwd)) {
            pwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY,defaultPwd);
        } else {
            pwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, loginPwd);
        }
        return pwd;
    }



    public static String getUserAgentLoginType() {
        String str = "";
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grobInfo = request.getHeader(sessionGrobKey);
        if (StringUtils.isNotEmpty(grobInfo) && grobInfo.contains("loginType")) {
            str = JSONObject.parseObject(grobInfo).getString("loginType");
        }
        return str;
    }

    /**
     * 生成 学生-座位号的映射关系
     *
     * @param stuList
     * @return
     */
    public static Map<Long, Integer> getStuZnpkMap(List<Long> stuList) {
        Map<Long, Integer> stuMap = new HashMap<>();
        Collections.shuffle(stuList);
        int grace = 0;
        for (Long su : stuList) {
            grace++;
            stuMap.put(su, grace);
        }
        return stuMap;
    }

    /**
     * 生成8位随机考号
     *
     * @return
     */
    public static String getStuScoreNum() {
        Random rd = new Random();
        int num = rd.nextInt(100000000);
        return num + "";
    }

    /**
     * 生成8位随机数字，用于毕业证上的文书编号
     *
     * @return
     */
    public static Long getRandomNum() {
        Random rd = new Random();
        int num = rd.nextInt(100000000);
        return new Long(num);
    }

    /**
     * 获取性别类型转换
     *
     * @return
     */
    public static Integer getStuSexDetail(String sexStr) {
        Integer sex = 0;
        if (StringUtils.equals(sexStr, "男")) {
            sex = 0;
        } else if (StringUtils.equals(sexStr, "女")) {
            sex = 1;
        }
        return sex;
    }



    /**
     * 生成考场-老师映射，老师的集合数量应该等于考场集合的数量
     *
     * @param roomList
     * @param teachList
     * @return
     */
    public static Map<Long, Long> getZnpkToRoomTeachMap(List<Long> roomList, List<Long> teachList) {
        Map<Long, Long> stuMap = new HashMap<>();
        Collections.shuffle(teachList);
        for (int s = 0; s < roomList.size(); s++) {
            stuMap.put(roomList.get(s), teachList.get(s));
        }
        return stuMap;
    }

    /**
     * 考试计划的初始状态设置：根据当前时间计算 状态，当前时间大于等于开始时间，小于等于结束时间，状态为进行中-1
     * 当前时间<开始时间，状态为待开始-2
     * 大于结束时间为已结束-3
     * 4-草稿状态
     *
     * @return
     */
    public static Integer getExamPlanByDate(Date bmStart, Date bmEnd) {
        Integer status = 0;

        Long currLong = DateUtils.setDateToLong(new Date());
        Long examStart = DateUtils.setDateToLong(bmStart);
        Long examEnd = DateUtils.setDateToLong(bmEnd);
        if (currLong < examStart) {
            status = 2;
        } else if (examStart <= currLong && currLong <= examEnd) {
            status = 1;
        } else if (currLong > examEnd) {
            status = 3;
        } else {
            status = 4;
        }

        return status;
    }

    /**
     * 获取文件地址里的文件名
     *
     * @param name
     * @return
     */
    public static String substrFileUrlByNamePrefix(String name) {
        String prefix = null;
        if (StringUtils.isEmpty(name)) return prefix;
        int length = name.length();
        int lastidx = name.lastIndexOf("/");
        prefix = name.substring(lastidx + 1, length);
        return prefix;
    }

    /**
     * 考勤状态码转中文描述（公共方法，供Service层多处复用）
     *
     * @param status 考勤状态码：1到课 2迟到 3早退 4缺勤 5请假
     * @return 中文描述
     */
    public static String getAttendanceStatusDesc(Integer status) {
        if (status == null) return null;
        switch (status) {
            case 1: return "正常/到课";
            case 2: return "迟到";
            case 3: return "早退";
            case 4: return "缺勤";
            case 5: return "请假";
            default: return "未知";
        }
    }

    /**
     * 预警等级码转中文描述（公共方法，供Service层多处复用）
     *
     * @param level 预警等级：1正常 2预警 3紧急
     * @return 中文描述
     */
    public static String getWarningLevelDesc(Integer level) {
        if (level == null) return null;
        switch (level) {
            case 1: return "普通";
            case 2: return "预警";
            case 3: return "紧急";
            default: return "未知";
        }
    }

    public static String getHandleStatusDesc(Integer status) {
        if (status == null) return null;
        switch (status) {
            case 0: return "未处理";
            case 1: return "处理中";
            case 2: return "已处理";
            case 3: return "已关闭";
            default: return "未知";
        }
    }


    /**
     * 学校学段转换
     * @param stage
     * @return
     */
    public static Integer operStudentStage(String stage) {
        Integer numStage = 1;
        if(StringUtils.isEmpty(stage)) return numStage;
        if (StringUtils.equals(stage,"初中")) {
            numStage = 2;
        } else if (StringUtils.equals(stage,"高中")) {
            numStage = 3;
        }

        return numStage;
    }


    /**
     * 获取学籍状态
     *
     * @param term
     * @return
     */
    public static String getStuStatusName(Integer term) {
        String name = "";
        if (term == 0) {
            name = "在籍";
        } else if (term == 2) {
            name = "肄业";
        } else if (term == 1) {
            name = "已毕业";
        }
        return name;
    }

    /**
     * 获取学历名称 学历：学历层次：1-中专 2-大专 3-本科 4-硕士研究生 5-博士研究生
     *
     * @param xlcc
     * @return
     */
    public static Integer getStudyTypeName(String xlcc) {
        Integer type = 3;
        if(StringUtils.isNotBlank(xlcc)){
            if(xlcc.trim().contains("大专") || xlcc.trim().contains("专科")){
                type = 2;
            }else if(xlcc.trim().contains("中专")){
                type = 1;
            }else if(xlcc.trim().contains("硕士研究生")){
                type = 4;
            }else if(xlcc.trim().contains("博士研究生")){
                type = 5;
            }
        }
        return type;
    }

    public static String getGradeNoToName(String gradeName) {
        String type = "01";
        if(StringUtils.isNotBlank(gradeName)){
            switch (gradeName) {
                case "一年级":
                    type = "01";
                    break;
                case "二年级":
                    type = "02";
                    break;
                case "三年级":
                    type = "03";
                    break;
                case "四年级":
                    type = "04";
                    break;
                case "五年级":
                    type = "05";
                    break;
                case "六年级":
                    type = "06";
                    break;
                case "七年级":
                    type = "07";
                    break;
                case "八年级":
                    type = "08";
                    break;
                case "九年级":
                    type = "09";
                    break;
            }
        }
        return type;
    }

    /**
     * 导出公共样式处理
     *
     * @return
     */
    public static HorizontalCellStyleStrategy exportCommonOperDetail() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteFont.setFontHeightInPoints((short) 12);
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 设置单元格上下左右边框为细边框
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;

    }



    // 从学号里截取班级
    public static String subStudentForClassNum(String studentId) {
        String classNum = "";
        if(StringUtils.isNotBlank(studentId) && studentId.length() == 12){
            classNum = studentId.substring(studentId.length()-6,studentId.length()-4);
        }else{
            classNum = studentId.substring(studentId.length()-4,studentId.length()-2);
        }
        return classNum;
    }

    /**
     * 获取文件上传后缀
     *
     * @param name
     * @return
     */
    public static String substrFilesPrefix(String name) {
        String prefix = null;
        if (StringUtils.isEmpty(name)) return prefix;
        int length = name.length();
        int lastidx = name.indexOf(".");
        String type = name.substring(lastidx + 1, length);
        // 去掉.
        if (StringUtils.isNotEmpty(type) && type.contains(".")) {
            prefix = type.replace(".", "");
        } else {
            prefix = type;
        }
        if (StringUtils.isNotEmpty(prefix)) {
            prefix = prefix.toLowerCase(Locale.ROOT);
        }
        return prefix;
    }

    /**
     * 文件追加序号
     *
     * @param name
     * @return
     */
    public static String appendCopyFileName(String name) {
        String append = null;
        if (StringUtils.isEmpty(name)) return append;
        String extend = substrFilesPrefix(name);
        // 去掉.
        if (StringUtils.isNotEmpty(name) && name.contains(".")) {
            String fileLast = name.substring(0, name.indexOf("."));
            int currIdx = 1;
            append = fileLast + "(" + currIdx + ")" + "." + extend;
        }
        return append;
    }

    /**
     * 流量大小转为KB/MB
     * @param size
     * @return
     */
    public static String numberConvertFlow(Long size) {
        String sizeStr = "0";
        long currSizeGb = 1024 * 1024 * 1024;
        long currSizeMb = 1024 * 1024;
        long currSizeKb = 1024;
        if(null == size) return sizeStr;
        String sizeRes = null;
        if (size > currSizeGb) {
            sizeRes = SmartBigDecimalUtil.divide(BigDecimal.valueOf(size),BigDecimal.valueOf(currSizeGb),0).setScale(0, RoundingMode.HALF_UP).toString();
            sizeStr = sizeRes + "Gb";
        }else if (size > currSizeMb) {
            sizeRes = SmartBigDecimalUtil.divide(BigDecimal.valueOf(size),BigDecimal.valueOf(currSizeMb),0).setScale(0,RoundingMode.HALF_UP).toString();
            sizeStr = sizeRes + "Mb";
        }else if (size > 1024) {
            sizeRes = SmartBigDecimalUtil.divide(BigDecimal.valueOf(size),BigDecimal.valueOf(currSizeKb),0).setScale(0, RoundingMode.HALF_UP).toString();
            sizeStr = sizeRes + "Kb";
        }
        return sizeStr;
    }

    /**
     * 获取文件类型名称
     * @param type
     * @return
     */
    public static String getFileTypeName(Integer type) {
        String typeName = null;
        switch (type){
            case 1:
                typeName = "图片";
                break;
            case 2:
                typeName = "文档";
                break;
            case 3:
                typeName = "视频";
                break;
            case 4:
                typeName = "音频";
                break;
            case 5:
                typeName = "压缩文件";
                break;
            default:
                typeName = "其他文件";
                break;
        }
        return typeName;
    }

    /**
     * 返回角色类型优先级，管理员》学校管理员》教师》学生
     *
     * @param roleList
     * @return
     */
    public static Integer getUserTypeByRoleList(List<RoleEntity> roleList) {
        Integer type = 2;
        Set<Integer> typeList = roleList.stream().map(f -> f.getRoleType()).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(typeList)) {
            if (typeList.contains(0)) {
                type = 0;
            }else if (typeList.contains(3)) {
                type = 3;
            } else if (typeList.contains(1)) {
                type = 1;
            } else if (typeList.contains(4)) {
                type = 4;
            }
        }
        return type;
    }


    /**
     * 返回角色用户类型映射
     *
     * @param roleId
     * @return
     */
    public static Integer getUserTypeByRoleId(Long roleId) {
        Integer type = 2;
        if (roleId == 1) {
            type = 0;
        }else if (roleId == 2) {
            type = 1;
        } else if (roleId == 3) {
            type = 2;
        } else if (roleId == 4) {
            type = 3;
        } else if (roleId == 5) {
            type = 4;
        } else if (roleId == 6) {
            type = 5;
        }
        return type;
    }

    public static Integer getUserStatusByRegistStatus(Integer status) {
        if(null == status) return null;
        Integer userStatus = 1;
        if (status == 3 || status == 0) {
            userStatus = 0;
        }else  {
            userStatus = status;
        }
        return userStatus;
    }

    /**
     * 用户类型与角色代码映射
     *
     * @param userType
     * @return
     */
    public static String getRoleCodeByUserType(Integer userType) {
        String roleCode = null;
        if (userType == 0) {
            roleCode = RoleEnum.PORTAL_ROLE_ADMIN.getCode();
        } else if (userType == 1) {// 校友学生
            roleCode = RoleEnum.PORTAL_ROLE_STU.getCode();
        } else if (userType == 2) {// 校友教职工
            roleCode = RoleEnum.PORTAL_ROLE_TEACHER.getCode();
        }
        return roleCode;
    }

    /**
     * 截取字符串返回数组，截取英文逗号
     *
     * @param name
     * @return
     */
    public static List<String> spitNameForList(String name,String split) {
        List<String> prefixList = new ArrayList<>();
        if (StringUtils.isEmpty(name)) return prefixList;
        if (name.contains(split)) {
            String[] arrs = name.split(split);
            prefixList = new ArrayList<>(Arrays.asList(arrs));
        } else {
            prefixList.add(name);
        }
        return prefixList;
    }

    /**
     * 获取性别类型转换
     * @return
     */
    public static String getStuSexForInteger(Integer sex){
        String sexStr = "男";
        if(sex == 1){
            sexStr = "女";
        }
        return sexStr;
    }

    /**
     * @Description : 根据登陆token获取登陆信息
     * @Author : JIANGJJ
     * @Date : 2025-5-8 11:56
     * @Company : SXKJ
     * @Version : 1.0
     **/
    public static Boolean checkTokenInValid(String token) {
        Long employeeId = null;
        Boolean isTrue = false;
        try {
            if (StringUtils.isNotEmpty(token)) {
                Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
                String idStr = claims.get(CLAIM_ID_KEY).toString();
                if (StringUtils.isNotEmpty(idStr)) {
                    employeeId = Long.valueOf(idStr);
                }
            }
        } catch (Exception e) {
            log.warn("call checkTokenInValid check in false,token:{}",token);
            return false;
        }
        if(null != employeeId){
            isTrue = true;
        }
        return isTrue;
    }


    public static Integer getMenuHideResult(Boolean hide){
        Integer hideInt = 0;
        if(null != hide && hide){
            hideInt = 1;
        }
        return hideInt;
    }

    public static Boolean getMenuHideForBooleanResult(Integer req){
        Boolean hideInt = false;
        if(null != req && req == 1){
            hideInt = true;
        }
        return hideInt;
    }

    /**
     * 日期拼接返回字符
     * @param start
     * @param end
     * @return
     */
    public static String getDateContactString(Date start,Date end){
        return DateUtils.format(start, DateUtils.YMD_DASH_WITH_DATE) + " - " + DateUtils.format(end,  DateUtils.YMD_DASH_WITH_DATE);
    }

    /**
     * 根据课程ID和会话ID判断是否包含课程id
     * @param courseIdJson
     * @param sessionId
     * @return
     */
    public static Boolean checkAsLeaveByCourseId(String courseIdJson,Long sessionId){
        if(StringUtils.isEmpty(courseIdJson) || sessionId == null) return false;

        // 将JSON字符串转为List<Long>
        List<Long> sessionIds = JSON.parseArray(courseIdJson, Long.class);
        if(CollectionUtils.isNotEmpty(sessionIds) && sessionIds.contains(sessionId)){
            return true;
        }
        return false;
    }

    // ==================== 课后选课字典映射 ====================

    /**
     * 报名方式名称映射
     *
     * @param value 报名方式编码：1-公开选课 2-邀请选课 3-行政班安排
     * @return 中文名称
     */
    public static String getEnrollTypeName(Integer value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case 1: return "公开选课";
            case 2: return "邀请选课";
            case 3: return "行政班安排";
            default: return "未知";
        }
    }

    /**
     * 报名来源名称映射
     *
     * @param value 来源编码：1-家长公开报名 2-教师邀请 3-行政班安排 4-后台手动分配
     * @return 中文名称
     */
    public static String getEnrollSourceName(Integer value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case 1: return "家长公开报名";
            case 2: return "教师邀请";
            case 3: return "行政班安排";
            case 4: return "后台手动分配";
            default: return "未知";
        }
    }

    /**
     * 报名状态名称映射
     *
     * @param value 状态编码：0-待确认 1-已报名 2-已取消 3-已分配 4-未分配
     * @return 中文名称
     */
    public static String getEnrollStatusName(Integer value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case 0: return "待确认";
            case 1: return "已报名";
            case 2: return "已取消";
            case 3: return "已分配";
            case 4: return "未分配";
            default: return "未知";
        }
    }

    /**
     * 选课日志动作名称映射，管理端驳回复用取消动作并通过备注区分
     *
     * @param value  动作编码
     * @param remark 备注信息
     * @return 中文名称
     */
    public static String getEnrollmentActionName(Integer value, String remark) {
        if (value == null) {
            return null;
        }
        if (java.util.Objects.equals(value, CoreConstant.ENROLL_ACTION_CANCEL)
                && org.springframework.util.StringUtils.hasText(remark) && remark.startsWith("管理端驳回")) {
            return "管理端驳回";
        }
        switch (value) {
            case CoreConstant.ENROLL_ACTION_INIT: return "初始化";
            case CoreConstant.ENROLL_ACTION_PUBLIC_APPLY: return "公开报名";
            case CoreConstant.ENROLL_ACTION_INVITE_CONFIRM: return "邀请确认";
            case CoreConstant.ENROLL_ACTION_INVITE_REJECT: return "邀请拒绝";
            case CoreConstant.ENROLL_ACTION_MANUAL_ASSIGN: return "手动分配";
            case CoreConstant.ENROLL_ACTION_CHANGE_COURSE: return "改课";
            case CoreConstant.ENROLL_ACTION_CANCEL: return "取消";
            case CoreConstant.ENROLL_ACTION_STUDENT_ADJUST: return "学生调课";
            default: return "未知";
        }
    }


    /**
     * 非课程去向类型名称映射
     *
     * @param destinationType 去向类型编码：1-课程班 2-回家 3-校外机构 4-其他
     * @return 中文名称
     */
    public static String getDestinationTypeName(Integer destinationType) {
        if (java.util.Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_GO_HOME)) {
            return "回家";
        }
        if (java.util.Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION)) {
            return "校外机构";
        }
        if (java.util.Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_OTHER)) {
            return "其他";
        }
        return "课程班";
    }

    public static String toAvailableStatus(String reason) {
        if (!org.springframework.util.StringUtils.hasText(reason)) {
            return "available";
        }
        if (reason.contains("名额已满")) {
            return "full";
        }
        if (reason.contains("性别")) {
            return "gender_limited";
        }
        if (reason.contains("范围") || reason.contains("学期") || reason.contains("学校")) {
            return "not_scope";
        }
        return "disabled";
    }

    public static String headteacherStatusName(String tabType) {
        if (CoreConstant.TAB_REGISTERED.equals(tabType)) {
            return "已报名";
        }
        if (CoreConstant.TAB_ASSIGNED.equals(tabType)) {
            return "已分配";
        }
        return "未分配";
    }

    public static  int compareNullableLong(Long left, Long right) {
        if (Objects.equals(left, right)) {
            return 0;
        }
        if (left == null) {
            return 1;
        }
        if (right == null) {
            return -1;
        }
        return left.compareTo(right);
    }

    public static int compareNullableString(String left, String right) {
        if (Objects.equals(left, right)) {
            return 0;
        }
        if (!org.springframework.util.StringUtils.hasText(left)) {
            return 1;
        }
        if (!org.springframework.util.StringUtils.hasText(right)) {
            return -1;
        }
        return left.compareTo(right);
    }

    public static String dateWeekDayName(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }

    /**
     * 星期几数字转中文名称。
     */
    public static String getWeekDayName(Integer weekDay) {
        if (weekDay == null) {
            return null;
        }
        switch (weekDay) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
            default:
                return "周" + weekDay;
        }
    }

    /**
     * 服务状态码转名称。
     */
    public static String getServiceStatusName(Integer status) {
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_DRAFT)) {
            return "未发布";
        }
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_ENROLLING)) {
            return "报名中";
        }
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_RUNNING)) {
            return "进行中";
        }
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_FINISHED)) {
            return "已结束";
        }
        return "未知";
    }

    /**
     * 报名方式码转名称。
     */
    public static String getEnrollTypeNameStr(Integer enrollType) {
        if (Objects.equals(enrollType, CoreConstant.ENROLL_TYPE_PUBLIC)) {
            return "公开选课";
        }
        if (Objects.equals(enrollType, CoreConstant.ENROLL_TYPE_INVITE)) {
            return "邀请选课";
        }
        if (Objects.equals(enrollType, CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)) {
            return "行政班安排";
        }
        return "未知";
    }

    /**
     * 课程班状态码转名称。
     */
    public static String getClassStatusName(Integer status) {
        if (Objects.equals(status, CoreConstant.CLASS_STATUS_NOT_STARTED)) {
            return "未开始";
        }
        if (Objects.equals(status, CoreConstant.CLASS_STATUS_RUNNING)) {
            return "进行中";
        }
        if (Objects.equals(status, CoreConstant.CLASS_STATUS_FINISHED)) {
            return "已结束";
        }
        if (Objects.equals(status, 4)) {
            return "停用";
        }
        return "未知";
    }

    /**
     * 格式化时间为 HH:mm，null 返回 null。
     */
    public static String formatTime(Date time) {
        if (time == null) {
            return null;
        }
        return new SimpleDateFormat("HH:mm").format(time);
    }

    /**
     * 解析 HH:mm 格式的时间字符串，失败返回 null。
     */
    public static Date parseTime(String value) {
        if (!org.springframework.util.StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new SimpleDateFormat("HH:mm").parse(value.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将日期和纯时间合并为完整日期时间，用于设置 session_start_date / session_end_date。
     */
    public static Date combineDateAndTime(Date date, Date time) {
        if (date == null || time == null) {
            return null;
        }
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        Calendar result = Calendar.getInstance();
        result.clear();
        result.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
        result.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
        result.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
        result.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        result.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        result.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        result.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND));
        return result.getTime();
    }

    /**
     * 日期归一化到当天 00:00:00.000。
     */
    public static Date normalizeDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 日期加减天数。
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取星期几（1=周一 ... 7=周日）。
     */
    public static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY ? 7 : day - 1;
    }

    /**
     * 判断两个日期是否为同一天（归一化后比较）。
     */
    public static boolean sameDate(Date left, Date right) {
        return left != null && right != null && normalizeDate(left).equals(normalizeDate(right));
    }

    /**
     * 生成头调课请求编号
     */
    public static String generateHeadteacherRequestNo() {
        return "XSTK" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                + String.format("%04d", new Random().nextInt(10000));
    }

    public static List<Integer> parseWeekDays(String weekDaysJson) {
        if (!org.springframework.util.StringUtils.hasText(weekDaysJson)) {
            return Collections.emptyList();
        }
        String value = weekDaysJson.replace("[", "").replace("]", "").trim();
        if (!org.springframework.util.StringUtils.hasText(value)) {
            return Collections.emptyList();
        }
        List<Integer> weekDays = new ArrayList<>();
        for (String item : value.split(",")) {
            try {
                weekDays.add(Integer.valueOf(item.trim()));
            } catch (Exception ignored) {
                // 忽略脏数据中的无效星期配置，保留其它可解析项。
            }
        }
        return weekDays.stream().filter(Objects::nonNull).sorted().collect(Collectors.toList());
    }

    public static String weeklyRuleDayName(Integer weekDay) {
        if (weekDay == null) {
            return null;
        }
        String[] names = {"", "一", "二", "三", "四", "五", "六", "日"};
        return weekDay >= 1 && weekDay <= 7 ? names[weekDay] : null;
    }


    public static String statusDesc(Integer status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case CoreConstant.ADJUST_STATUS_PENDING:
                return "待审核";
            case CoreConstant.ADJUST_STATUS_APPROVED:
                return "已通过";
            case CoreConstant.ADJUST_STATUS_REJECTED:
                return "已驳回";
            case CoreConstant.ADJUST_STATUS_WITHDRAWN:
                return "已撤回";
            default:
                return String.valueOf(status);
        }
    }

    public static String weekDayName(Integer weekDay) {
        if (weekDay == null) {
            return "";
        }
        String[] names = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return weekDay >= 1 && weekDay <= 7 ? names[weekDay] : String.valueOf(weekDay);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }
    public static void main(String[] args) {
        String className =  "2601班";
        String classNo = className.replaceAll("班","").substring(2,4);
        String year = className.substring(0,2);
        String stuNoQuery = "20" + year + classNo + "22";
//        String classNo = "2601班".replaceAll("班","").substring(0,2);
        System.out.println(stuNoQuery);
    }
}
