package com.gaotianye.springboot.spider.before;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.config.DownLoadConfig;
import com.gaotianye.springboot.spider.config.ProcessConfig;
import com.gaotianye.springboot.spider.config.RepositoryConfig;
import com.gaotianye.springboot.spider.config.StoreConfig;

/**
 * 系统启动时立即执行，主要目的是   ###展示基本信息###
 * 可以有多个类，通过@Order(value=1)注解来表示先后顺序
 * 优先级是从小到大
 * @author gaotianye
 */
@Component
public class BeforeStarter implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(BeforeStarter.class);
	
	@Resource
	private DownLoadConfig downloadConfig;
	@Resource
	private ProcessConfig processConfig;
	@Resource
	private StoreConfig storeConfig;
	@Resource
	private RepositoryConfig repositoryConfig;
	
	
	@Override
	public void run(String... arg0) throws Exception {
		//download--下载实现类
		String download = downloadConfig.getEntity();
		//parse--解析实现类
		String process = processConfig.getEntity();
		//store--存储实现类
		String store = storeConfig.getEntity();
		//repository--队列实现类
		String repository = repositoryConfig.getEntity();
		
		logger.info("#########展示爬虫的基本信息如下#########");
		logger.info("#########下载实现类：{}",download);
		logger.info("#########解析实现类：{}",process);
		logger.info("#########存储实现类：{}",store);
		logger.info("#########队列实现类：{}",repository);
	}

}
