package com.gaotianye.springboot.spider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Xpath配置项
 * @author gaotianye
 */
@ConfigurationProperties(prefix = "xpath")
@Component
public class XpathConfig {
	//标题名称
	private String title;
	//图片位置
	private String picture;
	//商品价格
	private String price;
	//规格参数
	private String specifications;
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
