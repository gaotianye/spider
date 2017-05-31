package com.gaotianye.springboot.spider.repository;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.utils.StringUtils;

@Component
public class QueueRepository implements IRepository {
	/**
	 * url队列
	 */
	Queue<String> HigherQueueList = new ConcurrentLinkedQueue<String>();
	Queue<String> LowerQueueList = new ConcurrentLinkedQueue<String>();
	
	/**
	 * 获取数据
	 */
	@Override
	public String poll() {
		String url = this.HigherQueueList.poll();
		//如果高队列中不存在url，则从低队列中获取
		if(StringUtils.isEmpty(url)){
			url = this.LowerQueueList.poll();
		}
		return url;
	}
	
	/**
	 * 添加数据(往low中添加)
	 */
	@Override
	public void add(String nextUrl) {
		this.LowerQueueList.add(nextUrl);
	}
	
	/**
	 * 批量添加数据
	 */
	@Override
	public void addAll(List<String> urls) {
		for (String url : urls) {
			if(url.startsWith("http://list.jd.com/") || url.startsWith("https://list.jd.com/")){
				this.HigherQueueList.add(url);
			}else{
				this.LowerQueueList.add(url);
			}
		}
	}
	
	/**
	 * 添加数据（higher）
	 */
	@Override
	public void addHigher(String nexturl) {
		this.HigherQueueList.add(nexturl);
	}

}
