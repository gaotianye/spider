package com.gaotianye.springboot.spider.constant;

public class Constant {
	/**
	 * HBASE 信息
	 */
	// 表名
	public static final String TABLE_NAME = "spider";
	// 列簇1 商品信息
	public static final String COLUMNFAMILY_1 = "goods";
	// 列簇1中的列
	public static final String COLUMNFAMILY_1_TITLE = "title";
	public static final String COLUMNFAMILY_1_GOODS_URL = "url";
	public static final String COLUMNFAMILY_1_PIC_URL = "picture";
	public static final String COLUMNFAMILY_1_PRICE = "price";
	public static final String COLUMNFAMILY_1_INFO = "goodsInfo";
	// 列簇2 商品规格
	public static final String COLUMNFAMILY_2 = "specific";
	public static final String COLUMNFAMILY_2_INFO = "specifications";
	
	/**
	 * Redis 信息
	 */
	// redis存储list数据类型的key(商品列表)
	public static final String REDIS_LIST_KEY = "test_spider_list";
	// redis存储list数据类型的key(商品详情)
	public static final String REDIS_ITEM_KEY = "test_spider_item";
	
	/**
	 * 线程池
	 */
	public static final int NTHREAD = 8;
}
