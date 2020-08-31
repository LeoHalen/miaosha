package site.halenspace.common.redis.repository;

import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Zg.Li · 2020/8/31
 */
interface CacheRepository {
    void flushDB(RedisClusterNode node);

    /**
     * 设置key过期时间
     * @param key redis主键
     * @param time 过期时间(单位秒)
     */
    void expire(final String key, final long time);

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    void setExpire(final byte[] key, final byte[] value, final long time);

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    void setExpire(final String key, final Object value, final long time);

    /**
     * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
     *
     * @param keys   redis主键数组
     * @param values 值数组
     * @param time   过期时间(单位秒)
     */
    void setExpire(final String[] keys, final Object[] values, final long time);


    /**
     * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
     *
     * @param keys   the keys
     * @param values the values
     */
    void set(final String[] keys, final Object[] values);


    /**
     * 添加到缓存
     *
     * @param key   the key
     * @param value the value
     */
    void set(final String key, final Object value);

    /**
     * 查询在以keyPatten的所有  key
     *
     * @param keyPatten the key patten
     * @return the set
     */
    Set<String> keys(final String keyPatten);

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the byte [ ]
     */
    byte[] get(final byte[] key);

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the string
     */
    <T> T get(final String key, final Class<T> clazz);


    /**
     * Ops for hash hash operations.
     *
     * @return the hash operations
     */
    <T> HashOperations<String, String, T> opsForHash();

    /**
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    void putHashValue(String key, String hashKey, Object hashValue);

    /**
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    <T> T getHashValue(String key, String hashKey);

    /**
     * 获取单个field对应的值, 并转换为指定的clazz类型
     * @param key the key
     * @param hashKey the hash key
     * @param clazz the hash value type
     * @param <T>
     * @return the hash value
     */
    <T> T getHashValue(String key, String hashKey, Class<T> clazz);

    /**
     * 根据key值删除
     *
     * @param key      the key
     * @param hashKeys the hash keys
     */
    void delHashValues(String key, Object... hashKeys);

    /**
     * key只匹配map
     *
     * @param key the key
     * @return the hash value
     */
    <HK, HV> Map<HK, HV> getHashValues(String key);

    /**
     * 批量添加
     *
     * @param key the key
     * @param map the map
     */
    void putHashValues(String key, Map<String, Object> map);

    /**
     * 集合数量
     *
     * @return the long
     */
    long dbSize();

    /**
     * 清空redis存储的数据
     *
     * @return the string
     */
    String flushDB();

    /**
     * 判断某个主键是否存在
     *
     * @param key the key
     * @return the boolean
     */
    boolean exists(final String key);

    /**
     * 删除key
     *
     * @param keys the keys
     * @return the long
     */
    boolean del(final String... keys);

    /**
     * 对某个主键对应的值加一,value值必须是全数字的字符串
     *
     * @param key the key
     * @return the long
     */
    long incr(final String key);

    /**
     * redis List 引擎
     *
     * @return the list operations
     */
    ListOperations<String, Object> opsForList();

    /**
     * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    Long leftPush(String key, Object value);

    /**
     * redis List数据结构 : 移除并返回列表 key 的头元素
     *
     * @param key the key
     * @return the string
     */
    Object leftPop(String key);

    /**
     * redis List数据结构 :将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    Long in(String key, Object value);

    /**
     * redis List数据结构 : 移除并返回列表 key 的末尾元素
     *
     * @param key the key
     * @return the string
     */
    Object rightPop(String key);


    /**
     * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
     *
     * @param key the key
     * @return the long
     */
    Long length(String key);

    /**
     * redis List数据结构 : 根据参数 i 的值，移除列表中与参数 value 相等的元素
     *
     * @param key   the key
     * @param i     the
     * @param value the value
     */
    void remove(String key, long i, Object value);

    /**
     * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    void set(String key, long index, Object value);

    /**
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    List<Object> getList(String key, int start, int end);

    /**
     * redis List数据结构 : 批量存储
     *
     * @param key  the key
     * @param list the list
     * @return the long
     */
    Long leftPushAll(String key, List<String> list);

    /**
     * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    void insert(String key, long index, Object value);
}
