package com.gaotianye.springboot.spider.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下载的页面信息
 * @author gaotianye
 */
public class Page {
	/*
	 * 1、基本信息
	 */
	//下载的内容
	private String content;
	
	//下载的url
	private String url;
	
	/*
	 * 2、list、search等页面抓取的临时需要保存的urls
	 */
	private List<String> urls = new ArrayList<String>();
	
	/*
	 * 3、商品详情页面：保存商品的基本信息
	 */
	private Map<String, String> values = new HashMap<String, String>();
	
	/*
	 * 4、用于hbase的rowkey
	 */
	private String goodsId;

	//================================================================
	public String getGoodsId() {
		return goodsId;
	}
	
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public List<String> getUrls() {
		return urls;
	}
	
	public void addUrl(String url){
		this.urls.add(url);
	}
	
	public Map<String, String> getValues() {
		return values;
	}

	public void addField(String key,String value){
		this.values.put(key, value);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
