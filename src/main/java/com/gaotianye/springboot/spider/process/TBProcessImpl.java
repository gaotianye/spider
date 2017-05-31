package com.gaotianye.springboot.spider.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.gaotianye.springboot.spider.page.Page;

/**
 * 淘宝解析类
 * @author gaotianye
 */
@Component
public class TBProcessImpl implements IProcess {
	private static Logger logger = LoggerFactory.getLogger(TBProcessImpl.class);
	
	@Override
	public boolean process(Page page) {
		logger.info("taobao.........process............................");
		return true;
	}

}
