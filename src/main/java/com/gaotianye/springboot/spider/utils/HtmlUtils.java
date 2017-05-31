package com.gaotianye.springboot.spider.utils;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * TODO 解析工具类(用途不太大)
 * @author gaotianye
 */
public class HtmlUtils {
	/**
	 * 获取指定标签的内容
	 */
	public static String getText(TagNode rootNode,String xpath){
		String result = null;
		try {
			Object[] evaluateXPath = rootNode.evaluateXPath(xpath);
			if(!StringUtils.isEmptyByArray(evaluateXPath)){
				TagNode tagNode = (TagNode)evaluateXPath[0];
				result = tagNode.getText().toString().trim();
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取指定标签指定属性的值
	 */
	public static String getAttributeByName(TagNode rootNode,String xpath,String attr){
		String result = null;
		try {
			Object[] evaluateXPath = rootNode.evaluateXPath(xpath);
			if(!StringUtils.isEmptyByArray(evaluateXPath)){
				TagNode tagNode = (TagNode)evaluateXPath[0];
				result = tagNode.getAttributeByName(attr);
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
}
