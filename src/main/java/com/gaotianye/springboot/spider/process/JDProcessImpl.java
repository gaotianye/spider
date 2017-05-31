package com.gaotianye.springboot.spider.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.config.XpathConfig;
import com.gaotianye.springboot.spider.page.Page;
import com.gaotianye.springboot.spider.utils.HtmlUtils;
import com.gaotianye.springboot.spider.utils.PageUtils;
import com.gaotianye.springboot.spider.utils.PrepareUtils;
import com.gaotianye.springboot.spider.utils.SleepUtils;
import com.gaotianye.springboot.spider.utils.StringUtils;

/**
 * 京东解析类 之 酒类、手机类
 * 前言：
 * 1）列表页面分为多种，list、search等
 * 2）京东页面有多种类型，不同类型产品可能使用的列表页面不一样。但是商品详情好像一致
 * 3）
 * TODO 问题：
 * 1）list页面：抓取url时，发现有的商品，div上带有alg="-1"（现象：只有1个图片）。
 *    这样的url，href指定的url，返回302 location指定到item（详情）页面。
 *    但是爬虫抓取不到这样的信息。即爬取获取不到url。
 *    但是当前页面下面的list中的数量，还是相等的
 *    （例如全部是60个，但是有几个抓取不到，按理说应该小于60，但是发现，它还抓取一些其他的页面，正好满足60个）
 * 2）有时候 同样的商品详情，js脚本还不一样。例如：商品详情图片，有的是attribute=src，有的src获取为空，
 *    通过data-origin来转换。
 * 3）更加崩溃的是：Xpath解析。相同产品，获取类似内容的Xpath都有可能不同。
 * 		甚至获取多重数据，有的需要角标，需要防止角标越界。
 * 		所以不建议这么使用：TagNode imgNode = liNode.getChildTags()[0].getChildTags()[0];
 * 4）找一个通用的Xpath，能把人搞疯
 *    有的   姓名：高天野     有的就是  姓名：
 *    于是split[1]就会报错
 * 5）TODO 如何做到报异常之后还能够继续执行？
 * 6）
 * @author gaotianye
 */
@Component
public class JDProcessImpl implements IProcess {
	private static Logger logger = LoggerFactory.getLogger(JDProcessImpl.class);

	@Resource
	private XpathConfig xpathConfig;

	@Override
	public boolean process(Page page) {
		String content = page.getContent();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(content);
		
		String url = page.getUrl();
		// TODO 硬编码，后期需要修改
		// 解析商品详情
		if (url.startsWith("http://item.jd.com") || url.startsWith("https://item.jd.com")) {
			return parseItemPage(page, rootNode);
		// 解析商品详情之HK
		} else if (url.startsWith("http://item.jd.hk") || url.startsWith("https://item.jd.hk")) {
			return parseItemPage(page, rootNode);
		// 解析商品列表页面之search
		} else if (url.startsWith("http://search.jd.com") || url.startsWith("https://search.jd.com")) {
			return parseSearchPage(page, rootNode);
		// 解析酒类
		} else if (url.startsWith("https://jiu.jd.com/")) {
			return parseProductPage(page, rootNode);
		// 解析商品列表页面之list
		} else if (url.startsWith("https://list.jd.com") || url.startsWith("http://list.jd.com")) {
			return parseListPage(page, rootNode);
		}
		return false;
	}
	
	/**
	 * 解析商品列表页面（版本1）
	 * 
	 * @param page
	 * @param rootNode
	 */
	private boolean parseListPage(Page page, TagNode rootNode) {
		boolean flag = true;
		// 列表Xpath
		String ListXpath = "//*[@id=\"plist\"]/ul/li";
		// 下一页Xpath
		String nextXpath = "//*[@id=\"J_topPage\"]/a[2]";
		try {
			// 1、获取当前页的url
			Object[] ListEvaluateXPath = rootNode.evaluateXPath(ListXpath);
			if (!StringUtils.isEmptyByArray(ListEvaluateXPath)) {
				for (Object object : ListEvaluateXPath) {
					TagNode liNode = (TagNode) object;
					TagNode imgNode = liNode.getChildTags()[0].getChildTags()[0];
					TagNode aNode = imgNode.getChildTags()[0];
					String url = aNode.getAttributeByName("href");
					page.addUrl("http:" + url);
				}
			} else {
				logger.error("list页面解析失效,url:{},Xpath:{}", page.getUrl(),ListXpath);
				flag = false;
			}
			// 2、获取下一页
			Object[] nextEvaluateXPath = rootNode.evaluateXPath(nextXpath);
			if (!StringUtils.isEmptyByArray(nextEvaluateXPath)) {
				TagNode nextNode = (TagNode) nextEvaluateXPath[0];
				String url = nextNode.getAttributeByName("href");
				if(!url.equals("javascript:;")){
					page.addUrl("http://list.jd.com" + url);
				}
			} else {
				logger.error("next页面解析失效,url:{},Xpath:{}", page.getUrl(),nextXpath);
				flag = false;
			}
		} catch (XPatherException e) {
			logger.error("Xpath 解析异常终止,url:{},exception:{}", page.getUrl(), e);
			flag = false;
		}
		return flag;
	}

	/**
	 * 解析商品详情页面（com和hk）
	 * @param page
	 * @param rootNode
	 * @return
	 */
	private boolean parseItemPage(Page page, TagNode rootNode) {
		boolean flag = true;
		// TODO 无法使用注入方式获取（返回值为null）
		// String titleXpath = xpathConfig.getTitle();
		// 标题
		String titleXpath = "//*[@class=\"sku-name\"]";
		// 图片
		String pictureXpath1 = "//*[@id=\"spec-img\"]";
		String pictureXpath2 = "//*[@id=\"spec-n1\"]/img";
		// 价格正则表达式
		String priceRegex1 = "https://item.jd.com/([0-9]+).html";
		String priceRegex2 = "http://item.jd.com/([0-9]+).html";
		// 商品介绍
		String parameterXpath = "//*[@id=\"detail\"]/div[2]/div[1]/div[1]/ul";
		String parameterXpath_hk = "//*[@id=\"item-detail\"]/div[1]/ul";
		// 商品介绍之brand
		String brandXpath = "//*[@id=\"parameter-brand\"]";
		// 规格与包装
		String specificationsXpath = "//*[@id=\"detail\"]/div[2]/div[2]/div";
		String specificationsXpath_hk = "//*[@id=\"specifications\"]/table/tbody/tr";
		try {
			// 1、标题（title）
			String title = HtmlUtils.getText(rootNode, titleXpath);
			if(title!=null){
				page.addField("title", title);
			}else{
				logger.error("标题解析失效,url:{},Xpath:{}", page.getUrl());
				flag = false;
			}
			//TODO 2、图片（picture）
			// 获取src为空，这是因为src的值是一段脚本：var src =
			// img.getAttribute('data-origin');
			String picUrl1 = HtmlUtils.getAttributeByName(rootNode, pictureXpath1,"data-origin");
			String picUrl2 = HtmlUtils.getAttributeByName(rootNode, pictureXpath2,"src");
			if(picUrl1!=null){
				page.addField("picture", "http:" + picUrl1);
			}else if(picUrl2!=null){
				page.addField("picture", "http:" + picUrl2);
			}else{
				logger.error("图片解析失效,url:{},Xpath:{}", page.getUrl());
				flag = false;
			}
			// 3.1、价格（price）
			// 这个价格是ajax异步获取的
			//http://p.3.cn/prices/get?type=1&area=1_72_2799&pdtk=&pduid=1340247559&pdpin=&pdbp=0&skuid=J_1308551
			String goodsId = "";
			// 使用正则表达式
			String url = page.getUrl();
			Pattern pattern1 = Pattern.compile(priceRegex1);
			Pattern pattern2 = Pattern.compile(priceRegex2);
			Matcher matcher1 = pattern1.matcher(url);
			Matcher matcher2 = pattern2.matcher(url);
			if (matcher1.find()) {
				goodsId = matcher1.group(1);
			} else if(matcher2.find()) {
				goodsId = matcher2.group(1);
			} else {
				logger.error("url: {}没有匹配上价格的正则表达式: {},{}", url, priceRegex1,priceRegex2);
				flag = false;
			}
			
			if (!StringUtils.isEmpty(goodsId)) {
				// 获取返回的json数据
				//如果爬取过于频繁，容易返回{"error":"pdos_captcha"},所以生成Price的pduid
				String pdUid = "";
				String priceContent = "";
				int i = 0;
				//price价格获取失败后，重试3次
				do {
					if(i>0){
						//TOOD 或者这里换ip
						SleepUtils.sleep(1000);
						logger.error("price没有爬取成功,json返回数据不正常,{},开始重试第{}次",priceContent,i);
					}
					pdUid = PrepareUtils.getPdUid();
					priceContent = PageUtils.getContent("http://p.3.cn/prices/get?type=1&area=1_72_2799&pdtk=&pduid="+pdUid+"&pdpin=&pdbp=0&skuid=J_" + goodsId);
					i++;
				} while (priceContent!=null && priceContent.equals("{\"error\":\"pdos_captcha\"}") && i<4);
				
				if(priceContent!=null && priceContent.startsWith("[")){
					// 解析json数据
					JSONArray jsonArray = new JSONArray(priceContent);
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					page.addField("price", jsonObject.getString("p"));
				}else {
					logger.error("price没有爬取成功,json返回数据不正常,{}",priceContent);
					//由于价格是异步获取的，可能有的时候无法获取到，所以如果获取失败，先不要设定为false
//					flag = false;
				}
			} else {
				logger.error("price没有爬取成功,没有获取到goodsId！");
				flag = false;
			}
			// 3.2、处理goodsId（作为hbase的rowkey）
			page.setGoodsId(StringUtils.reverse(goodsId)+"_jd");
			// 4、商品介绍（goodsInfo）
			JSONArray goodsInfoJsonArray = new JSONArray();
			Object[] goodsInfoEvaluateXPath = null;
			if(page.getContent().contains("京东全球购")){
				goodsInfoEvaluateXPath = rootNode.evaluateXPath(parameterXpath_hk);
				logger.info("商品详情---京东全球购 ，url:{}",page.getUrl());
			}else{
				goodsInfoEvaluateXPath = rootNode.evaluateXPath(parameterXpath);
			}
			if (!StringUtils.isEmptyByArray(goodsInfoEvaluateXPath)) {
				//处理brand
				getBrand(goodsInfoJsonArray,rootNode,brandXpath);
				//处理parame1
				getParame1(goodsInfoJsonArray,goodsInfoEvaluateXPath);
				//处理parame2
				getParame2(goodsInfoJsonArray, goodsInfoEvaluateXPath);
			} else {
				logger.error("parameter解析失效,url:{},Xpath:{}", page.getUrl());
				flag = false;
			}
			page.addField("goodsInfo", goodsInfoJsonArray.toString());
			// 5、规格与包装（specifications）
			JSONArray specificationsJsonArray = new JSONArray();
			Object[] divEvaluateXPath = null;
			Object[] divEvaluateXPath_hk = null;
			//com和hk区别开
			if(page.getContent().contains("京东全球购")){
				divEvaluateXPath_hk = rootNode.evaluateXPath(specificationsXpath_hk);
				logger.info("参数规格---京东全球购 ，url:{}",page.getUrl());
			}else{
				divEvaluateXPath = rootNode.evaluateXPath(specificationsXpath);
			}
			//处理com
			if (!StringUtils.isEmptyByArray(divEvaluateXPath)) {
				// 5.1 Ptable
				getPtable(specificationsJsonArray, divEvaluateXPath);
				// 5.2 packge-list
				getPackage(specificationsJsonArray, divEvaluateXPath);
				page.addField("specifications", specificationsJsonArray.toString());
			//处理hk
			}else if(!StringUtils.isEmptyByArray(divEvaluateXPath_hk)){
				getSpecifications(specificationsJsonArray,divEvaluateXPath_hk);
				page.addField("specifications", specificationsJsonArray.toString());
			}
		} catch (XPatherException e) {
			logger.error("Xpath 解析异常终止,url:{},exception:{}", page.getUrl(), e);
			flag = false;
		}
		return flag;
	}
	
	//--------------------------------------------------------------------
	/**
	 * 获取hk的参数规格详情
	 * @param specificationsJsonArray
	 * @param divEvaluateXPath_hk
	 */
	private void getSpecifications(JSONArray specificationsJsonArray, Object[] divEvaluateXPath_hk) {
		for (Object object : divEvaluateXPath_hk) {
			TagNode trNode = (TagNode) object;
			TagNode[] childTags = trNode.getChildTags();
			JSONObject jsonObject2 = new JSONObject();
			//th之后会有一行空段
			if(childTags.length==0)
				continue;
			if(childTags[0].getName().equals("th")){
				String name = childTags[0].getText().toString().trim();
				jsonObject2.put("name", name);
				jsonObject2.put("value", "");
			}else if(childTags[0].getName().equals("td")){
				String name = childTags[0].getText().toString().trim();
				String value = childTags[1].getText().toString().trim();
				jsonObject2.put("name", name);
				jsonObject2.put("value", value);
			}
			specificationsJsonArray.put(jsonObject2);
		}
	}

	/**
	 * 获取规格与包装的package-list标签内容
	 * @param specificationsJsonArray
	 * @param divEvaluateXPath
	 */
	private void getPackage(JSONArray specificationsJsonArray, Object[] divEvaluateXPath) {
		TagNode listNode = (TagNode) divEvaluateXPath[1];
		int len = listNode.getChildTags().length;
		TagNode lhNode = null;
		TagNode lpNode = null;
		JSONObject jsonObject2 = new JSONObject();
		if(len>0){
			lhNode = listNode.getChildTags()[0];
			jsonObject2.put("name", lhNode.getText().toString().trim());
		}
		if(len>1){
			lpNode = listNode.getChildTags()[1];
			jsonObject2.put("value", lpNode.getText().toString().trim());
		}else{
			jsonObject2.put("value", "");
		}
		specificationsJsonArray.put(jsonObject2);
	}
	
	/**
	 * 获取规格与包装的Ptable标签内容
	 * @param specificationsJsonArray
	 * @param divEvaluateXPath
	 */
	private void getPtable(JSONArray specificationsJsonArray, Object[] divEvaluateXPath) {
		TagNode pNode = (TagNode) divEvaluateXPath[0];
		TagNode[] childTags = pNode.getChildTags();
		for (TagNode tagNode : childTags) {
			TagNode hNode = tagNode.getChildTags()[0];
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("name", hNode.getText().toString().trim());
			jsonObject2.put("value", "");
			specificationsJsonArray.put(jsonObject2);
			// TODO 将jsonObject2对象设定为空
			jsonObject2 = new JSONObject();
			TagNode[] dlTagNodes = tagNode.getChildTags()[1].getChildTags();
			for (TagNode tagNode3 : dlTagNodes) {
				if (tagNode3.getName().equals("dt")) {
					jsonObject2.put("name", tagNode3.getText().toString().trim());
				} else {
					jsonObject2.put("value", tagNode3.getText().toString().trim());
					specificationsJsonArray.put(jsonObject2);
					// TODO 将jsonObject2对象设定为空
					jsonObject2 = new JSONObject();
				}
			}
		}
	}
	
	/**
	 * 获取商品介绍中的brand
	 * @param jsonArray
	 * @param rootNode
	 * @param brandXpath
	 * @throws XPatherException
	 */
	private void getBrand(JSONArray jsonArray,TagNode rootNode,String brandXpath) throws XPatherException {
		Object[] evaluateXPath = rootNode.evaluateXPath(brandXpath);
		if(evaluateXPath.length > 0){
			TagNode brandNode = (TagNode) evaluateXPath[0];
			String[] split = brandNode.getChildTags()[0].getText().toString().split(" ");
			int len = split.length;
			JSONObject  jsonObject2 = new JSONObject();
			if(len>0){
				jsonObject2.put("name", split[0].trim());
			}
			if(len>1){
				jsonObject2.put("value", split[1].trim());
			}else{
				jsonObject2.put("value", "");
			}
			jsonArray.put(jsonObject2);
		}
	}
	
	/**
	 * 获取商品介绍中的parame1
	 * @param goodsInfoJsonArray
	 * @param goodsInfoEvaluateXPath
	 * @param i
	 */
	private void getParame1(JSONArray goodsInfoJsonArray,Object[] goodsInfoEvaluateXPath) {
		for (Object object : goodsInfoEvaluateXPath) {
			TagNode tNode = (TagNode) object;
			String attr = tNode.getAttributeByName("class");
			if(attr.equals("parameter1 p-parameter-list") || attr.equals("parameter1")){
				TagNode[] childTags = tNode.getChildTags();
				JSONObject jsonObject2 = new JSONObject();
				for (TagNode tagNode : childTags) {
					TagNode[] pTags = tagNode.getChildTags()[1].getChildTags();
					for (TagNode p : pTags) {
						jsonObject2 = new JSONObject();
						// 普通：分辨率：1920*1080(FHD)
						String values = p.getText().toString().trim();
						String[] split = values.split("：");
						int len = split.length;
						// 可能含有：核&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数
						if (len>0 && split[0].trim().contains("&nbsp;")) {
							String value = "";
							String[] names = split[0].split("&nbsp;");
							for (String name : names) {
								if (!name.equals(",")) {
									value += name;
								}
							}
							jsonObject2.put("name", value);
						} else if(len >0){
							jsonObject2.put("name", split[0]);
						}
						if(len>1){
							jsonObject2.put("value", split[1]);
						}else{
							jsonObject2.put("value", "");
						}
						goodsInfoJsonArray.put(jsonObject2);
					}
				}
				return;
			}
		}
	}

	/**
	 * 获取商品介绍中的parame2
	 * @param goodsInfoJsonArray
	 * @param goodsInfoEvaluateXPath
	 * @param i
	 */
	private void getParame2(JSONArray goodsInfoJsonArray, Object[] goodsInfoEvaluateXPath) {
		for (Object object : goodsInfoEvaluateXPath) {
			TagNode tNode = (TagNode) object;
			String attr = tNode.getAttributeByName("class");
			if(attr.equals("parameter2 p-parameter-list") || attr.equals("parameter2")){
				TagNode[] liTages = tNode.getChildTags();
				for (TagNode liNode : liTages) {
					String values = liNode.getText().toString().trim();
					String[] splits = values.split("：");
					JSONObject jsonObject2 = new JSONObject();
					int len = splits.length;
					if(len>0){
						jsonObject2.put("name", splits[0].trim());
					}
					if(len>1){
						jsonObject2.put("value", splits[1].trim());
					}else{
						jsonObject2.put("value", "");
					}
					goodsInfoJsonArray.put(jsonObject2);
				}
				return;
			}
		}
	}
	/**
	 * 解析商品列表页面（版本2）
	 * （略）
	 * @param page
	 * @param rootNode
	 */
	private boolean parseSearchPage(Page page, TagNode rootNode) {
		return true;
	}

	/**
	 * 解析商品种类页面
	 * 
	 * @param page
	 * @param rootNode
	 */
	private boolean  parseProductPage(Page page, TagNode rootNode) {
		return true;
	}
}
