package com.sxkj.edu.common.page;

import com.sxkj.edu.common.utils.Args;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author JJJ
 * @date 2023/2/27
 * 数据转换工具类
 */
public class PageConvertUtil {


    /**
     * 分页工具起始页检查
     *
     * @param page 起始页
     * @return 返回数字类型
     */
    public static int checkPage(int page) {
        if (page == 0 || page < 0) {
            return 1;
        } else {
            return page;
        }
    }


    /**
     * 分页获取信息
     *
     * @param page 起始页
     * @param size 分页大小
     * @param <T>  对应的实体类
     * @return 返回自定义分页工具
     */
    public static <T> PageCommonBean<T> getList(List<T> list, long page, long size, long count) {
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        PageCommonBean<T> pageBean;
        // 传 0 则为查全部数据
        if (size != 0) {
            pageBean = new PageCommonBean<>(page, size, count, list);
        } else {
            // 取 list 的大小要判空
            if (Args.isEmpty(list)) {
                pageBean = new PageCommonBean<>(page, list.size(), list.size(), list);
            } else {
                pageBean = new PageCommonBean<>(page, 1, 0, list);
            }
        }
        return pageBean;
    }

    /**
     * 自定义List 分页获取信息
     * @param list
     * @param page
     * @param size
     * @param count
     * @param <T>
     * @return
     */
    public static <T> PageVO<T> getPageVO(List<T> list, int page, int size, int count) {
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        List<T> resList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return new PageVO<>(0,0,resList);
        }
        int totalPages = (int) Math.ceil((double) count / size);
        if (page > totalPages) {
            return new PageVO<>(0,0,resList);
        }

        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, count);

        resList = list.subList(startIndex, endIndex);
        return new PageVO<>(count,totalPages,resList);
    }


    /**
     * Bean -> Map
     *
     * @param sources [T] 资源
     * @return map
     */
    public static <T> Map<String, Object> transBeanToMap(T sources) {
        if (sources == null) {
            return new HashMap<>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Field[] fieldArray = getAllFields(sources);
            for (Field field : fieldArray) {
                //设置可访问,不然拿不到private
                field.setAccessible(true);
                //配置了注解的话则使用注解名称,作为header字段
                String fieldName = field.getName();
                Object o = field.get(sources);
                if (o != null) {
                    map.put(fieldName, o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("mybatisplus turn bean to map fail");
        }
        return map;
    }

    /**
     * string 转list
     * @param str
     * @return
     */
    public static List<String> strConvertList(@NotNull String str,String split){
        List<String> list = new ArrayList<>();
        //List转String
        list = Arrays.asList(str.split(split));
        return list;
    }



    private static Field[] getAllFields(Object object) {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    public static void main(String[] args) {
        List<String> list =strConvertList("[\"tjfx_syb_hyttl\",\"online_sbfw\"]",",");
        System.out.println(list);
    }


}
