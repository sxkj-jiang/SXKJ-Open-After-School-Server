package com.sxkj.edu.common.redis;

import com.sxkj.edu.common.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存公共实现
 */
@Slf4j
public class RedisCache {

    private RedisTemplate redisTemplate;

    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送消息到指定队列
     * @param data
     * @return
     */
    public boolean sendRedisMessage(String channel,Object data) {
        try {
            redisTemplate.convertAndSend(channel, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object getObjectValue(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 缓存存储
     * @param key
     * @param value
     * @param seconds 自动失效时间
     */
    public void set(String key, String value, int seconds){

        ValueOperations<String,String> vo = redisTemplate.opsForValue();
        if(seconds > 0){
            vo.set(key, value, seconds, TimeUnit.SECONDS);
        }else{
            vo.set(key, value);
        }
    }

    /**
     * 模糊查询key
     * @param key
     * @return
     */
    public Set<String> queryLikeKey(String key){
        Set<String> keys = redisTemplate.keys(key+"*");
        return keys;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setObject(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 缓存获取
     * @param key
     * @return
     */
    public Object get(String key) {

        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);

            }
            return true;
        } catch (Exception e) {
            log.error("Request NO:[{}], call RedisDao Occur exception:", ThreadUtils.getThreadNO(), e);
            return false;
        }
    }



    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 判断key以及域是否存在
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key, String field){
        return redisTemplate.opsForHash().hasKey(key,field);
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time){

        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);

            }else{
                set(key, value.toString());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 执行事物set
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lockSet(String key, Object value, long time){

        try {
            /*该方法会在没有key时，设置key;存在key时返回false；因此可以通过该方法及设置key的有效期，判断是否有其它线程持有锁*/
            if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
                redisTemplate.expire(key,time, TimeUnit.SECONDS);

                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return false;



    }


    /**
     * 自增
     *
     * @param key
     * @param value
     * @return
     */
    public Long increment(final String key, Long value) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * Redis自增并返回指定位数的补零字符串，key自动到当天23:59:59过期（次日重置）
     *
     * @param key    自增的key（建议包含日期，如 "xxx:20260602"）
     * @param length 返回字符串的总位数（补零填充）
     * @return 补零后的自增字符串，如 "00001"
     */
    public String incrementAndGetPadded(String key, int length) {
        if (length <= 0) {
            length = 5;
        }
        long current = incr(key, 1L);
        // key不过期
        expire(key, 0);
        return String.format("%0" + length + "d", current);
    }

    /**
     * Redis自增并返回5位补零字符串，key自动到当天23:59:59过期（次日重置）
     *
     * @param key 自增的key（建议包含日期，如 "xxx:20260602"）
     * @return 5位补零后的自增字符串，如 "00001"
     */
    public String incrementAndGetPadded5(String key) {
        return incrementAndGetPadded(key, 5);
    }

    /**
     * Redis自增并返回6位补零字符串，key自动到当天23:59:59过期（次日重置）
     *
     * @param key 自增的key（建议包含日期，如 "xxx:20260602"）
     * @return 6位补零后的自增字符串，如 "000001"
     */
    public String incrementAndGetPadded6(String key) {
        return incrementAndGetPadded(key, 6);
    }

    /**
     * Redis自增并返回指定位数的补零字符串，使用调用方指定的过期时间
     *
     * @param key     自增的key
     * @param length  返回字符串的总位数（补零填充）
     * @param timeout 过期时间（秒），<=0则不设置过期
     * @return 补零后的自增字符串
     */
    public String incrementAndGetPadded(String key, int length, long timeout) {
        if (length <= 0) {
            length = 5;
        }
        long current = incr(key, 1L);
        if (timeout > 0) {
            expire(key, timeout);
        }
        return String.format("%0" + length + "d", current);
    }



    //================================Map=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item){
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by){
        return redisTemplate.opsForHash().increment(key, item,-by);
    }

    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object...values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object...values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if(time>0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//    public List<Object> lGet(String key,String field){
//        try {
//            return redisTemplate.opsForList().leftPush(key,field);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Set<String> getKeys(String key){
        return redisTemplate.keys(key);
    }

    /**
     * 缓存手动失效
     * @param key
     * @return
     */
    public boolean delete(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 判断hash key是否存在
     * @param key
     * @return
     */
    public boolean hExists(String key) {
        return hGetAll(key).isEmpty();
    }

    /**
     * 获取hash变量中的键值对
     * 对应redis hgetall 命令
     * @param key
     * @return
     */
    public Map<String,String> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 以map集合的形式添加hash键值对
     * @param key
     * @param map
     */
    public void hPutAll(String key,Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key,map);
    }

    /**
     * 执行lua脚本
     * @param script
     * @param keys
     * @param args
     * @param <T>
     * @return
     */
    public <T> T execute(RedisScript<T> script, List<String> keys,String... args) {
        return (T)redisTemplate.execute(script,keys,args);
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存失效时间
     * @param key
     * @return
     */
    public long getKeyExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS); // 以秒为单位返回剩余时间
    }
}
