package com.gaotianye.springboot.spider.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * NO3:上下文创建完成后执行的事件监听器
 * 执行时间：spring boot上下文context创建完成,但此时spring中的bean是没有完全加载完成的。
 * 补充：在获取完上下文后，可以将上下文传递出去做一些额外的操作。
 * 		在该监听器中是无法获取自定义的bean和进行操作的。
 * @author gaotianye
 */
public class MyApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
	private Logger logger = LoggerFactory.getLogger(MyApplicationPreparedEventListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableApplicationContext cac = event.getApplicationContext();
        passContextInfo(cac);		
	}
	
	/**
	 * 传递上下文
	 * @param cac
	 */
	private void passContextInfo(ConfigurableApplicationContext cac) {
		//dosomething()
		logger.info("########################sc#########################");
	}

}
