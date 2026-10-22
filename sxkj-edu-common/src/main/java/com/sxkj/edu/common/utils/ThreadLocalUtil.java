package com.sxkj.edu.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
* @desc 线程安全隔离类
* @company SXKJ
* @author jiangjiangjun
* @date 2020/6/28 16:02
*/

public class ThreadLocalUtil {

  private static final ThreadLocal<Map<String, Object>> THREAD_VARIABLES = ThreadLocal.withInitial(HashMap::new);

  @SuppressWarnings("unchecked")
  public static <T> T getThreadVariable(String name) {
    return (T) THREAD_VARIABLES.get().get(name);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getThreadVariable(String name, T initialValue) {
    T o = (T) THREAD_VARIABLES.get().get(name);
    if (o == null) {
      THREAD_VARIABLES.get().put(name, initialValue);
      return getThreadVariable(name);
    } else {
      return o;
    }
  }

  public static void setThreadVariable(String name, Object value) {
    THREAD_VARIABLES.get().put(name, value);
  }

  /**
   * 缓存api与权限
   * @param obj
   */
  public static void setApiInPrivilege(Object obj){
    setThreadVariable("set_api_privilegee_info",obj);
  }

  /**
   * 获取api与权限
   */
  public static Map<String,String> getApiInPrivilege(){
    return getThreadVariable("set_api_privilegee_info");
  }

  public static void destroy() {
    THREAD_VARIABLES.remove();
  }
}
