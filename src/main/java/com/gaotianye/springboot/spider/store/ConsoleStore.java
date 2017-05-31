package com.gaotianye.springboot.spider.store;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.gaotianye.springboot.spider.page.Page;
import com.gaotianye.springboot.spider.process.JDProcessImpl;

/**
 * 控制台存储，仅用于测试，方便展示结果
 * @author gaotianye
 */
@Component
public class ConsoleStore implements IStore {
	private static Logger logger = LoggerFactory.getLogger(ConsoleStore.class);
	
	@Override
	public void store(Page page) {
		String url = page.getUrl();
		logger.info("url is : "+url);
		Map<String, String> values = page.getValues();
		if(values.size()>0){
			for (String key : values.keySet()) {
				logger.info("key is : "+key+",value is : "+values.get(key));
			}
		}else{
			logger.info("page values is null");
		}
	}
}
