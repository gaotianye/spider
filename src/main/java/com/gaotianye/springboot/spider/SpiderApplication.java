package com.gaotianye.springboot.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Springboot启动类
 * @author gaotianye
 *
 */
@SpringBootApplication
public class SpiderApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpiderApplication.class);
		//TODO spring boot 启动监听类
		/*app.addListeners(new MyApplicationStartedEventListener());
		//spring boot 配置环境事件监听
		app.addListeners(new MyApplicationEnvironmentPreparedEventListener());
		//spring boot 上下文创建完成后执行的事件监听器
		app.addListeners(new MyApplicationPreparedEventListener());
		//TODO spring boot启动异常时执行事件
		app.addListeners(new MyApplicationFailedEventListener());
		// 在BeforeStarter之前执行
		app.addListeners(new MyApplicationStartupListener());*/
		app.run(args);
	}
}
