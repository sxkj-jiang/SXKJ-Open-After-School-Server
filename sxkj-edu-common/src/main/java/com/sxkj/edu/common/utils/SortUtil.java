package com.sxkj.edu.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 排序工具类
 *
 * @author jiangjianjun
 */
public class SortUtil {

    /**
     * 根据ip使用次数升序
     *
     * @param map
     */
    public static Map<String, Integer> sortIpMap(Map<String, Integer> map) {
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        if (map != null && map.size() > 0) {
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
            Collections.sort(entryList, new MapValueComparator());

            Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
            Map.Entry<String, Integer> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }

    /**
     * 根据时间对list进行排序
     *
     * @param list
     * @param date
     */
    public static void sortDetailList(List<JSONObject> list, String date) {
        if (list != null && list.size() > 0) {
            // 根据时间排序
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject b1, JSONObject b2) {
                    String obj1 = b1.getString(date);
                    String obj2 = b2.getString(date);
                    if (StringUtils.isNotBlank(obj1) && StringUtils.isNotBlank(obj2)) {
                        return b1.getString(date).compareTo(b2.getString(date));// 升序
                    } else {
                        return 0;
                    }
                }
            });
        }
    }


    /**
     * 根据时间以及次数对list进行排序
     *
     * @param list-排序对象
     * @param date-第一重排序
     * @param number-第二重排序
     */
    public static void sortDetailList(List<JSONObject> list, String date, String number) {
        if (list != null && list.size() > 0) {
            // 根据时间排序
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject b1, JSONObject b2) {
                    String obj1 = b1.getString(date);
                    String obj2 = b2.getString(date);
                    if (StringUtils.isNotBlank(obj1) && StringUtils.isNotBlank(obj2) && StringUtils.isNotEmpty(b1.getString(number)) && StringUtils.isNotEmpty(b2.getString(number))) {
                        if (StringUtils.equals(b2.getString(date), b1.getString(date))) {//时间相同按照次数倒序
                            return b2.getString(number).compareTo(b1.getString(number));// 降序
                        } else {
                            return b2.getString(date).compareTo(b1.getString(date));// 时间降序
                        }
                    } else {
                        return 0;
                    }
                }
            });
        }
    }

    /**
     * 根据时间以及次数对list进行排序
     *
     * @param list-排序对象
     * @param hit-第一重是否命中排序
     * @param date-第二重时间排序
     * @param number-第三重次数排序
     */
    public static void sortThredDetailList(List<JSONObject> list, String hit, String date, String number) {
        if (list != null && list.size() > 0 && StringUtils.isNotBlank(hit) && StringUtils.isNotBlank(date)) {
            // 根据时间排序
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject b1, JSONObject b2) {
                    String obj1 = b1.getString(date);
                    String obj2 = b2.getString(date);
                    if (StringUtils.isNotBlank(obj1) && StringUtils.isNotBlank(obj2)) {
                        if (StringUtils.equals(b2.getString(hit), b1.getString(hit))) {//1.第一重是否命中排序
                            if (StringUtils.equals(b2.getString(date), b1.getString(date))) {//2.第二重时间排序
                                return b2.getInteger(number).compareTo(b1.getInteger(number));//3.第三重次数排序
                            } else {
                                return b2.getString(date).compareTo(b1.getString(date));//2.第二重时间排序
                            }
                        } else {
                            return b2.getBoolean(hit).compareTo(b1.getBoolean(hit));//1.第一重是否命中排序
                        }
                    } else {
                        if (b2.getBoolean(hit) == b1.getBoolean(hit)) {//时间为空时，先按是否命中排序
                            return b2.getInteger(number).compareTo(b1.getInteger(number));//3.第三重次数排序
                        } else {
                            return b2.getBoolean(hit).compareTo(b1.getBoolean(hit));//2.第二重是否命中排序
                        }
                    }
                }
            });
        }
    }

    /**
     * 根据时间对JsonArray进行排序
     *
     * @param list
     * @param dataName
     * @param type     0:降序，1：升序
     */
    public static void sortDetailJsonArray(List<JSONObject> list, String dataName, int type) {
        if (list != null && list.size() > 0) {
            // 根据时间排序
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject b1, JSONObject b2) {
                    int i1 = b1.getInteger(dataName);
                    int i2 = b2.getInteger(dataName);
                    if (i1 > 0 && i2 > 0) {
                        if (type == 0) {
                            return b2.getInteger(dataName).compareTo(b1.getInteger(dataName));// 降序
                        } else {
                            return b1.getInteger(dataName).compareTo(b2.getInteger(dataName));// 升序
                        }
                    } else {
                        return 0;
                    }
                }
            });
        }
    }



    /**
     * 私有实现比较器
     */
    private static class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<String>();
        data.add("上海");
        data.add("天津");
        data.add("北京");
        data.add("深圳");
        data.add("广州");
        data.add("成都");
        data.add("西安");
        data.add("武汉");
        data.add("郑州");
        data.add("邯郸");



        for (String str : data) {
            System.out.println(str);
        }

    }


}
