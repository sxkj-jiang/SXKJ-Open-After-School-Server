package com.sxkj.edu.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.sxkj.edu.common.base.constants.CoreConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author jiangjianjun
 * @version 1.0
 * @Description 全局统一日期工具类
 * @Classname DateUtils
 * @company SXKJ
 * @Date 2019/12/26 18:50
 */
@Slf4j
public class DateUtils {

    /**
     * yyyyMM
     */
    public static final String YM = "yyyyMM";

    /**
     * yyyy-MM
     */
    public static final String YYYYMM = "yyyy-MM";

    /**
     * yyyy年MM月
     */
    public static final String YEARMONTH = "yyyy年MM月";

    /**
     * yyyy年
     */
    public static final String YEAR = "yyyy年";

    /**
     * yyyy
     */
    public static final String YEAR_01 = "yyyy";


    /**
     * yyyyMMdd
     */
    public static final String YMD = "yyyyMMdd";

    /**
     * yyyy/MM/dd
     */
    public static final String YMD_XG = "yyyy/MM/dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    public static final String YMD_DASH_WITH_DATE = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd
     */
    public static final String YMD_DASH_WITH_DATE_D = "yyyy.MM.dd";
    public static final String YMD_DASH_WITH_DATE_D1 = "yyyy年MM月dd日";

    /**
     * HH:mm:ss
     */
    public static final String YMD_DASH_WITH_SECOND = "HH:mm:ss";

    /**
     * yyyyMMddHHmmss
     */
    public static final String YMD_WITH_TIME = "yyyyMMdd HH:mm:ss";
    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public static final String YMD_WITH_TIME_T = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String YMD_WITH_TIME_T_NS = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * yyyyMMddHHmmssMh
     */
    public static final String YMD_WITH_TIME_MH = "yyyyMMddHH:mm:ss";

    /**
     * yyyyMMddHHmm
     */
    public static final String YMD_TIME_ZERO = "yyyyMMddHHmm";

    public static final String YMD_TIME_HMS = "yyyyMMddHHmmss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YMD_DASH_WITH_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_DASH_WITH_TIME_MIN = "yyyy-MM-dd HH:mm";
    public static final String YMD_DASH_WITH_TIME_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String YMD_DASH_WITH_TIME_NO_SS = "yyyy-MM-dd HH:mm";

    /**
     * yyyy-MM-dd HH
     */
    public static final String YMD_DASH_WITH_TIME_NO_MM_SS = "yyyy-MM-dd HH";

    /**
     * yyyy_MM_dd HH:mm:ss
     */
    public static final String YMD_SLASH_WITH_TIME = "yyyy_MM_dd HH:mm:ss";

    public static String[] pattern = new String[]{"yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd",
            "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS"};

    /**
     * 根据指定的时间格式来将Date转化为字符串（线程安全）
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return DateFormatUtils.format(date, pattern);
    }

    public static String subYMD(String date) {
        String ymd = null;
        if (date == null) {
            return null;
        }
        ymd = date.substring(0, 10);
        return ymd;
    }

    /**
     * localDateTime -> date
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * date -> localDateTime
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * date -> localDateTime
     */
    public static Date queryTime(String date, boolean isStart) {
        if (isStart) {
            return cn.hutool.core.date.DateUtil.parseDateTime(date + " 00:00:00");
        } else {
            return cn.hutool.core.date.DateUtil.parseDateTime(date + " 23:59:59");
        }
    }

    /**
     * 字符串转时间Date
     *
     * @param source
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String source, String pattern) throws ParseException {
        Date date = null;
        if (StringUtils.isEmpty(source)) {
            return date;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(source);
    }


    /**
     * 字符串转时间Date
     *
     * @param source
     * @return
     * @throws ParseException
     */
    public static Date parse(String source) throws ParseException {
//    return org.apache.commons.lang.time.DateUtils.parseDate(source, pattern);
        return org.apache.commons.lang3.time.DateUtils.parseDateStrictly(source, pattern);
    }

    public static String getCurrDate(String dateFormat) {
        Date date = new Date();
//    DateFormat df = new SimpleDateFormat(dateFormat);
//    return df.format(date);
        return format(date, dateFormat);
    }

    // -----------------获取指定日期的年份，月份，日份，小时，分，秒，毫秒----------------------------

    /**
     * 获取指定日期的年份最后2位
     *
     * @param p_date util.Date日期
     * @return int 年份
     */
    public static String getYearOfDateLast2(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        String year = String.valueOf(c.get(Calendar.YEAR));
        return year.substring(year.length() - 2);
    }

    /**
     * 获取指定日期的年份
     *
     * @param p_date util.Date日期
     * @return int 年份
     */
    public static int getYearOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取指定日期的年份
     *
     * @param p_date util.Date日期
     * @return int 年份
     */
    public static String getYearStr(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.YEAR) + "";
    }

    /**
     * 获取指定日期的月份
     *
     * @param p_date util.Date日期
     * @return int 月份
     */
    public static int getMonthOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定日期的日份
     *
     * @param p_date util.Date日期
     * @return int 日份
     */
    public static int getDayOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定日期的小时
     *
     * @param p_date util.Date日期
     * @return int 日份
     */
    public static int getHourOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取指定日期的分钟
     *
     * @param p_date util.Date日期
     * @return int 分钟
     * @author zhuqx
     * @Date: 2006-10-31
     */
    public static int getMinuteOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取指定日期的秒钟
     *
     * @param p_date util.Date日期
     * @return int 秒钟
     */
    public static int getSecondOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param p_date util.Date日期
     * @return long 毫秒
     */
    public static long getMillisOfDate(Date p_date) {
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.getTimeInMillis();
    }

    /**
     * 是否开始日期在结束日期之前(不包括相等)
     *
     * @param p_startDate
     * @param p_endDate
     * @return boolean 在结束日期前:ture;否则：false
     */
    public static boolean isStartDateBeforeEndDate(Date p_startDate, Date p_endDate) {
        long l_startTime = getMillisOfDate(p_startDate);
        long l_endTime = getMillisOfDate(p_endDate);
        return (l_endTime - l_startTime > (long) 0) ? true : false;
    }

    /**
     * 计算指定时间戳与当前系统时间戳的毫秒差值
     *
     * @param l_startTime 开始时间戳
     * @return
     */
    public static long getMillisOfTowDiffDate(long l_startTime) {
        long l_endTime = System.currentTimeMillis();
        long millis = l_endTime - l_startTime;
        return millis;
    }

    /**
     * 获取2个日期的毫秒差
     *
     * @param l_startTime
     * @param l_endTime
     * @return
     */
    public static long getMillisOfTowDiffDate(long l_startTime, long l_endTime) {
        long millis = l_endTime - l_startTime;
        return millis;
    }


    /**
     * 获取2个日期的秒数差
     *
     * @param p_startDate
     * @param p_endDate
     * @return
     */
    public static long getSecondsOfTowDiffDate(Date p_startDate, Date p_endDate) {
        long l_startTime = getMillisOfDate(p_startDate);
        long l_endTime = getMillisOfDate(p_endDate);
        long betweenSeconds = (long) ((l_endTime - l_startTime) / 1000);
        return betweenSeconds;
    }

    /**
     * date转long
     *
     * @param p_startDate
     * @return
     */
    public static long setDateToLong(Date p_startDate) {
        long l_startTime = getMillisOfDate(p_startDate);
        return l_startTime;
    }

    /**
     * 获取2个日期的分钟差
     *
     * @param l_startTime
     * @param l_endTime
     * @return
     */
    public static long getMinutesOfTowDiffDate(long l_startTime, long l_endTime) {
        long betweenMinutes = (long) ((l_endTime - l_startTime) / (1000 * 60));
        return betweenMinutes;
    }

    /**
     * 获取2个日期的天数差，不足0.5天算0.5，大于0.5算一天
     *
     * @param leaveStartTime
     * @param leaveEndTime
     * @return
     */
    public static Double getMinutesOfTowDiffDay(Date leaveStartTime,Date leaveEndTime) {
        if(null == leaveStartTime || null == leaveEndTime){
            return 0d;
        }
        long diffMillis = leaveEndTime.getTime() - leaveStartTime.getTime();
        double exactDays = diffMillis / (1000.0 * 60 * 60 * 24);

        // 计算整数天数和小数部分
        long fullDays = (long) exactDays;
        double fractionalPart = exactDays - fullDays;

        // 应用舍入规则：不足0.5天算0.5，大于0.5天算一天
        double leaveDays;
        if (fractionalPart > 0.5) {
            leaveDays = fullDays + 1;
        } else if (fractionalPart > 0) {
            leaveDays = fullDays + 0.5;
        } else {
            leaveDays = fullDays;
        }
        return leaveDays;
    }

    /**
     * 时间戳转日期格式
     *
     * @param dateLong
     * @return
     */
    public static Date timeLongForDate(long dateLong) {
        Date d = null;
        DateFormat dateformat = new SimpleDateFormat(YMD_DASH_WITH_TIME);
        try {
            d = new Date(dateLong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return d;
    }

    /**
     * long转字符串
     *
     * @param dateLong
     * @return
     */
    public static String timeLongToStr(long dateLong) {
        String longToStr = null;
        Date d = null;
        try {
            if (dateLong > 0) {
                DateFormat dateformat = new SimpleDateFormat(YMD_DASH_WITH_TIME);
                d = new Date(dateLong);
                longToStr = format(d, YMD_DASH_WITH_TIME);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return longToStr;
    }

    /**
     * 获取2个日期的小时差
     *
     * @param p_startDate
     * @param p_endDate
     * @return
     */
    public static long getHoursOfTowDiffDate(Date p_startDate, Date p_endDate) {
        long l_startTime = getMillisOfDate(p_startDate);
        long l_endTime = getMillisOfDate(p_endDate);
        long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60));
        return betweenDays;
    }

    /**
     * 获取2个字符日期的天数差
     *
     * @param p_startDate
     * @param p_endDate
     * @return 天数差
     */
    public static long getDaysOfTowDiffDate(Date p_startDate, Date p_endDate) {
        long l_startTime = getMillisOfDate(p_startDate);
        long l_endTime = getMillisOfDate(p_endDate);
        long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60 * 24));
        return betweenDays;
    }

    /**
     * 获取2个字符日期的周数差
     *
     * @param p_startDate
     * @param p_endDate
     * @return 周数差
     */
    public static long getWeeksOfTowDiffDate(Date p_startDate, Date p_endDate) {
        return getDaysOfTowDiffDate(p_startDate, p_endDate) / 7;
    }

    /**
     * 获取2个字符日期的月数差
     *
     * @param p_startDate
     * @param p_endDate
     * @return 月数差
     */
    public static long getMonthsOfTowDiffDate(Date p_startDate, Date p_endDate) {
        return getDaysOfTowDiffDate(p_startDate, p_endDate) / 30;
    }

    /**
     * 获取指定日期与当前日期的月数差
     *
     * @param curr
     * @param aft  指定日期
     * @return
     */
    public static Boolean getMonthNumber(Date aft, Date curr) {
        Boolean bs = false;
        if (null == aft) {
            return bs;
        }
        long avg = getMonthsOfTowDiffDate(aft, curr);
        if (avg < 1) {
            bs = true;
        }
        return bs;
    }

    /**
     * 获取2个字符日期的年数差
     *
     * @param p_startDate
     * @param p_endDate
     * @return 年数差
     */
    public static long getYearsOfTowDiffDate(Date p_startDate, Date p_endDate) {
        return getDaysOfTowDiffDate(p_startDate, p_endDate) / 365;
    }

    /**
     * 在给定的日期基础上添加年，月，日、时，分，秒
     * 例如要再2006－10－21（uitl日期）添加3个月，并且格式化为yyyy-MM-dd格式，
     * 这里调用的方式为 addDate(2006－10－21,3,Calendar.MONTH,"yyyy-MM-dd")
     *
     * @param p_startDate 给定的日期
     * @param p_count     时间的数量
     * @param p_field     添加的域
     * @param p_format    时间转化格式，例如：yyyy-MM-dd hh:mm:ss 或者yyyy-mm-dd等
     * @return 添加后格式化的时
     */
    public static String addDate(Date p_startDate, int p_count, int p_field, String p_format) {

        // 年，月，日、时，分，秒
        int l_year = getYearOfDate(p_startDate);
        int l_month = getMonthOfDate(p_startDate) - 1;
        int l_day = getDayOfDate(p_startDate);
        int l_hour = getHourOfDate(p_startDate);
        int l_minute = getMinuteOfDate(p_startDate);
        int l_second = getSecondOfDate(p_startDate);
        Calendar l_calendar = new GregorianCalendar(l_year, l_month, l_day, l_hour, l_minute, l_second);
        l_calendar.add(p_field, p_count);
        return format(l_calendar.getTime(), p_format);
    }

    public static Date addDate(Date p_startDate, int p_count, int p_field) {

        // 年，月，日、时，分，秒
        int l_year = getYearOfDate(p_startDate);
        int l_month = getMonthOfDate(p_startDate) - 1;
        int l_day = getDayOfDate(p_startDate);
        int l_hour = getHourOfDate(p_startDate);
        int l_minute = getMinuteOfDate(p_startDate);
        int l_second = getSecondOfDate(p_startDate);
        Calendar l_calendar = new GregorianCalendar(l_year, l_month, l_day, l_hour, l_minute, l_second);
        l_calendar.add(p_field, p_count);
        return l_calendar.getTime();
    }

    /**
     * 向前2小时
     *
     * @param date
     * @return
     */
    public static String getBeforeNHours(Date date, Integer hours) {
        String string = DateUtils.addDate(date, -hours, Calendar.HOUR_OF_DAY, DateUtils.YMD_DASH_WITH_TIME);
        return string;
    }

    /**
     * 获取当前日期的年月日
     * @param date
     * @return
     */
    public static String formatYmd(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, YMD_DASH_WITH_DATE);
    }

    /**
     * 向前N天
     *
     * @param date
     * @return
     */
    public static String getBeforeNDays(Date date, Integer N) {
        String string = DateUtils.addDate(date, -N, Calendar.DAY_OF_MONTH, DateUtils.YMD_DASH_WITH_TIME);
        return string;
    }

    /**
     * 向前N天
     *
     * @param date
     * @return
     */
    public static String getBeforeNDaysYMD(Date date, Integer N) {
        String string = DateUtils.addDate(date, -N, Calendar.DAY_OF_MONTH, DateUtils.YMD_DASH_WITH_DATE);
        return string;
    }


    /**
     * 向后N天
     *
     * @param date
     * @return
     */
    public static String getAfterNDays(Date date, int count) {
        String string = DateUtils.addDate(date, count, Calendar.DAY_OF_MONTH, DateUtils.YMD_DASH_WITH_TIME);
        return string;
    }

    /**
     * 向后N天
     *
     * @param date
     * @return
     */
    public static String getAfterNDays(Date date, int count, String format) {
        String string = DateUtils.addDate(date, count, Calendar.DAY_OF_MONTH, format);
        return string;
    }

    /**
     * 向前15天
     *
     * @param date
     * @return
     */
//    public static String getBefore15Days(Date date) {
//        return string;
//    }

    /**
     * 向前30天(小时) - 1个月
     *
     * @param date
     * @return
     */
//    public static String getBefore30DaysHour(Date date) {
//        return string;
//    }

    /**
     * 时间转时间戳
     *
     * @param dateStr
     * @return
     */
    public static long forDateTimeinMills(String dateStr) {
        long time = 0l;
        DateFormat dateformat = new SimpleDateFormat(YMD_DASH_WITH_TIME);
        Date date;
        try {
            if (StringUtils.isNotBlank(dateStr)) {
                date = dateformat.parse(dateStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                time = date.getTime();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }

    /**
     * 时间戳转时间字符串
     *
     * @param dateLong
     * @return
     */
    public static String timeinMillsForDate(long dateLong) {
        String res = null;
        DateFormat dateformat = new SimpleDateFormat(YMD_DASH_WITH_TIME);
        try {
            Date d = new Date(dateLong);
            res = dateformat.format(d);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 判断是否是周末
     *
     * @param now
     * @return
     */
    public static Boolean ifWeekend(Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    /**
     * 根据当前日期年份+入参的月日组成 年月日
     *
     * @param day
     * @param month
     * @return
     */
    public static String contactYmd(String day, String month) {
        int year = getYearOfDate(new Date());
        String ymd = null;
        ymd = year + "-" + month + "-" + day;
        return ymd;
    }

    /**
     * 将日期转成东8区的日期格式
     *
     * @param date
     * @return
     */
    public static String contactStrForD8Date(String date) throws ParseException {
        String curr = "";
        if (StringUtils.isNotEmpty(date)) {
            String bef = date.substring(0, 10);
            String aft = date.substring(11, date.length());
            curr = bef + "T" + aft + "+08:00";
        }
        return curr;
    }

    /**
     * 将东8区的时间转成日期
     *
     * @param date
     * @return
     */
    public static Date contactDateForStr(String date) throws ParseException {
        Date curr = null;
        if (StringUtils.isEmpty(date)) {
            return new Date();
        }
        String sub = null, format = null, slan = "";
        if (date.indexOf("-") > -1) {
            sub = date.substring(0, 19);
            format = YMD_DASH_WITH_TIME;
            slan = " ";
        } else {
            sub = date.substring(0, 17);
            format = YMD_WITH_TIME_MH;
        }
        if (sub.indexOf("T") > -1) {
            sub = sub.replace("T", slan);
        }
        curr = parse(sub, format);

        return curr;
    }

    /**
     * 根据日期显示月日：Apr 10
     *
     * @param dateYMD
     * @return
     * @throws Exception
     */
    public static String changeYMDtoUk(String dateYMD) throws Exception {
        if (StringUtils.isEmpty(dateYMD)) {
            return "";
        }
        // 月日为MMMdd,如果是年，则为MMMddyyyy
        SimpleDateFormat sdf = new SimpleDateFormat("MMMdd", Locale.UK);
        Date curr = parse(dateYMD);
        return sdf.format(curr);
    }


    /**
     * 截取当前时间，精确到分钟
     *
     * @return
     */
    public static JSONObject getDateForSfm() {
        JSONObject jsonObject = new JSONObject();
        String start, end, curr;
        curr = format(new Date(), YMD_TIME_ZERO);
        start = curr + "00";
        end = curr + "59";
        jsonObject.put("start", start);
        jsonObject.put("end", end);
        return jsonObject;
    }

    public static String convertDateFormatStr(String sourceDate) throws Exception {
        String res = null;
        if (StringUtils.isEmpty(sourceDate)) {
            return res;
        }
        Date curr = parse(sourceDate, YMD_XG);
        if (null != curr) {
            String currStr = format(curr, YMD_DASH_WITH_TIME);
            res = currStr;
        }
        return res;
    }

    /**
     * 获取同一天在某段时间内的周次
     *
     * @param startTime
     * @param endTime
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static Integer getWeek(String startTime, String endTime, String dateTime) throws ParseException {
        Date beginDate = DateUtil.parse(startTime);
        Date endDate = DateUtil.parse(endTime);
        Date date = DateUtil.parse(dateTime);
        int week = 1;
        if (DateUtil.isIn(date, beginDate, endDate)) {
            Date endDateOfWeek = DateUtil.endOfWeek(beginDate);
            while (!DateUtil.isIn(date, beginDate, endDateOfWeek)) {
                week = week + 1;
                endDateOfWeek = DateUtil.endOfWeek(DateUtil.offsetDay(endDateOfWeek, 1));
            }
        }
        return week;
    }


    /**
     * 获取当前月的所有周
     */
    public List<String> monthWeekNum() {
        // DateUtil是hutool的类
        DateTime end = DateUtil.endOfMonth(new Date()); // 获取本月最后一天
        int weekNum = DateUtil.weekOfMonth(end); // 获取本月最后一天在第几周
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= weekNum; i++) {
            list.add("第" + i + "周");
        }
        return list;
    }

    /**
     * 根据指定的第几周，获取这周的开始、结束时间
     *
     * @param week 第几周（比如第1周、第2周）
     */
    public static String[] getStartEndOfWeek(String week) {
        // 如果没有传week，则默认查询当前周的开始、结束时间
        if (StringUtils.isEmpty(week)) {
            String[] result = new String[2];
            Date date = new Date();
            // 获取本周的开始时间和结束时间
            result[0] = DateUtil.beginOfWeek(date).toString("yyyy-MM-dd");
            result[1] = DateUtil.endOfWeek(date).toString("yyyy-MM-dd");
            return result;
        }
        int weekNo = Integer.parseInt(week.replace("第", "").replace("周", ""));
        String weekOfDate = getWeekOfDate(weekNo);
        return weekOfDate.split(",");
    }

    /**
     * 指定周数，获取当前月本周的开始、结束时间
     *
     * @param weekNo 第几周
     */
    public static String getWeekOfDate(int weekNo) {
        Date date = new Date();
        DateTime start = DateUtil.beginOfMonth(date); // 获取本月的第一天
        DateTime end = DateUtil.endOfMonth(date); // 获取本月最后一天
        String weekOfDate = getWeekOfDate(start, end, weekNo);
        return weekOfDate;
    }

    /**
     * 指定时间计算有几周，并返回每周起止日期
     *
     * @param start  开始时间（本月第一天）
     * @param end    结束时间（本月最后一天）
     * @param weekNo 第几周
     */
    public static String getWeekOfDate(Date start, Date end, int weekNo) {
        Map<Integer, String> weekOfDate = getWeekOfDate(start, end);
        return weekOfDate.get(weekNo);
    }

    /**
     * 指定时间计算有几周，并返回每周起止日期（这里也可以不用放到map中，直接返回某一周的起止时间就行）
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return week：第几周 start：每周开始时间 end：每周结束时间
     */
    public static Map<Integer, String> getWeekOfDate(Date start, Date end) {
        Map<Integer, String> result = new HashMap<>();
        StringBuffer sb = null;
        assert end != null;
        assert start != null;
        int weekNum = DateUtil.weekOfMonth(end); // 获取本月最后一天在第几周
        System.out.println("指定日期所在月共有 " + weekNum + " 周");

        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(start);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        for (int i = 1; i <= weekNum; i++) {
            sb = new StringBuffer();
            if (i == 1) {//第一周以 start 开始
                sb.append(DateUtil.format(start, "yyyy-MM-dd"));
            } else {
                sb.append(DateUtil.format(cal.getTime(), "yyyy-MM-dd"));
            }
            sb.append(",");
            if (i == weekNum) {//最后一周以 end 结束
                sb.append(DateUtil.format(end, "yyyy-MM-dd"));
            } else {
                //设置这周的周日日期，1代表周日，取值范围1~7，设置1~7之外会从周日开始往前后推算，负前正后，DAY_OF_WEEK的日期变更范围只会是在当前日期的周
                cal.set(Calendar.DAY_OF_WEEK, 1);
                sb.append(DateUtil.format(cal.getTime(), "yyyy-MM-dd"));
            }
            result.put(i, sb.toString());
            //调用 org.apache.commons.lang.time.DateUtils 包下的方法
            //新增一天到下一周的开始日期
            cal.setTime(org.apache.commons.lang3.time.DateUtils.addDays(cal.getTime(), 1));
        }
        return result;
    }

    /**
     * 获取当前月某一周的所有日期
     *
     * @param week 第几周
     */
    public static List<String> weekDay(String week) {
        String[] day = getStartEndOfWeek(week);
        String[] split1 = day[0].split("-");
        String[] split2 = day[1].split("-");
        int start = Integer.parseInt(split1[2]);
        int end = Integer.parseInt(split2[2]);
        int month = Integer.parseInt(split1[1]);
        List<String> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(month + "月" + i + "日");
        }
        return list;
    }

    /**
     * 获取本周的开始、结束时间
     *
     * @return
     */
    public static JSONObject getWeekStartAndEndTime() {
        JSONObject jsonObject = new JSONObject();
        //获得本周一日期
        Date timesWeekmorning = DateUtil.lastWeek();
        String startTime = format(timesWeekmorning, YMD_DASH_WITH_DATE) + " 23:59:59";
        // 获取本周日
        String endTime = getAfterNDays(timesWeekmorning, 7, YMD_DASH_WITH_DATE) + " 23:59:59";

        jsonObject.put("monday", startTime);
        jsonObject.put("weekday", endTime);
        return jsonObject;
    }

    public static String utcTime(Date date) {
        DateFormat dateformat = new SimpleDateFormat(YMD_DASH_WITH_TIME_UTC);
        dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcTime = dateformat.format(date);
        return utcTime;
    }


/*    public static String dayOfWeek1(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return String.valueOf(dayOfWeek);
    }*/

    public static Date formatDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取下一天
     */
    public static Date nextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 打印指定日期是星期几
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String dayOfWeek(String date) {

        try {
            Date parse = parse(date);
            SimpleDateFormat format = new SimpleDateFormat("EEEE");
            String dayOfWeek = format.format(parse);
            return dayOfWeek;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static String dayOfWeek(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("EEEE",Locale.CHINA);
        String dayOfWeek = format.format(date);
        return dayOfWeek;

    }

    /**
     * 获取两个日期之间的所有日(字符串格式, 按日计算)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 获取两个日期之间的所有日(字符串格式, 按日计算)
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     * 获取两个日期之间的所有日期(字符串格式, 按日计算)
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDateStrRange(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        LocalDate temp = startDate;
        while (!temp.isAfter(endDate)) {
            dateList.add(temp.format(formatter));
            temp = temp.plusDays(1);
        }
        return dateList;
    }

    /**
     * Date 转 LocalDate（系统默认时区）
     */
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        // 1. Date转Instant
        Instant instant = date.toInstant();
        // 2. 绑定系统时区，转ZonedDateTime，再取日期
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 计算开始时间与结束时间的分钟差，判断是否大于30分钟
     *
     * @return true-时间差大于30分钟，false-时间差不大于30分钟
     */
    public static boolean isDurationGreaterThan45Minutes(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        long diffMillis = endTime.getTime() - startTime.getTime();
        long diffMinutes = diffMillis / (1000 * 60);
        return diffMinutes < CoreConstant.SUBJECT_MIN_MINUTES;
    }

    /**
     * 获取开始时间与结束时间的分钟差
     *
     * @return 分钟差值（endTime - startTime），若任一时间为null则返回0
     */
    public static long getDurationMinutes(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            return 0;
        }
        long diffMillis = endTime.getTime() - startTime.getTime();
        return diffMillis / (1000 * 60);
    }

    public static void main(String[] args) {
        try {
//            String curr = DateUtils.format(new Date(),YMD_DASH_WITH_TIME);
//            String startTime = DateUtils.format(DateUtil.lastWeek(),YMD_DASH_WITH_TIME);
//            String endTime = DateUtils.format(DateUtil.nextWeek(),YMD_DASH_WITH_TIME);
//            System.out.println(startTime);

            // System.out.println(getWeekStartAndEndTime());

            String s = dayOfWeek(new Date());
            System.out.println(s);
            String format = format(new Date(), YMD_DASH_WITH_DATE);
            Date parse = parse(format);
            System.out.println(dayOfWeek(parse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
