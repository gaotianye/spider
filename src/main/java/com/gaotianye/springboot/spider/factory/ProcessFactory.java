package com.gaotianye.springboot.spider.factory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.config.ProcessConfig;
import com.gaotianye.springboot.spider.process.IProcess;

/**
 * 解析工厂
 * @author gaotianye
 */
@Component
public class ProcessFactory {
	Logger logger = LoggerFactory.getLogger(ProcessFactory.class);
	@Resource
	private ProcessConfig processConfig;
	
	public IProcess getProcess() {
		String entity = processConfig.getEntity();
		IProcess process = null;
		try {
			process = (IProcess) Class.forName(entity).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error("ProcessFactory 创建失败,原因如下：{}",e);
		}
		return process;
	}
}
