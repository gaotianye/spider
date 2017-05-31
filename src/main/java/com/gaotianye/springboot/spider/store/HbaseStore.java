package com.gaotianye.springboot.spider.store;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.constant.Constant;
import com.gaotianye.springboot.spider.page.Page;

/**
 * Hbase存储，分为两个列簇
 * 列族1：标题、价格、图片地址
 * 列族2：规格参数
 * create 'spider','goodsinfo','spec'
 * alter 'spider',{NAME=>'goodsinfo',VERSIONS=>30}
 * 
 * rowkey：使用商品编号和网站的唯一标识
 * 为了解决热点数据问题：rowkey需要这样设计：商品编号倒置_网站唯一标识
 * 
 * @author gaotianye
 */
@Component
public class HbaseStore implements IStore {
	private static Logger logger = LoggerFactory.getLogger(HbaseStore.class);
	//TODO 不清楚为什么为null
	/*@Resource
	private HbaseUtils hbaseUtils;*/
	
	@Override
	public void store(Page page) {
		HbaseUtils hbaseUtils = new HbaseUtils();
		String rowKey = page.getGoodsId();
		Map<String, String> values = page.getValues();
		if(hbaseUtils!=null){
			try {
				//如果表不存在，则创建
				//创建二个列族  goods 和 specifications
				hbaseUtils.createTable(Constant.TABLE_NAME, Constant.COLUMNFAMILY_1,Constant.COLUMNFAMILY_2);
				//添加数据 ：String tableName,String rowKey,String columnFamily, String column,String data
				hbaseUtils.put(Constant.TABLE_NAME, rowKey, Constant.COLUMNFAMILY_1, Constant.COLUMNFAMILY_1_GOODS_URL, page.getUrl());
				hbaseUtils.put(Constant.TABLE_NAME, rowKey, Constant.COLUMNFAMILY_1, Constant.COLUMNFAMILY_1_TITLE, values.get("title"));
				hbaseUtils.put(Constant.TABLE_NAME, rowKey, Constant.COLUMNFAMILY_1, Constant.COLUMNFAMILY_1_PRICE, values.get("price"));
				hbaseUtils.put(Constant.TABLE_NAME, rowKey, Constant.COLUMNFAMILY_1, Constant.COLUMNFAMILY_1_PIC_URL, values.get("picture"));
				hbaseUtils.put(Constant.TABLE_NAME, rowKey, Constant.COLUMNFAMILY_1, Constant.COLUMNFAMILY_1_INFO, values.get("goodsInfo"));
				
				hbaseUtils.put(Constant.TABLE_NAME, rowKey, Constant.COLUMNFAMILY_2, Constant.COLUMNFAMILY_2_INFO, values.get("specifications"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			logger.info("hbaseUtils is null");
		}
	}
}
