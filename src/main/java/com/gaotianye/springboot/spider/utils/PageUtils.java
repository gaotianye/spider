package com.gaotianye.springboot.spider.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据url下载content信息
 * @author gaotianye
 */
public class PageUtils {
	static Logger logger = LoggerFactory.getLogger(PageUtils.class);
	
	public static String getContent(String url){
		//下载内容
		String content = null;
		//获取一个构建器，构建一个client对象
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		//封装http请求
		HttpGet request = new HttpGet(url);
		try {
			long start_time = System.currentTimeMillis();
			//执行get请求
			CloseableHttpResponse response = client.execute(request);
			//获取页面内容实体对象
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			logger.info("页面下载成功，消耗时间：{},url：{}",System.currentTimeMillis()-start_time,url);
		} catch (Exception e) {
			logger.error("页面下载失败，url：{},错误信息如下：{}",url,e.getMessage());
		}
		return content.trim();
	}
}
