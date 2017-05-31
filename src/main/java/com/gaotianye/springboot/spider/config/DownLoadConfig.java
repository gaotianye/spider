package com.gaotianye.springboot.spider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * 下载实体类 配置文件
 * @author gaotianye
 */
@ConfigurationProperties(prefix = "download")
@Component
public class DownLoadConfig {
	private String entity;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
}
