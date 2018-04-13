package com.holly.cache.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;

import lombok.extern.log4j.Log4j;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

/**
 * TODO: Redis 客户端操作类
 */
@Log4j
@Component
public class RedisClient {
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	@Autowired
	private JedisPool jedisPool;

	private Jedis getJedis() {
		return jedisPool.getResource();
	}

	private ShardedJedis getShardedJedis() {
		return shardedJedisPool.getResource();
	}

	/**
	 * 使用客户端向 当前Redis服务器发送一个 PING ，如果服务器运作正常的话，会返回一个 PONG。
	 * 通常用于测试与服务器的连接是否仍然生效，或者用于测量延迟值。
	 * 
	 * @return result 如果连接正常就返回一个 PONG ，否则返回一个连接错误。
	 */
	public String ping() {
		String result = null;
		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.ping();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 切换到当前Redis指定的数据库，数据库索引号index用数字值指定，以0作为起始索引值。默认使用0号数据库。
	 * @param index  数据库索引号
	 * @return result OK
	 */
	public String select(int index) {
		String result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.select(index);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 清空当前数据库中的所有key。
	 * @return result OK
	 */
	public String flushDB() {
		String result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.flushDB();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 清空整个当前 Redis 服务器的数据(删除所有数据库的所有 key )。
	 * @return result OK
	 */
	public String flushAll() {
		String result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.flushAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 返回当前数据库的 key 的数量。
	 * 
	 * @return result 当前数据库的 key 的数量。
	 */
	public Long dbSize() {
		Long result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.dbSize();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 返回所有连接到当前服务器的客户端信息和统计数据。
	 * 返回参数详情见：http://redisdoc.com/server/client_list.html
	 * 
	 * @return result 客户端信息和统计数据。
	 */
	public String clientList() {
		String result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.clientList();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 返回关于 当前Redis 服务器的各种信息和统计数值。 <br>
	 * 返回参数详情见：http://redisdoc.com/server/info.html
	 * 
	 * @return result 服务器信息。
	 */
	public String info() {
		String result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.info();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * ===============================操作Key的方法===============================
	 */

	/**
	 * 查找当前数据库中所有符合给定模式 pattern 的 key 。 <br>
	 * KEYS * 匹配数据库中所有 key 。 <br>
	 * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。 <br>
	 * KEYS h*llo 匹配 hllo 和 heeeeello 等。 <br>
	 * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。 <br>
	 * 特殊符号用 \ 隔开<br>
	 * 注意：KEYS 的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，如果你需要从一个数据集中查找特定的 key， 你最好还是用
	 * Redis 的集合结构(set)来代替。
	 * 
	 * @param pattern
	 *            符合给定模式的 key 列表。
	 */
	public Set<String> keys(String pattern) {
		Set<String> result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.keys(pattern);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}
	
	

	/**
	 * 返回 key 所储存的值的类型。
	 * @param key
	 * @return none(key不存在)、string(字符串)、list(列表)、set(集合)、zset(有序集)、hash(哈希表)
	 */
	public String type(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.type(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回 key 所储存的值的类型。
	 * 
	 * @param key
	 * @return none(key不存在)、string(字符串)、list(列表)、set(集合)、zset(有序集)、hash(哈希表)
	 */
	public String type(byte[] key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.type(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * 
	 * @param key
	 * @param seconds 以秒为单位的时间
	 *            
	 * @return result 设置成功返回 1 。 当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的
	 *         Redis 中你尝试更新 key 的生存时间)，返回 0 。
	 */
	public Long expire(String key, int seconds) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * 
	 * @param key
	 * @param seconds
	 *            以秒为单位的时间
	 * @return result 设置成功返回 1 。 当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的
	 *         Redis 中你尝试更新 key 的生存时间)，返回 0 。
	 */
	public Long expire(byte[] key, int seconds) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX
	 * 时间戳(unix timestamp)。 它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
	 * 
	 * @param key
	 * @param unixTime
	 *            UNIX时间戳
	 * @return result 如果生存时间设置成功，返回 1 。 当 key 不存在或没办法设置生存时间，返回 0 。
	 */
	public Long expireAt(String key, long unixTime) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expireAt(key, unixTime);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX
	 * 时间戳(unix timestamp)。 它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
	 * 
	 * @param key
	 * @param unixTime
	 *            UNIX时间戳
	 * @return result 如果生存时间设置成功，返回 1 。 当 key 不存在或没办法设置生存时间，返回 0 。
	 */
	public Long expireAt(byte[] key, long unixTime) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expireAt(key, unixTime);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 * 
	 * @param key
	 * @return result 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。
	 *         否则，以秒为单位，返回 key 的剩余生存时间。
	 */
	public Long ttl(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ttl(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 * 
	 * @param key
	 * @return result 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。
	 *         否则，以秒为单位，返回 key 的剩余生存时间。
	 */
	public Long ttl(byte[] key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ttl(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除当前数据库中给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的
	 * key )。
	 * 
	 * @param key
	 * @return result 当生存时间移除成功时，返回 1 . 如果 key 不存在或 key 没有设置生存时间，返回 0 。
	 */
	public Long persist(String key) {
		Long result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.persist(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 移除当前数据库中给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的
	 * key )。
	 * 
	 * @param key
	 * @return result 当生存时间移除成功时，返回 1 . 如果 key 不存在或 key 没有设置生存时间，返回 0 。
	 */
	public Long persist(byte[] key) {
		Long result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.persist(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 删除当前数据库中给定的一个或多个 key 。不存在的 key 会被忽略。
	 * 
	 * @param keys
	 * @return result 被删除 key 的数量。
	 */
	public Long del(String... keys) {
		Long result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.del(keys);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 删除当前数据库中给定的一个或多个 key 。不存在的 key 会被忽略。
	 * 
	 * @param keys
	 * @return result 被删除 key 的数量。
	 */
	public Long del(byte[]... keys) {
		Long result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.del(keys);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 删除集群环境中给定的一个key 。不存在的 key 会被忽略。
	 * 
	 * @param key
	 * @return result 被删除 key 的数量。
	 */
	public Long del(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.del(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 删除集群环境中给定的一个key 。不存在的 key 会被忽略。
	 * 
	 * @param key
	 * @return result 被删除 key 的数量。
	 */
	public Long del(byte[] key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.del(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 检查给定 key 是否存在。
	 * 
	 * @param key
	 * @return result 若 key 存在，返回 1 ，否则返回 0 。
	 */
	public Boolean exists(String key) {
		Boolean result = false;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 检查给定 key 是否存在。
	 * 
	 * @param key
	 * @return result 若 key 存在，返回 1 ，否则返回 0 。
	 */
	public Boolean exists(byte[] key) {
		Boolean result = false;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中。 <br>
	 * 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ，或者 key 不存在于当前数据库，那么 MOVE 没有任何效果。
	 * 
	 * @param key
	 * @param dbIndex
	 *            数据库索引号
	 * @return result 移动成功返回 1 ，失败则返回 0 。
	 */
	public Long move(String key, int dbIndex) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.move(key, dbIndex);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回或保存给定列表、集合、有序集合 key 中经过排序的元素。排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较。
	 * 
	 * @param key
	 * @return result 返回列表形式的排序结果
	 */
	public List<String> sort(String key) {
		List<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sort(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回或保存给定列表、集合、有序集合 key 中经过排序的元素。排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较。<br>
	 * 最简单的 SORT 使用方法是 SORT key 和 SORT key DESC <br>
	 * 因为 SORT 命令默认排序对象为数字， 当需要对字符串进行排序时， 需要显式地在 SORT 命令之后添加 ALPHA 修饰符<br>
	 * 排序之后返回元素的数量可以通过 LIMIT 修饰符进行限制， 修饰符接受 offset(要跳过的元素数量) 和 count(跳过 offset
	 * 个指定的元素之后，要返回多少个对象) 两个参数<br>
	 * 
	 * 参考：http://redisdoc.com/key/sort.html
	 * 
	 * @param key
	 * @param sortingParameters
	 *            排序参数
	 * @return result 返回列表形式的排序结果
	 */
	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sort(key, sortingParameters);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * ========================对存储结构为String（字符串）类型的操作========================
	 */
	/**
	 * 将字符串值 value 关联到 key 。如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 
	 * @param key
	 * @param value
	 * @return result SET在设置操作成功完成时，才返回 OK ，如果设置操作未执行，那么命令返回空批量回复（NULL Bulk Reply）。
	 */
	public String set(String key, String value) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			// result = shardedJedis.set(SafeEncoder.encode(key),
			// SafeEncoder.encode(value));
			result = shardedJedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将字符串值 value 关联到 key 。如果 key 已经持有其他值， SET 就覆写旧值，无视类型。<br>
	 * EX second ：设置键的过期时间为 second 秒。 SET key value EX second 效果等同于 SETEX key
	 * second value 。<br>
	 * PX millisecond ：设置键的过期时间为 millisecond 毫秒。 SET key value PX millisecond
	 * 效果等同于 PSETEX key millisecond value 。<br>
	 * NX ：只在键不存在时，才对键进行设置操作。 SET key value NX 效果等同于 SETNX key value 。<br>
	 * XX ：只在键已经存在时，才对键进行设置操作。
	 *
	 * @param key
	 * @param value
	 * @param value nxxx 参数
	 * @param expx EX|PX, expire time units: EX = seconds; PX = milliseconds
	 * @param time expire time in the units

	 * @return result SET在设置操作成功完成时，才返回 OK ，如果设置操作未执行，那么命令返回空批量回复（NULL Bulk Reply）。
	 */
	public String set(String key, String value, String nxxx,String expx,long time) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}
		try {
			result = shardedJedis.set(key, value, nxxx,expx,time);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。 <br>
	 * 若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * 
	 * @param key
	 * @param value
	 *            值
	 * @return 设置成功，返回 1 ； 设置失败，返回 0 。
	 */
	public Long setnx(String key, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setnx(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。 <br>
	 * 若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * 
	 * @param key
	 * @param value
	 *            值
	 * @return 设置成功，返回 1 ； 设置失败，返回 0 。
	 */
	public Long setnx(byte[] key, byte[] value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setnx(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回 key 所关联的字符串值。GET 只能用于处理字符串值。
	 * 
	 * @param key
	 * @return result 当 key 不存在时，返回 nil ，否则，返回 key 的值。 如果 key 不是字符串类型，那么返回一个错误。
	 */
	public String get(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回当前数据库中所有(一个或多个)给定 key 的值。 <br>
	 * 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。因此，该命令永不失败。
	 * @param keys
	 * @return 一个包含所有给定 key 的值的列表。
	 */
	public List<String> mget(String... keys) {
		List<String> result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.mget(keys);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 在当前数据库中同时设置一个或多个 key-value 对。 <br>
	 * 如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定
	 * key 都不存在的情况下进行设置操作。
	 * @param keys
	 * @return 总是返回 OK (因为 MSET 不可能失败)
	 */
	public String mset(String... keysvalues) {
		String result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.mset(keysvalues);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 在当前数据库中同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。 <br>
	 * 即使只有一个给定 key 已存在， MSETNX 也会拒绝执行所有给定 key 的设置操作。
	 * @param keys
	 * @return 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
	 */
	public Long msetnx(String... keysvalues) {
		Long result = null;
		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.msetnx(keysvalues);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 返回 key 所储存的字符串值的长度。 当 key 储存的不是字符串值时，返回一个错误。
	 * 
	 * @param keys
	 * @return 字符串值的长度。 当 key 不存在时，返回 0 。
	 */
	public Long strlen(String key) {
		Long result = null;

		Jedis jedis = getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.strlen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}

		return result;
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
	 * 
	 * @param key
	 * @param value
	 * @return 返回给定 key 的旧值。 当 key 没有旧值时，也即是， key 不存在时，返回 nil 。
	 */
	public String getSet(String key, String value) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getSet(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
	 * 
	 * @param key
	 * @param value
	 * @return 返回给定 key 的旧值。 当 key 没有旧值时，也即是， key 不存在时，返回 nil 。
	 */
	public byte[] getSet(byte[] key, byte[] value) {
		byte[] result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getSet(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
	 * 
	 * @param key
	 * @param offset
	 *            偏移量，offset参数必须大于或等于 0 ，小于 2^32 (bit 映射被限制在 512 MB 之内)。
	 * @param value
	 *            位的设置或清除取决于 value 参数，可以是 0 也可以是 1 。
	 * @return result 指定偏移量原来储存的位。
	 */
	public boolean setbit(String key, long offset, boolean value) {
		boolean result = false;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setbit(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
	 * 
	 * @param key
	 * @param offset
	 *            偏移量
	 * @return result 指定偏移量原来储存的位。
	 */
	public boolean getbit(String key, long offset) {
		boolean result = false;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getbit(key, offset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始。<br>
	 * 例:String str="hello world";<br>
	 * 不存在的 key 当作空白字符串处理：<br>
	 * redis> SETRANGE empty_string 5 "Redis!" # 对不存在的 key 使用 SETRANGE <br>
	 * redis> GET empty_string # 空白处被填充
	 * 
	 * @param key
	 * @param offset
	 *            偏移量
	 * @param value
	 *            值
	 * @return result 被修改之后，字符串的长度。
	 */
	public Long setrange(String key, long offset, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setrange(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。<br>
	 * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推。
	 * 
	 * @param key
	 * @param startOffset
	 *            开始位置(包含)
	 * @param endOffset
	 *            结束位置(包含)
	 * @return result 截取得出的子字符串。
	 */
	public String getrange(String key, long startOffset, long endOffset) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。<br>
	 * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推。
	 * 
	 * @param key
	 * @param start
	 *            开始位置(包含)
	 * @param end
	 *            结束位置(包含)
	 * @return result 截取得出的子字符串。
	 */
	public String substr(String key, int start, int end) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.substr(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。<br>
	 * 如果 key 已经存在， SETEX 命令将覆写旧值。
	 * 
	 * @param key
	 * @param seconds
	 *            过期时间，以秒为单位
	 * @param value
	 *            值
	 * @return result 设置成功时返回 OK 。 当 seconds 参数不合法时，返回一个错误。
	 */
	public String setex(String key, int seconds, String value) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setex(key, seconds, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。 <br>
	 * 如果 key 已经存在， SETEX 命令将覆写旧值。
	 * 
	 * @param key
	 * @param seconds
	 *            过期时间，以秒为单位
	 * @param value
	 *            值
	 * @return result 设置成功时返回 OK 。 当 seconds 参数不合法时，返回一个错误。
	 */
	public String setex(byte[] key, int seconds, byte[] value) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setex(key, seconds, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用。 <br>
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 
	 * @param key
	 * @param integer
	 *            要减去的值
	 * @return 减去之后的值
	 */
	public Long decrBy(String key, long integer) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decrBy(key, integer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用。<br>
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 
	 * @param key
	 * @param integer
	 *            要减去的值
	 * @return 减去之后的值
	 */
	public Long decrBy(byte[] key, long integer) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decrBy(key, integer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将 key 中储存的数字值减一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
	 * 
	 * @param key
	 * @return result 执行 DECR 命令之后 key 的值。
	 */
	public Long decr(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decr(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将 key 所储存的值加上增量 integer 。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
	 * 
	 * @param key
	 * @param integer
	 *            要增加的值
	 * @return result 加上 integer 之后， key 的值。
	 */
	public Long incrBy(String key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incrBy(key, integer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
	 * 
	 * @param key
	 * @return result 执行 INCR 命令之后 key 的值。
	 */
	public Long incr(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incr(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。 <br>
	 * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
	 * 
	 * @param key
	 * @param value
	 * @return result 追加 value 之后， key 中字符串的长度。
	 */
	public Long append(String key, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.append(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/** ========================对存储结构为Hash（哈希表）类型的操作======================== */

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。
	 * 
	 * @param key
	 * @param field
	 *            域
	 * @param value
	 *            值
	 * @return result 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。 如果哈希表中域 field
	 *         已经存在且旧值已被新值覆盖，返回 0 。
	 */
	public Long hset(String key, String field, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hset(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。 <br>
	 * 若域 field 已经存在，该操作无效。 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 * 
	 * @param key
	 * @param field
	 *            域
	 * @param value
	 *            值
	 * @return result 设置成功，返回 1 。 如果给定域已经存在且没有操作被执行，返回 0 。
	 */
	public Long hsetnx(String key, String field, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hsetnx(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 此命令会覆盖哈希表中已存在的域。如果 key
	 * 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 * 
	 * @param key
	 * @param hash
	 * @return result 如果命令执行成功，返回 OK 。 当 key 不是哈希表(hash)类型时，返回一个错误。
	 */
	public String hmset(String key, Map<String, String> hash) {
		String result = null;
		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmset(key, hash);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 * 
	 * @param key
	 * @param field
	 *            域
	 * @return result 给定域的值。 当给定域不存在或是给定 key 不存在时，返回 nil 。
	 */
	public String hget(String key, String field) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hget(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 * 
	 * @param key
	 * @param fields
	 *            域列表
	 * @return result 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
	 */
	public List<String> hmget(String key, String... fields) {
		List<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmget(key, fields);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 value 。增量也可以为负数，相当于对给定域进行减法操作。
	 * 
	 * @param key
	 * @param field
	 *            域
	 * @param value
	 * @return result 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
	 */
	public Long hincrBy(String key, String field, long value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hincrBy(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在。
	 * 
	 * @param key
	 * @param field
	 *            域
	 * @return result 如果哈希表含有给定域，返回 1 。 如果哈希表不含有给定域，或 key 不存在，返回 0 。
	 */
	public Boolean hexists(String key, String field) {
		Boolean result = false;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hexists(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * 
	 * @param key
	 * @param field
	 *            域
	 * @return result 被成功移除的域的数量，不包括被忽略的域。
	 */
	public Long hdel(String key, String field) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hdel(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回哈希表 key 中域的数量。
	 * 
	 * @param key
	 * @return result 哈希表中域的数量。 当 key 不存在时，返回 0 。
	 */
	public Long hlen(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hlen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 * 
	 * @param key
	 * @return result 一个包含哈希表中所有域的表。 当 key 不存在时，返回一个空表。
	 */
	public Set<String> hkeys(String key) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hkeys(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 * 
	 * @param key
	 * @return result 一个包含哈希表中所有值的表。 当 key 不存在时，返回一个空表。
	 */
	public List<String> hvals(String key) {
		List<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hvals(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。 <br>
	 * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 * 
	 * @param key
	 * @return result 以列表形式返回哈希表的域和域的值。 若 key 不存在，返回空列表。
	 */
	public Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hgetAll(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}
		return result;
	}

	/** ========================对存储结构为List（列表）类型的操作======================== */

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。 如果有多个 value 值，那么各个 value
	 * 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c
	 * ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。 <br>
	 * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。 <br>
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 * 
	 * @param key
	 * @param string
	 *            一个或多个string值
	 * @return result 执行 RPUSH 操作后，表的长度。
	 */
	public Long rpush(String key, String... string) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpush(key, string);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。 <br>
	 * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做。
	 * 
	 * @param key
	 * @param string
	 *            一个或多个string值
	 * @return result RPUSHX 命令执行之后，表的长度。
	 */
	public Long rpushx(String key, String... string) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpushx(key, string);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头：
	 * 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH
	 * mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。<br>
	 * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 <br>
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 * 
	 * @param key
	 * @param string
	 *            一个或多个string值
	 * @return result 执行 LPUSH 命令后，列表的长度。
	 */
	public Long lpush(String key, String... string) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpush(key, string);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。<br>
	 * 和 LPUSH 命令相反，当 key 不存在时， LPUSHX 命令什么也不做。
	 * 
	 * @param key
	 * @param string
	 *            一个或多个string值
	 * @return result 执行 LPUSHX 命令后，列表的长度。
	 */
	public Long lpushx(String key, String... string) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpushx(key, string);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回列表 key 的长度。<br>
	 * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .<br>
	 * 如果 key 不是列表类型，返回一个错误。
	 * 
	 * @param key
	 * @return result 列表 key 的长度。
	 */
	public Long llen(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.llen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。<br>
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * <br>
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。<br>
	 * 超出范围的下标值不会引起错误。
	 * 
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束如果为负数，则尾部开始计算
	 * @return result 一个列表，包含指定区间内的元素。
	 */
	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。<br>
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * <br>
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。<br>
	 * 当 key 不是列表类型时，返回一个错误。<br>
	 * 超出范围的下标值不会引起错误。
	 * 
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置，则尾部开始计算
	 * @return result 命令执行成功时，返回 ok 。
	 */
	public String ltrim(String key, long start, long end) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ltrim(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。<br>
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * <br>
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。<br>
	 * 如果 key 不是列表类型，返回一个错误。
	 * 
	 * @param key
	 * @param index
	 *            下标
	 * @return result 列表中下标为 index 的元素。 如果 index 参数的值不在列表的区间范围内，返回 nil 。
	 */
	public String lindex(String key, long index) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lindex(key, index);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。<br>
	 * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。<br>
	 * 关于列表下标的更多信息，请参考 LINDEX 命令。
	 * 
	 * @param key
	 * @param index
	 *            下标
	 * @param value
	 *            值
	 * @return result 操作成功返回 ok ，否则返回错误信息。
	 */
	public String lset(String key, long index, String value) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lset(key, index, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。<br>
	 * count 的值可以是以下几种：<br>
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。<br>
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。<br>
	 * count = 0 : 移除表中所有与 value 相等的值。
	 * 
	 * @param key
	 * @param count
	 *            要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
	 * @param value
	 *            要匹配删除的值
	 * @return result 被移除元素的数量。 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM
	 *         命令总是返回 0 。
	 */
	public Long lrem(String key, long count, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrem(key, count, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除并返回列表 key 的头元素。
	 * 
	 * @param key
	 * @return result 列表的头元素。 当 key 不存在时，返回 nil 。
	 */
	public String lpop(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除并返回列表 key 的尾元素。
	 * 
	 * @param key
	 * @return result 列表的尾元素。 当 key 不存在时，返回 nil 。
	 */
	public String rpop(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。<br>
	 * 当 pivot 不存在于列表 key 时，不执行任何操作。<br>
	 * 当 key 不存在时， key 被视为空列表，不执行任何操作。<br>
	 * 如果 key 不是列表类型，返回一个错误。
	 * 
	 * @param key
	 * @param where
	 *            前面插入或后面插入
	 * @param pivot
	 *            相对位置的内容
	 * @param value
	 *            插入的内容
	 * @return result 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。 如果 key
	 *         不存在或为空列表，返回 0 。
	 */
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/** ========================对存储结构为Set（集合）类型的操作======================== */

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。<br>
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。<br>
	 * 当 key 不是集合类型时，返回一个错误。
	 * 
	 * @param key
	 * @param member
	 *            元素
	 * @return result 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 */
	public Long sadd(String key, String... member) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sadd(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回集合 key 中的所有成员。 不存在的 key 被视为空集合。
	 * 
	 * @param key
	 * @return result 集合中的所有成员。
	 */
	public Set<String> smembers(String key) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.smembers(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * 
	 * 当 key 不是集合类型，返回一个错误。
	 * 
	 * @param key
	 * @param member
	 *            元素
	 * @return result 被成功移除的元素的数量，不包括被忽略的元素。
	 */
	public Long srem(String key, String member) {
		ShardedJedis shardedJedis = getShardedJedis();

		Long result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srem(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

		} finally {
			shardedJedis.close();
		}
		return result;
	}

	/**
	 * 移除并返回集合中的一个随机元素。<br>
	 * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
	 * 
	 * @param key
	 * @return result 被移除的随机元素。 当 key 不存在或 key 是空集时，返回 nil 。
	 */
	public String spop(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.spop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回集合中的一个随机元素。
	 * 
	 * @param key
	 * @return result 返回一个元素；如果集合为空，返回 nil 。
	 */
	public String srandmember(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srandmember(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回集合中的count个随机元素。<br>
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count
	 * 大于等于集合基数，那么返回整个集合。<br>
	 * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
	 * 
	 * @param key
	 * @param count
	 *            元素个数
	 * @return result 返回一个数组；如果集合为空，返回空数组。
	 */
	public List<String> srandmember(String key, int count) {
		List<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srandmember(key, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回集合 key 的基数(集合中元素的数量)。
	 * 
	 * @param key
	 * @return result 集合的基数。 当 key 不存在时，返回 0 。
	 */
	public Long scard(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.scard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员。
	 * 
	 * @param key
	 * @param member
	 *            元素
	 * @return result 如果member元素是集合的成员，返回 1。如果 member元素不是集合的成员，或 key不存在，返回 0。
	 */
	public Boolean sismember(String key, String member) {
		Boolean result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sismember(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/** ====================对存储结构为SortedSet（有序集合）类型的操作==================== */

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。<br>
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该
	 * member 在正确的位置上。<br>
	 * score 值可以是整数值或双精度浮点数。<br>
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 * 
	 * @param key
	 * @param score
	 * @param member
	 *            成员
	 * @return result 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 */
	public Long zadd(String key, double score, String member) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zadd(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递增(从小到大)来排序。<br>
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br>
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。<br>
	 * 超出范围的下标并不会引起错误。
	 * 
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return result 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 */
	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递减(从大到小)来排列。<br>
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br>
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。<br>
	 * 超出范围的下标并不会引起错误。
	 * 
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return result 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 */
	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。 当 key 存在但不是有序集类型时，返回一个错误。
	 * 
	 * @param key
	 * @param member
	 *            成员
	 * @return result 被成功移除的成员的数量，不包括被忽略的成员。
	 */
	public Long zrem(String key, String... member) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrem(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 。<br>
	 * 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key
	 * increment member 。<br>
	 * 当 key 不是有序集类型时，返回一个错误。
	 * 
	 * @param key
	 * @param score
	 *            score 值可以是整数值或双精度浮点数。
	 * @param member
	 *            成员
	 * @return result member成员的新 score 值，以字符串形式表示。
	 */
	public Double zincrby(String key, double score, String member) {
		Double result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zincrby(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。<br>
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 * 
	 * @param key
	 * @param member
	 *            成员
	 * @return result 如果 member 是有序集 key 的成员，返回 member 的排名。 如果 member 不是有序集 key
	 *         的成员，返回 nil 。
	 */
	public Long zrank(String key, String member) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrank(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。<br>
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 
	 * @param key
	 * @param member
	 *            成员
	 * @return result 如果 member 是有序集 key 的成员，返回 member 的排名。 如果 member 不是有序集 key
	 *         的成员，返回 nil 。
	 */
	public Long zrevrank(String key, String member) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrank(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 的基数。
	 * 
	 * @param key
	 * @return result 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
	 */
	public Long zcard(String key) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zcard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。<br>
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
	 * 
	 * @param key
	 * @param member
	 *            成员
	 * @return result member 成员的 score 值，以字符串形式表示。
	 */
	public Double zscore(String key, String member) {
		Double result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zscore(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递增(从小到大)次序排列。
	 * 
	 * @param key
	 * @param min
	 *            最小分值
	 * @param max
	 *            最大分值
	 * @return result 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 */
	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score
	 * 值递增(从小到大)次序排列。<br>
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset
	 * 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
	 * 
	 * @param key
	 * @param min
	 *            最小分值
	 * @param max
	 *            最大分值
	 * @param offset
	 *            要跳过的元素数量
	 * @param count
	 *            跳过offset个指定的元素之后，要返回多少个对象
	 * @return result 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 */
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 
	 * @param key
	 * @param min
	 *            最小分值
	 * @param max
	 *            最大分值
	 * @return result score 值在 min 和 max 之间的成员的数量。
	 */
	public Long zcount(String key, double min, double max) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zcount(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score
	 * 值递减(从大到小)的次序排列。
	 * 
	 * @param key
	 * @param min
	 *            最小分值
	 * @param max
	 *            最大分值
	 * @return result 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 */
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score
	 * 值递减(从大到小)的次序排列。
	 * 
	 * @param key
	 * @param min
	 *            最小分值
	 * @param max
	 *            最大分值
	 * @param offset
	 *            要跳过的元素数量
	 * @param count
	 *            跳过offset个指定的元素之后，要返回多少个对象
	 * @return result 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 */
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。<br>
	 * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。<br>
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br>
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return result 被移除成员的数量。
	 */
	public Long zremrangeByRank(String key, int start, int end) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/**
	 * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 
	 * @param key
	 * @param start
	 *            开始分值
	 * @param end
	 *            结束分值
	 * @return result 被移除成员的数量。
	 */
	public Long zremrangeByScore(String key, double start, double end) {
		Long result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	/** ===============================其它方法=============================== */

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeWithScores(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Jedis getShard(String key) {
		Jedis result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getShard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public JedisShardInfo getShardInfo(String key) {
		JedisShardInfo result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getShardInfo(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public String getKeyTag(String key) {
		String result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getKeyTag(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Collection<JedisShardInfo> getAllShardInfo() {
		Collection<JedisShardInfo> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getAllShardInfo();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public Collection<Jedis> getAllShards() {
		Collection<Jedis> result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getAllShards();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

	public ShardedJedisPipeline pipelined() {
		ShardedJedisPipeline result = null;

		ShardedJedis shardedJedis = getShardedJedis();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.pipelined();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			shardedJedis.close();
		}

		return result;
	}

}
