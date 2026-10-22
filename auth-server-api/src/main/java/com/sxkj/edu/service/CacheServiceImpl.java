package com.sxkj.edu.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.ThreadUtils;
import com.sxkj.edu.module.system.login.domain.LoginDetailVO;
import com.sxkj.edu.module.system.login.domain.LoginStuSchoolDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.sxkj.edu.common.base.constants.CoreConstant.PARENT_FIELD_PREFIX;
import static com.sxkj.edu.common.base.constants.CoreConstant.PARENT_MAP_KEY;


/**
 * 〈公共缓存服务类〉
 *
 * @author jiangjianjun
 * @create 2018/5/9
 * @since 1.0.0
 */
@Service
public class CacheServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Resource
    private RedisCache redisCache;

    /**
     * 保存用户成绩到redis，存在就更新，不存在插入
     *
     * @param businessType 模块代码
     */
    public void setStuCacheInfo(String businessType, String stuNum, JSONObject content) {
        String key = businessType + "_" + stuNum;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("stu_num", stuNum);
            map.put("cjgl_info", content);
            redisCache.hset(businessType, key, map, 0);

        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl setStuCacheInfo occur exception", ThreadUtils.getThreadNO(), e);
        }
    }

    public Map<String, JSONObject> getStuCacheInfo(String businessType, String stuNum) {
        Map<String, JSONObject> codeMap = new HashMap<>();
        try {
            String key = businessType + "_" + stuNum;
            if (redisCache.hexists(businessType, key)) {
                codeMap = (Map) redisCache.hget(businessType, key);
            } else {
                codeMap = (Map) redisCache.hmget(key);
            }
        } catch (Exception e) {
            log.error("request no ,call CacheServiceImpl getStuCacheInfo occur exception", e);
        }
        return codeMap;
    }

    /**
     * 保存用户登录信息到redis，存在就更新，不存在插入
     * loginType
     *
     * @param loginName 用户账号
     */
    public void setLoginUserCache(String loginType, String loginName, JSONObject content) {
        String key = loginType + "_" + loginName;
        try {
            if (redisCache.hexists(key, key)) {
                log.warn("call CacheServiceImpl setLoginUserCache in exist,key:{},", key);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("login_name", loginName);
                map.put("user_info", content);
                redisCache.hmset(key, map, CoreConstant.USER_LOGIN_CACHE_EXPIRE);// 过期时间为3天
                log.warn("call CacheServiceImpl setLoginUserCache in exist,key:{},", key);
            }

        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl setLoginUserCache occur exception", ThreadUtils.getThreadNO(), e);
        }
    }

    public void setLoginUserSchoolCache(String loginType, String loginName, JSONObject content) {
        String key = loginType + "_" + loginName;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login_name", loginName);
            map.put("user_info", content);
            Long currTime = redisCache.getKeyExpireTime(key);
            // 默认减去1分钟
            if(null != currTime && currTime > 60){
                redisCache.hset(key, key, map, currTime-60);
            }else{
                log.warn("call CacheServiceImpl setLoginUserSchoolCache in not expire,key:{},", key);
            }

        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl setLoginUserCache occur exception", ThreadUtils.getThreadNO(), e);
        }
    }

    /**
     * 从缓存里获取用户信息，注意更新用户时需要更新缓存
     * @param loginType
     * @param loginName
     * @return
     */
    public LoginDetailVO getLoginUserCache(String loginType, String loginName) {
        LoginDetailVO loginDetailVO = null;
        Map<String,JSONObject> codeMap = new HashMap<>();
        try {
            String key = loginType +"_"+ loginName;
            if(redisCache.hexists(key,key)){
                codeMap = (Map) redisCache.hget(key,key);
            }else{
                codeMap = (Map) redisCache.hmget(key);
            }
            if(!CollectionUtils.isEmpty(codeMap)){
                loginDetailVO = JSON.parseObject(JSON.toJSONString(codeMap.get("user_info")),LoginDetailVO.class);
            }
        } catch (Exception e) {
            log.error("request no ,call CacheServiceImpl getLoginUserCache occur exception", e);
        }
        return loginDetailVO;
    }

    public Boolean removeLoginUserCache(String loginType, String loginName) {
        Boolean bs = false;
        try {
            String key = loginType + "_" + loginName;
            redisCache.del(key);
            bs = true;
        } catch (Exception e) {
            log.error("request no ,call CacheServiceImpl removeLoginUserCache occur exception", e);
        }
        return bs;
    }

    /**
     * 模糊删除redis的数据
     * @param loginType
     * @return
     */
    public Boolean removeLoginUserCacheLikeKey(String loginType) {
        Boolean bs = false;
        try {
            Set<String> keys = redisCache.queryLikeKey(loginType);
            String[] keyArr = keys.stream().toArray(String[]::new);
            redisCache.del(keyArr);
            bs = true;
        } catch (Exception e) {
            log.error("request no ,call CacheServiceImpl removeLoginUserCache occur exception", e);
        }
        return bs;
    }

    /**
     * 根据登录账号模糊删除redis的数据
     * @param loginName
     * @return
     */
    public Boolean removeLoginUserNameCacheLikeKey(String loginName) {
        Boolean bs = false;
        try {
            Set<String> keys = redisCache.queryLikeKey(loginName);
            String[] keyArr = keys.stream().toArray(String[]::new);
            redisCache.del(keyArr);
            bs = true;
        } catch (Exception e) {
            log.error("request no ,call CacheServiceImpl removeLoginUserNameCacheLikeKey occur exception", e);
        }
        return bs;
    }

    /**
     * 重新设置用户缓存
     * @param loginType
     * @param loginName
     * @param content
     */
    public void reloadLoginUserCache(String loginType,String loginName,JSONObject content){
        String key = loginType +"_"+ loginName;
        try {
            if(redisCache.hexists(key,key)){
                Map<String, Object> map = new HashMap<>();
                map.put("login_name",loginName);
                map.put("user_info",content);
                // 获取当前缓存剩余时间
                Long currTime = redisCache.getKeyExpireTime(key);
                // 默认减去1分钟
                if(null != currTime && currTime > 60){
                    redisCache.hset(key, key, map, currTime-60);
                }
                log.warn("call CacheServiceImpl reloadLoginUserCache in exist,update key:{},",key);
            }else{
                log.warn("call CacheServiceImpl reloadLoginUserCache in not exist key:{},",key);
            }
        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl reloadLoginUserCache occur exception", ThreadUtils.getThreadNO(), e);
        }
    }

    /**
     * 根据用户名获取对应的学校信息
     * @param userName
     * @return
     */
    public LoginStuSchoolDetailVO getLoginUserSchool(String userName) {
        LoginStuSchoolDetailVO loginDetailVO = null;
        String redisSKey = CoreConstant.edu_user_school;
        Map<String, JSONObject> codeMap = new HashMap<>();
        try {
            if (redisCache.hexists(redisSKey, userName)) {
                codeMap = (Map) redisCache.hget(redisSKey, userName);
            } else {
                codeMap = (Map) redisCache.hmget(redisSKey);
            }
            if (!CollectionUtils.isEmpty(codeMap)) {
                loginDetailVO = JSON.parseObject(JSON.toJSONString(codeMap), LoginStuSchoolDetailVO.class);
            }
        } catch (Exception e) {
            log.error("request no ,call CacheServiceImpl getLoginUserSchool occur exception", e);
        }
        return loginDetailVO;
    }


    /**
     * 保存学生家长对应的学生id（Hash结构缓存）
     * @param loginPhone
     * @param stuId
     */
    public Long setLoginParentStuIdCache(String loginPhone, Long stuId) {
        String field = PARENT_FIELD_PREFIX + loginPhone;
        Long currStuId = null;
        Map<String, Object> map = new HashMap<>();
        try {
            if (redisCache.exists(PARENT_MAP_KEY)) {
                map = (Map) redisCache.hmget(PARENT_MAP_KEY);
                if(CollectionUtil.isEmpty(map)){
                    map = new HashMap<>();
                }
            } else {
                log.info("call CacheServiceImpl setLoginParentStuIdCache in not exist,field:{},", field);
            }
            currStuId = stuId;
            map.put(field, stuId);
            redisCache.hmset(PARENT_MAP_KEY, map);
        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl setLoginParentStuIdCache occur exception", ThreadUtils.getThreadNO(), e);
        }
        return currStuId;
    }

    public Long delLoginParentStuIdCache(String loginPhone) {
        String field = PARENT_FIELD_PREFIX + loginPhone;
        Long currStuId = null;
        try {
            if (redisCache.hexists(PARENT_MAP_KEY, field)) {
                redisCache.hdel(PARENT_MAP_KEY, field);
                log.info("call CacheServiceImpl delLoginParentStuIdCache in exist,field:{},", field);
            }
        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl setLoginParentStuIdCache occur exception", ThreadUtils.getThreadNO(), e);
        }
        return currStuId;
    }

    /**
     * 切换学生并更新到缓存（Hash结构缓存）
     * @param loginPhone
     * @param stuId
     * @return
     */
    public void switchLoginParentStuIdCache(String loginPhone, Long stuId) {
        String field = PARENT_FIELD_PREFIX + loginPhone;
        try {
            Map<String, Object> map = new HashMap<>();
            if (redisCache.exists(PARENT_MAP_KEY)) {
                map = (Map) redisCache.hmget(PARENT_MAP_KEY);
                if(CollectionUtil.isEmpty(map)){
                    map = new HashMap<>();
                }
            }else{
                log.warn("call CacheServiceImpl switchLoginParentStuIdCache success,field:{},", field);
            }
            map.put(field, stuId);
            redisCache.hmset(PARENT_MAP_KEY, map);
        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl switchLoginParentStuIdCache occur exception", ThreadUtils.getThreadNO(), e);
        }
    }

    /**
     * 获取家长的当前学生id（Hash结构缓存）
     * @param loginPhone
     */
    public Long getLoginParentStuIdCache(String loginPhone) {
        String field = PARENT_FIELD_PREFIX + loginPhone;
        try {
            if (redisCache.hexists(PARENT_MAP_KEY, field)) {
                Object val = redisCache.hget(PARENT_MAP_KEY, field);
                return val != null ? new Long(val.toString()) : null;
            } else {
                log.warn("call CacheServiceImpl getLoginParentStuIdCache in not exist,field:{},", field);
                return null;
            }
        } catch (Exception e) {
            log.error("request no:[{}],call CacheServiceImpl getLoginParentStuIdCache occur exception", ThreadUtils.getThreadNO(), e);
        }
        return null;
    }

    public Long incr(String key) {
        try {
            return redisCache.incr(key,1);
        } catch (Exception e) {
            log.error("cacheService.incr error", e);
        }
        return null;
    }
}
