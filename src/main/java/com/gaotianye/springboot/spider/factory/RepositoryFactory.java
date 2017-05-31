package com.gaotianye.springboot.spider.factory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.config.RepositoryConfig;
import com.gaotianye.springboot.spider.repository.IRepository;

@Component
public class RepositoryFactory {
	Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);
	@Resource
	private RepositoryConfig config;
	
	public IRepository getRepository(){
		String entity = config.getEntity();
		IRepository repository = null;
			try {
				repository = (IRepository) Class.forName(entity).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error("RepositoryFactory 创建失败,原因如下：{}",e);
			}
		return repository;
	}
}
