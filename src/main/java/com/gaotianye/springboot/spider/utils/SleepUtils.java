package com.gaotianye.springboot.spider.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaotianye.springboot.spider.starter.StartController;

/**
 * Sleep工具类
 * @author gaotianye
 *
 */
public class SleepUtils {
	private static Logger logger = LoggerFactory.getLogger(SleepUtils.class);
	/**
	 * 指定休息时间
	 * @param times
	 */
	public static void sleep(long times){
		try {
			Thread.sleep(times);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 随机生成休息时间
	 */
	public static void sleep(){
		Random random = new Random();
		//至少5s
		int nextInt = random.nextInt(20000)+5000;
		logger.info("sleep {}s ",nextInt/1000);
		try {
			Thread.sleep(nextInt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
