package com.gaotianye.springboot.spider.utils;

public class StringUtils {
	/**
	 * 检查Object数组是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmptyByArray(Object[] obj){
		if(obj!=null && obj.length>0){
			return false;
		}
		return true;
	}
	/**
	 * 检查字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str!=null && str.trim().length()>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 字符串反转
	 * @param s
	 * @return
	 */
	public static String reverse(String s) {
		  char[] array = s.toCharArray();
		  String reverse = "";
		  for (int i = array.length - 1; i >= 0; i--)
		   reverse += array[i];
		  return reverse;
	 }
}
