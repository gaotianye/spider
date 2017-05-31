package com.gaotianye.springboot.spider.repository;

import java.util.List;

public interface IRepository {
	/**
	 * 拉取数据
	 * @return
	 */
	String poll();
	
	/**
	 * 添加数据(lower)
	 * @param nextUrl
	 */
	void add(String nextUrl);
	
	/**
	 * 添加数据(higher)
	 * @param nexturl
	 */
	void addHigher(String nexturl);
	
	/**
	 * 批量添加数据（管道方式）
	 * @param urls
	 */
	void addAll(List<String> urls);
}
