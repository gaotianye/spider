package com.gaotianye.springboot.spider.utils;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepareUtils {
	private static Logger logger = LoggerFactory.getLogger(PrepareUtils.class);
	
	public static void check(){
		logger.info("I am checking........");
	}
	/**
	 * 获取Price的pduid
	 * @return
	 */
	public static String getPdUid(){
		String time = new Date().getTime()+"_";
		Random random = new Random();
		for(int i = 0;i<5;i++){
			time += random.nextInt(10);
		}
		return MD5Utils.getMD5(time);
	}
}
