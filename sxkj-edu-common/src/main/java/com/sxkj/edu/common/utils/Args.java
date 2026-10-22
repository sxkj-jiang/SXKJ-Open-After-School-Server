/**
 *
 */
package com.sxkj.edu.common.utils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 *@Description : 参数校验操作工具类
 *@Author : JIANGJJ
 *@Date : 2022-12-27 11:22
 *@Company : SXKJ
 *@Version : 1.0
 **/
public final class Args {

    /**
     * 检查条件是否成里（为true），如果不成立，则根据传入的提示信息抛出异常。
     *
     * @param condition [boolean] 待判断条件
     * @param format    [String] 输出信息模板
     * @param args      [Object[]] 模板中占位符的具体内容
     */
    public static final void check(boolean condition, String format, Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    /**
     * 判断字符串是否为空，字符串为null或全为空白符该方法都会返回true。
     *
     * @param str [String] 待判断字符串
     * @return [boolean] 如果传入字符串为空则该方法返回true，反之则返回false。
     */
    public static final boolean isEmpty(String str) {
        return str == null || str.trim().length() <= 0;
    }

    public static final boolean isEmptyBoolean(Boolean bs) {
        return bs == null || bs == false;
    }

    /**
     * 判断数组是否为空，传入参数为null或数组长度为0该方法都会返回true。
     *
     * @param array [T[]] 待判断数组
     * @return [boolean] 如果传入数组为空则该方法返回true，反之则返回false。
     */
    public static final <T> boolean isEmpty(T[] array) {
        return array == null || array.length <= 0;
    }

    /**
     * 判断集合是否为空，传入参数为null或集合长度为0该方法都会返回true。
     *
     * @param col [{@code Collection<?>}] 待判断集合
     * @return [boolean] 如果传入集合为空则该方法返回true，反之则返回false。
     */
    public static final <T> boolean isEmpty(Collection<?> col) {
        return col == null || col.size() <= 0;
    }

    /**
     * 判断Map容器是否为空，传入参数为null或容器内元素个数为0该方法都会返回true。
     *
     * @param map [{@code Map<?, ?>}] 待判断容器
     * @return [boolean] 如果传入容器为空则该方法返回true，反之则返回false。
     */
    public static final <T> boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() <= 0;
    }

    /**
     * 判断字符串是否不为空，字符串为null或全为空白符该方法都会返回false。
     *
     * @param str [String] 待判断字符串
     * @return [boolean] 如果传入字符串为空则该方法返回false，反之则返回true。
     */
    public static final boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static final boolean isNotEmptyDate(Date date) {
        return null != date;
    }

    /**
     * 判断数组是否不为空，传入参数为null或数组长度为0该方法都会返回false。
     *
     * @param array [T[]] 待判断数组
     * @return [boolean] 如果传入数组为空则该方法返回false，反之则返回true。
     */
    public static final <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断集合是否不为空，传入参数为null或集合元素个数为0该方法都会返回false。
     *
     * @param col [{@code Collection<?>}] 待判断集合
     * @return [boolean] 如果传入集合为空则该方法返回false，反之则返回true。
     */
    public static final <T> boolean isNotEmpty(Collection<?> col) {
        return !isEmpty(col);
    }

    /**
     * 判断Map容器是否不为空，传入参数为null或容器内元素个数为0该方法都会返回
     * false。
     *
     * @param map [{@code Map<?, ?>}] 待判断容器
     * @return [boolean] 如果传入容器为空则该方法返回false，反之则返回true。
     */
    public static final <T> boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断特定对象是否为null。
     *
     * @param obj [Object] 待判断对象
     * @return [boolean] 如果对象为null该方法将返回true，反之返回false。
     */
    public static final boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 判断特定对象是否不为null。
     *
     * @param obj [Object] 待判断对象
     * @return [boolean] 如果对象为null该方法将返回false，反之返回true。
     */
    public static final boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * 检查跟定目标数值是否在指定区间范围内，如果不在，则根据传入的提示信息抛
     * 出异常。
     *
     * @param target [int] 待判断数值
     * @param min    [int] 判断区间最小值（包含）
     * @param max    [int] 判断区间最大值（不包含）
     * @param name   [String] 待判断数值描述名称
     */
    public static final void requireBetween(int target, int min, int max, String name) {
        if (target < min || max <= target) {
            throw new IllegalArgumentException(String.format(
                    "%s is invalid which must be between in [%d, %d), but not %s.", name, min, max, target));
        }
    }

    /**
     * 检查跟定目标数值是否在指定区间范围内，如果不在，则根据传入的提示信息抛
     * 出异常。
     *
     * @param target [long] 待判断数值
     * @param min    [long] 判断区间最小值（包含）
     * @param max    [long] 判断区间最大值（不包含）
     * @param name   [String] 待判断数值描述名称
     */
    public static final void requireBetween(long target, long min, long max, String name) {
        if (target < min || max <= target) {
            throw new IllegalArgumentException(String.format(
                    "%s is invalid which must be between in [%d, %d), but not %s.", name, min, max, target));
        }
    }

    /**
     * 断言传入参数必须为false。
     *
     * @param condition [Boolean] 待判断参数条件
     * @param name      [String] 输出异常信息中的名称
     */
    public static final void requireFalse(Boolean condition, String name) {
        if (condition != false) {
            throw new IllegalArgumentException(String.format("%s must be false.", name));
        }
    }

    /**
     * 断言传入参数不能为空。
     *
     * @param array [double[]] 待判断参数
     * @param name  [String] 输出异常信息中的名称
     */
    public static final void requireNotEmpty(double[] array, String name) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException(String.format("%s may not be empty.", name));
        }
    }

    /**
     * 断言传入参数不能为空。
     *
     * @param array [Object[]] 待判断参数
     * @param name  [String] 输出异常信息中的名称
     */
    public static final void requireNotEmpty(Object[] array, String name) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException(String.format("%s may not be empty.", name));
        }
    }

    /**
     * 断言传入参数不能为空。
     *
     * @param str  [String] 待判断参数
     * @param name [String] 输出异常信息中的名称
     */
    public static final void requireNotEmpty(String str, String name) {
        if (str == null || str.trim().length() <= 0) {
            throw new IllegalArgumentException(String.format("%s may not be empty.", name));
        }
    }

    /**
     * 判断参数是否不为null，如果为null则抛出异常。
     *
     * @param obj  [Object] 待判断参数
     * @param name [String] 输出异常信息中的名称
     */
    public static final void requireNotNull(Object obj, String name) {
        if (obj == null) {
            throw new NullPointerException(String.format("%s may not be null.", name));
        }
    }

    /**
     * 断言传入参数必须为true。
     *
     * @param condition [Boolean] 待判断参数条件
     * @param name      [String] 输出异常信息中的名称
     */
    public static final void requireTrue(Boolean condition, String name) {
        if (condition != true) {
            throw new IllegalArgumentException(String.format("%s must be true.", name));
        }
    }

}
