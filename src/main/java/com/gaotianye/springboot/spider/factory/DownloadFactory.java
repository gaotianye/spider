package com.gaotianye.springboot.spider.factory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.gaotianye.springboot.spider.config.DownLoadConfig;
import com.gaotianye.springboot.spider.download.IDownload;

/**
 * 获取下载实体类的工厂
 * @author gaotianye
 */
@Component
public class DownloadFactory {
	Logger logger = LoggerFactory.getLogger(DownloadFactory.class);
	@Resource
	private DownLoadConfig config;
	
	public IDownload getDownLoad(){
		String entity = config.getEntity();
		IDownload download = null;
			try {
				download = (IDownload) Class.forName(entity).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error("DownloadFactory 创建失败,原因如下：{}",e);
			}
		return download;
	}
}
