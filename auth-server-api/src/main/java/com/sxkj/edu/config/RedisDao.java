package com.sxkj.edu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fanggt
 * @create 2022-01-08 10:35
 **/
@Service
@Slf4j
public class RedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

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

    public Long incr(final String key, long delta) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        Long increment = operations.increment(key, delta);
        return increment;
    }

    public Long decr(final String key, long delta) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        Long increment = operations.decrement(key, delta);
        return increment;
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
            log.error("Request NO:[{}], call RedisDao Occur exception:", e);
            return false;
        }
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

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    public boolean setTimeoutByKey(final String key, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希 添加所有
     *
     * @param key
     * @param map
     */
    public void hmSetAll(String key, Map map) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key, map);
    }

    /**
     * 哈希 自增
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public Long hmIncrement(String key, Object hashKey, Long value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.increment(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }
    //

    /**
     * 获取散列全部数据
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hmEntries(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    /**
     * 获取散列全部value
     *
     * @param key
     * @return
     */
    public List<Object> hmValues(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.values(key);
    }


    public Object hmDel(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        if (null == hashKey) {
            this.remove(key);
            return "all";
        } else {
            return hash.delete(key, hashKey);
        }

    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void rPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(k, v);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void rightPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 列表删除
     *
     * @param key
     * @param value
     * @return
     */
    public void lRemove(String key, Object value) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        long count = 1;
        list.remove(key, count, value);
    }

    /**
     * 列表获取 左侧弹出
     *
     * @return
     */
    public Object lLeftPop(String key) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPop(key);
    }

    /**
     * 列表获取 右侧弹出
     *
     * @return
     */
    public Object lRightPop(String key) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPop(key);
    }


    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * set删除
     *
     * @param key   key
     * @param value 值
     */
    public void setRemove(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.remove(key, value);
    }


    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 集合中是否含有某元素
     *
     * @param key
     * @return
     */
    public Boolean isMember(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.isMember(key, value);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合长度
     *
     * @param key
     */
    public Long zSize(String key) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.size(key);
    }

    /**
     * 有序集合长度
     *
     * @param key
     */
    public Long zCard(String key) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.zCard(key);
    }

    /**
     * 有序集合scoure自增
     *
     * @param key
     * @param value
     * @param delta
     */
    public void zIncrementScore(String key, Object value, double delta) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.incrementScore(key, value, delta);
    }

    /**
     * 有序集合scoure自增
     *
     * @param key
     * @param score
     * @param member
     */
    public void zIncrementScore(String key, String member, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.incrementScore(key, member, score);
    }


    /**
     * 有序集合取交集
     *
     * @param key
     * @param key2
     * @param destKey
     */
    public Long zIntersectAndStore(String key, String key2, String destKey) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, key2, destKey);
    }

    /**
     * 有序集合取交集
     *
     * @param key
     * @param otherKey
     * @param destKey
     */
    public Long zIntersectAndStore(String key, List<String> otherKey, String destKey) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 有序集合删除
     *
     * @param key
     * @param values
     */
    public void zRemove(String key, Object... values) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.remove(key, values);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> zRangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 有序集合获取位置
     *
     * @param key
     * @param scoure
     * @return
     */
    public Long zRank(String key, Object scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rank(key, scoure);
    }

    /**
     * 有序集合获取位置,从大到小
     *
     * @param key
     * @param scoure
     * @return
     */
    public Long zreverseRank(String key, Object scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRank(key, scoure);
    }

    /**
     * 有序集合获取--倒序
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> ZreverseRange(String key, Long scoure, Long scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRange(key, scoure, scoure1);
    }

    /**
     * 有序集合获取--倒序
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> ZreverseRangeWithScores(String key, Long scoure, Long scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRangeWithScores(key, scoure, scoure1);
    }

    /**
     * zset 获取指定值的score
     *
     * @param key
     * @param o
     * @return
     */
    public Double ZScore(String key, Object o) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, o);
    }

    /**
     * 删除指定范围内的数据
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Long ZremoveRangeByScore(String key, Long scoure, Long scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.removeRangeByScore(key, scoure, scoure1);
    }

    /**
     * 删除指定排序内的数据
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Long ZremoveRange(String key, Long scoure, Long scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.removeRange(key, scoure, scoure1);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> Zrange(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeWithScores(key, start, end);
    }

    /**
     * 获取hash中所有的key
     *
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.keys(key);
    }


    public void delKeys(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.delete(keys);
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

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
