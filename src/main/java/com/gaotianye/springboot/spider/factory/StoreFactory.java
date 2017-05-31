package com.gaotianye.springboot.spider.factory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.config.StoreConfig;
import com.gaotianye.springboot.spider.store.IStore;


/**
 * 存储工厂
 * @author gaotianye
 */
@Component
public class StoreFactory {
	Logger logger = LoggerFactory.getLogger(StoreFactory.class);
	
	@Resource
	private StoreConfig storeConfig;
	
	public IStore getStore() {
		String entity = storeConfig.getEntity();
		IStore store = null;
		try {
			store = (IStore) Class.forName(entity).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error("StoreFactory 创建失败,原因如下：{}",e);
		}
		return store;
	}
}
