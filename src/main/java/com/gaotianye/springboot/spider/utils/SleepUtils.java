package com.gaotianye.springboot.spider.utils;

import java.util.Random;

/**
 * Sleep工具类
 * @author gaotianye
 *
 */
public class SleepUtils {
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
		try {
			Thread.sleep(nextInt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
