package com.gaotianye.springboot.spider.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * NO1:spring boot 启动监听类
 * 执行时间：Springboot启动开始时执行的事件
 * 补充：在该事件中可以获取到SpringApplication对象，可做一些执行前的设置
 * @author gaotianye
 */
public class MyApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
	private Logger logger = LoggerFactory.getLogger(MyApplicationStartedEventListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
        SpringApplication app = event.getSpringApplication();
        
        logger.info("################MyApplicationStartedEventListener#################");
	}

}
