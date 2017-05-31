package com.gaotianye.springboot.spider.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaotianye.springboot.spider.constant.Constant;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * TODO redis如何做到去重url？
 * 解决思路：list和set二个集合。首先，将url存到set中，如果成功，则插入到list中。
 *           如果失败，则表示是重复的url，不插入到list中。
 * 弊端：url如果有上亿条，用set做存储，相当耗费内存（set是不删除的，而list是可以删除的）。
 * @author gaotianye
 *
 */
public class RedisUtils {
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
	
	JedisPool jedisPool = null;
	public RedisUtils(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxTotal(100);
		poolConfig.setMaxWaitMillis(10000);
		poolConfig.setTestOnBorrow(true);
		jedisPool = new JedisPool(poolConfig, "192.168.3.39", 6379);
	}
	
	/**
	 * 添加数据,首先往set集合里面添加，如果添加成功，再往list集合中添加。
	 * @param key
	 * @param url
	 */
	public void add(String key, String url) {
		Jedis jedis = jedisPool.getResource();
		Long sadd = jedis.sadd("set_"+key, url);
		if(sadd==1){
			jedis.lpush(key, url);
		}
		jedis.close();
	}
	
	/**
	 * 批量添加数据
	 * 首先也是往set集合中添加数据，如果添加成功，则再往list集合中添加。
	 * TODO 如果使用管道，那么就无法使用set（因为管道期间，set后并不能获取值）
	 * @param key
	 * @param urls
	 */
	public void addAll(List<String> urls){
		Jedis jedis = jedisPool.getResource();
		for (String url : urls) {
			if(url.startsWith("http://list.jd.com/") || url.startsWith("https://list.jd.com/")){
				//TODO 是否有必要 通过参数来传递key？
				Long sadd = jedis.sadd("set_"+Constant.REDIS_LIST_KEY, url);
				if(sadd==1){
					jedis.lpush(Constant.REDIS_LIST_KEY, url);
				}
			}else{
				Long sadd = jedis.sadd("set_"+Constant.REDIS_ITEM_KEY, url);
				if(sadd==1){
					jedis.lpush(Constant.REDIS_ITEM_KEY, url);
				}
			}
		}
	}
	
	/**
	 * 拉取数据
	 * @param key
	 * @return
	 */
	public String poll(String key) {
		Jedis resource = jedisPool.getResource();
		String result = resource.rpop(key);
		resource.close();
		return result;
	}
}
