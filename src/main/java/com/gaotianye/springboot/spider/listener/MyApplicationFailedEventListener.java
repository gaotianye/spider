package com.gaotianye.springboot.spider.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * NO4：spring boot启动异常时执行事件
 * 执行时间：在异常发生时，最好是添加虚拟机对应的钩子进行资源的回收与释放，能友善的处理异常信息。
 * 		在spring boot中已经为大家考虑了这一点，默认情况开启了对应的功能：
	   public void registerShutdownHook() {
	        if (this.shutdownHook == null) {
	            // No shutdown hook registered yet.
	            this.shutdownHook = new Thread() {
	                @Override
	                public void run() {
	                    doClose();
	                }
	            };
	            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
	        }
	    }
	    
	    在doClose()方法中进行资源的回收与释放。
 * 
 * @author gaotianye
 */
public class MyApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
	private Logger logger = LoggerFactory.getLogger(MyApplicationFailedEventListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationFailedEvent event) {
        Throwable throwable = event.getException();
        handleThrowable(throwable);
	}
	/**
	 * 处理异常
	 * @param throwable
	 */
	private void handleThrowable(Throwable throwable) {
		logger.info("##################EXCEPTION#######################");
	}

}
