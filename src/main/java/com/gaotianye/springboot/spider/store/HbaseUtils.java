package com.gaotianye.springboot.spider.store;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.before.BeforeStarter;

@Component
public class HbaseUtils {
	private static Logger logger = LoggerFactory.getLogger(HbaseUtils.class);
	
	private HBaseAdmin admin = null;
	private Configuration conf = null;

	/**
	 * 构造函数加载配置
	 */
	public HbaseUtils() {
		conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "mmm:2181");
		conf.set("hbase.rootdir", "hdfs://mmm:9000/hbase");
		try {
			admin = new HBaseAdmin(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * rowFilter的使用
	 * 
	 * @param tableName
	 * @param reg
	 * @throws Exception
	 */
	public void getRowFilter(String tableName, String reg) throws Exception {
		logger.info("----filter查询  start----");
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
		// Filter
		RowFilter rowFilter = new RowFilter(CompareOp.NOT_EQUAL, new RegexStringComparator(reg));
		scan.setFilter(rowFilter);
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			logger.info(new String(result.getRow()));
		}
		logger.info("----filter查询  end----");
	}
	
	/**
	 * scan查询
	 * @param tableName
	 * @param family
	 * @param qualifier
	 * @throws Exception
	 */
	public void getScanData(String tableName, String family, String qualifier) throws Exception {
		logger.info("----scan查询  start----");
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
		scan.addColumn(family.getBytes(), qualifier.getBytes());
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			if (result.raw().length == 0) {
				logger.info(tableName + " 表数据为空！");
			} else {
				for (KeyValue kv : result.raw()) {
					logger.info(new String(kv.getKey()) + "\t" + new String(kv.getValue()));
				}
			}
		}
		logger.info("----scan查询  end----");
	}

	/**
	 * 删除表
	 * @param tableName
	 */
	public void deleteTable(String tableName) {
		logger.info("----删除表 start----");
		try {
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				logger.info(tableName + "表删除成功！");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(tableName + "表删除失败！");
		}
		logger.info("----删除表 end----");
	}

	/**
	 * 删除一条记录
	 * 
	 * @param tableName
	 * @param rowKey
	 */
	public void deleteOneRecord(String tableName, String rowKey) {
		logger.info("----删除一条记录 start----");
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Delete delete = new Delete(rowKey.getBytes());
		try {
			table.delete(delete);
			logger.info(rowKey + "记录删除成功！");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(rowKey + "记录删除失败！");
		}
		logger.info("----删除一条记录 end----");
	}

	/**
	 * 获取表的所有数据
	 * 
	 * @param tableName
	 */
	public void getALLData(String tableName) {
		logger.info("----获取表" + tableName + "的所有数据start----");
		try {
			HTable hTable = new HTable(conf, tableName);
			Scan scan = new Scan();
			ResultScanner scanner = hTable.getScanner(scan);
			for (Result result : scanner) {
				if (result.raw().length == 0) {
					logger.info(tableName + " 表数据为空！");
				} else {
					for (KeyValue kv : result.raw()) {
						logger.info(new String(kv.getKey()) + "\t" + new String(kv.getValue()));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("----获取表" + tableName + "的所有数据end----");

	}

	/**
	 *  添加一条记录
	 * @param tableName
	 * @param row
	 * @param columnFamily
	 * @param column
	 * @param data
	 * @throws IOException
	 */
	public void put(String tableName, String row, String columnFamily, String column, String data) throws IOException {
		logger.info("----添加一条记录 start----");
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Put p1 = new Put(Bytes.toBytes(row));
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
		table.put(p1);
		logger.info("put'" + row + "'," + columnFamily + ":" + column + "','" + data + "'");
		logger.info("----添加一条记录 end----");
	}

	/**
	 * 查询所有表名
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getALLTable() throws Exception {
		logger.info("----查询所有表名 start----");
		ArrayList<String> tables = new ArrayList<String>();
		if (admin != null) {
			HTableDescriptor[] listTables = admin.listTables();
			if (listTables.length > 0) {
				for (HTableDescriptor tableDesc : listTables) {
					tables.add(tableDesc.getNameAsString());
					logger.info(tableDesc.getNameAsString());
				}
			}
		}
		logger.info("----查询所有表名 end----");
		return tables;
	}

	/**
	 * 创建一张表
	 * 
	 * @param tableName
	 * @param column
	 * @throws Exception
	 */
	public void createTable(String tableName, String... columns) throws Exception {
		logger.info("----创建表 start----");
		if (admin.tableExists(tableName)) {
			logger.info(tableName + "表已经存在！");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (String column : columns) {
				tableDesc.addFamily(new HColumnDescriptor(column.getBytes()));
			}
			admin.createTable(tableDesc);
			logger.info(tableName + "表创建成功！");
		}
		logger.info("----创建表 end----");
	}
}
