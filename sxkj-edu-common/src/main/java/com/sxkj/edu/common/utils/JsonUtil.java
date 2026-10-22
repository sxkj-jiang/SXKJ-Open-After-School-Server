package com.sxkj.edu.common.utils;

import com.alibaba.excel.util.FileUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxkj.edu.common.core.ObjectMapperImpl;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Json对象转换工具
 *
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapperImpl();

    public static final String DEFAULT_DELIMITER = ".";

    /**
     * 单个对象转json字符串
     *
     * @param o
     * @return
     */
    public static String object2Json(Object o) {
        if (o == null) {
            return null;
        }

        String s = null;
        try {
            s = objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            log.warn("object to com.ruyuan.eshop.common.json error", e);
        }
        return s;
    }

    /**
     * 集合对象批量转json字符串集合
     *
     * @param objects
     * @param <T>
     * @return
     */
    public static <T> List<String> listObject2ListJson(List<T> objects) {
        if (objects == null) {
            return null;
        }

        List<String> lists = new ArrayList<String>();
        for (T t : objects) {
            lists.add(object2Json(t));
        }

        return lists;
    }

    /**
     * json字符串集合批量转对象集合
     *
     * @param jsons
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> listJson2ListObject(List<String> jsons, Class<T> clazz) {
        if (jsons == null) {
            return null;
        }

        List<T> ts = new ArrayList<T>();
        for (String j : jsons) {
            ts.add(json2Object(j, clazz));
        }

        return ts;
    }

    /**
     * json字符串转指定类型的对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, Class<T> clazz) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("com.ruyuan.eshop.common.json to object error", e);
        }
        return null;
    }

    /**
     * json字符串转指定类型的对象
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, TypeReference<T> typeReference) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.warn("com.ruyuan.eshop.common.json to object error", e);
        }
        return null;
    }

    /**
     * 用于支持获取远程调用返回数据的转换
     * 注意事项：
     * 支持多单词下划线专驼峰（序列化和反序列化）
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getData(Object data, Class<T> clazz) {
        if (data == null) {
            return null;
        }
        try {
            String jsonStr = objectMapper.writeValueAsString(data);
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.warn("com.ruyuan.eshop.common.json data process error", e);
        }
        return null;
    }

    /**
     * 用于支持获取远程调用返回数据的转换
     * 注意事项：
     * 支持多单词下划线专驼峰（序列化和反序列化）
     *
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T getData(Object data, TypeReference<T> typeReference) {
        if (data == null) {
            return null;
        }
        try {
            String jsonStr = objectMapper.writeValueAsString(data);
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.warn("com.ruyuan.eshop.common.json data process error", e);
        }
        return null;
    }

    public static <T> T parse(String s) {
        return (T) JSON.parse(s);
    }

    public static <T> T parse(InputStream inputStream) throws IOException {
        String s = IOUtils.toString(inputStream, Charset.defaultCharset());
        return parse(s);
    }

    public static <T> T parse(String s, Class clazz) {
        JSON json = parse(s);
        return (T) JSON.toJavaObject(json, clazz);
    }

//    public static <T> T parse(File file) throws IOException {
//        return (T) parse(null, file);
//    }

//    public static <T> T parse(String charset, File file) throws IOException {
//        return parse(FileUtils.readFileToString(file, charset));
//    }

//    public static <T> T parse(File file, Class clazz) throws IOException {
//        return (T) parse(null, file, clazz);
//    }

//    public static <T> T parse(String charset, File file, Class clazz) throws IOException {
//        JSON json = parse(charset, file);
//        return (T) jsonToJava(json, clazz);
//    }

    public static <T> T parseClasspathResource(String path) throws IOException {
        InputStream inputStream = JsonUtil.class.getResourceAsStream(path);
        return parse(inputStream);
    }

    public static <T> T jsonToJava(JSON json, Class clazz) {
        return (T) JSON.toJavaObject(json, clazz);
    }

    /**
     * 将JSON平展化为带路径的map
     *
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> flatten(String jsonString) throws JSONException {
        return flatten(jsonString, DEFAULT_DELIMITER);
    }

    /**
     * 将JSON平展化为带路径的map
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> flatten(JSONObject jsonObject) throws JSONException {
        return flatten(jsonObject, DEFAULT_DELIMITER);
    }

    /**
     * 将JSON平展化为带路径的map
     *
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> flatten(String jsonString, String delimiter) throws JSONException {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        return flatten(jsonObject, delimiter);
    }

    /**
     * 将JSON平展化为带路径的map
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> flatten(JSONObject jsonObject, String delimiter) throws JSONException {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> result = new ConcurrentHashMap<>();
        flatten(result, sb, "$", jsonObject, delimiter);
        String s = "{" + sb.toString() + "}";
        return result;
    }

    /**
     * 将JSON平展化为带路径的map
     *
     * @return
     * @throws JSONException
     */
    private static void flatten(Map<String, Object> result, StringBuilder flattenedJson, String parent, Object value, String delimiter) throws JSONException {
        if (value instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) value;
            final int[] count = {0};
            jsonObject.entrySet().stream().forEachOrdered(o -> {
                String key = o.getKey();
                if (key.contains(delimiter)) {
                    throw new IllegalArgumentException("A key cannot contain the delimiter");
                }
                String hkey = (parent == null) ? key : parent + delimiter + key;
                Object jval = jsonObject.get(key);
                flatten(result, flattenedJson, hkey, jval, delimiter);
                if (count[0] < jsonObject.size() - 1) {
                    flattenedJson.append(",");
                    count[0]++;
                }
            });

        } else if (value instanceof Collection) {
            Object[] valueArray = ((Collection) value).toArray();
            boolean flatChildOfCollection = true;
            if (flatChildOfCollection) {
                for (int i = 0; i < valueArray.length; i++) {
                    if (i < 2) {
                        //                    String hkey = (parent == null) ? Integer.toString(i) : parent + delimiter + i;
                        String hkey = (parent == null) ? Integer.toString(i) : parent + delimiter;
                        Object aval = valueArray[i];
                        flatten(result, flattenedJson, hkey, aval, delimiter);
                        if (i < valueArray.length - 1) {
                            flattenedJson.append(",");
                        }
                    }
                }
            }
            JSONArray array = new JSONArray();
            for (int i = 0; i < valueArray.length; i++) {
                array.add(valueArray[i]);
            }
            result.put(parent, array);
        } else if (value instanceof String) {
            flattenedJson.append("\"").append(parent).append("\"").append(":");
            String s = (String) value;
            flattenedJson.append("\"").append(s).append("\"");
            result.put(parent, s);
        } else if (value instanceof Integer) {
            flattenedJson.append("\"").append(parent).append("\"").append(":");
            Integer integer = (Integer) value;
            flattenedJson.append(integer);
            result.put(parent, integer);
        } else if (value instanceof Object) {
            flattenedJson.append("\"").append(parent).append("\"").append(":");
            try {
                String s = String.valueOf(value);
                flattenedJson.append(s);
                result.put(parent, s);
            } catch (Exception e) {
                throw new JSONException("flatten fail " + e.getMessage());
            }
        } else if (value == null) {
            flattenedJson.append("\"").append(parent).append("\"").append(":");
            flattenedJson.append("null");
        }
    }

    /**
     * 自定义过滤指定字段的过滤器
     *
     * @param filterSign true为过滤掉指定字段 ；false为显示指定的字段
     * @param fieldName  指定需要过滤掉或者显示的字段名
     * @return
     */
    public static PropertyFilter myPropertyFilter(boolean filterSign, String... fieldName) {
        PropertyFilter profilter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                if ( fieldName != null ) {
                    for (String filterName : fieldName) {
                        if ( name.equalsIgnoreCase(filterName) ) {
                            return !filterSign;
                        }
                    }
                }
//        return true;
                return filterSign;
            }
        };
        return profilter;
    }

    /**
     * 从指定JSON根节点，向下获取指定的节点
     * @param inputNode  如："base_model"
     * @param nodes  如："bigdata.result"
     * @return JSONObject
     */
    public static JSONObject fetchNode(JSONObject inputNode, String... nodes) {
        JSONObject rootNode = inputNode;
        try {
            if (rootNode != null && !rootNode.isEmpty() && nodes != null) {
                for (String node : nodes) {
                    rootNode = rootNode.getJSONObject(node);
                    if (rootNode == null || rootNode.isEmpty()) {
                        log.warn("Request NO:[%s], fetch node failed");
                        return rootNode;
                    }
                }
            } else {
                log.warn("Request NO:[%s], fetch node is null");
                return rootNode;
            }

        } catch (Exception e) {
            log.error("Request NO:[{}], fetch node Occur exception:", e);
            return rootNode;
        }
        return rootNode;
    }

    public static List<String> jsonArrayToList(@NotNull JSONArray array){
        List<String> list = array.toJavaList(String.class);
        return list;
    }
}
