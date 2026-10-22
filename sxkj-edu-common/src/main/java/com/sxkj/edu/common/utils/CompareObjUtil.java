package com.sxkj.edu.common.utils;

import java.lang.reflect.Field;

public class CompareObjUtil {

    public static boolean compareObj(Object beforeObj, Object afterObj) throws Exception{
        boolean flag = true;

        if(beforeObj == null) {
            throw new RuntimeException("原对象不能为空");
        }
        if(afterObj == null) {
            throw new RuntimeException("新对象不能为空");
        }
        if(!beforeObj.getClass().isAssignableFrom(afterObj.getClass())){
            throw new RuntimeException("两个对象不相同，无法比较");
        }

        //取出属性
        Field[] beforeFields = beforeObj.getClass().getDeclaredFields();
        Field[] afterFields = afterObj.getClass().getDeclaredFields();
        Field.setAccessible(beforeFields, true);
        Field.setAccessible(afterFields, true);

        //遍历取出差异值
        if(beforeFields != null && beforeFields.length > 0){
            for(int i=0; i<beforeFields.length; i++){
                Object beforeValue = beforeFields[i].get(beforeObj);
                Object afterValue = afterFields[i].get(afterObj);
                if((beforeValue != null && !"".equals(beforeValue) && !beforeValue.equals(afterValue)) || ((beforeValue == null || "".equals(beforeValue)) && afterValue != null)){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

}
