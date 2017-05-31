package com.gaotianye.springboot.spider.repository;


import java.util.List;

import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.constant.Constant;
import com.gaotianye.springboot.spider.utils.StringUtils;

@Component
public class RedisRepository implements IRepository {
	RedisUtils redisUtils = new RedisUtils();
	
	/**
	 * 拉取数据
	 */
	@Override
	public String poll() {
		String url = this.redisUtils.poll(Constant.REDIS_LIST_KEY);
		if(StringUtils.isEmpty(url)){
			url = this.redisUtils.poll(Constant.REDIS_ITEM_KEY);
		}
		return url;
	}
	
	/**
	 * 添加数据(list)
	 */
	@Override
	public void add(String nextUrl) {
		this.redisUtils.add(Constant.REDIS_LIST_KEY, nextUrl);
	}
    
	/**
	 * 批量添加数据（管道方式）
	 */
	@Override
	public void addAll(List<String> urls) {
		this.redisUtils.addAll(urls);
	}
	
	/**
	 * 添加数据(item)
	 */
	@Override
	public void addHigher(String nexturl) {
		this.redisUtils.add(Constant.REDIS_LIST_KEY, nexturl);
	}
}
