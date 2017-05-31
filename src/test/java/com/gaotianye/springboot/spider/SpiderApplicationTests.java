package com.gaotianye.springboot.spider;

import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gaotianye.springboot.spider.download.IDownload;
import com.gaotianye.springboot.spider.factory.DownloadFactory;
import com.gaotianye.springboot.spider.factory.ProcessFactory;
import com.gaotianye.springboot.spider.factory.RepositoryFactory;
import com.gaotianye.springboot.spider.factory.StoreFactory;
import com.gaotianye.springboot.spider.page.Page;
import com.gaotianye.springboot.spider.process.IProcess;
import com.gaotianye.springboot.spider.repository.IRepository;
import com.gaotianye.springboot.spider.store.IStore;
import com.gaotianye.springboot.spider.utils.MD5Utils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {
	@Resource
	private DownloadFactory downloadFactory;
	@Resource
	private ProcessFactory processFactory;
	@Resource
	private StoreFactory storeFactory;
	@Resource
	private RepositoryFactory repositoryFactory;
	
/*	@Test
	public void XpathTest() {
		IDownload download = downloadFactory.getDownLoad();
		IProcess process = processFactory.getProcess();
		IStore store = storeFactory.getStore();
		
		//TODO 1、从配置文件中读取各大电商网站要爬起的初始url，然后存到redis中
		String url = jdConfig.getProducter();
		Page page = download.download(url);
		process.process(page);
		store.store(page);
	}*/
	
	/*@Test
	public void test1(){
		String str = "核&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数";
		String[] split = str.split("&nbsp;");
		String name = "";
		for (String str1 : split) {
			if(!str1.equals("&nbsp;")){
				name+=str1;
			}
		}
		System.out.println("--"+name+"--");
	}*/
	
/*	@Test
	public void test2(){
		String s = "http://item.jd.com/4390096.html, http://item.jd.com/3652063.html, http://item.jd.com/3726830.html, http://item.jd.com/4510588.html, http://item.jd.com/3553539.html, http://item.jd.com/3133498.html, http://item.jd.com/4461470.html, http://item.jd.com/2967927.html, http://item.jd.com/3995645.html, http://item.jd.com/3458051.html, http://item.jd.com/3995643.html, http://item.jd.com/3742098.html, http://item.jd.com/2600210.html, http://item.jd.com/4371862.html, http://item.jd.com/4159500.html, http://item.jd.com/4884236.html, http://item.jd.com/2888228.html, http://item.jd.com/4609868.html, http://item.jd.com/4207732.html, http://item.jd.com/2600240.html, http://item.jd.com/3322629.html, http://item.jd.com/4586850.html, http://item.jd.com/3367822.html, http://item.jd.com/3984704.html, http://item.jd.com/2321852.html, http://item.jd.com/4864120.html, http://item.jd.com/4483094.html, http://item.jd.com/4094700.html, http://item.jd.com/1217500.html, http://item.jd.com/1057746.html, http://item.jd.com/4483118.html, http://item.jd.com/2402694.html, http://item.jd.com/4453528.html, http://item.jd.com/4220828.html, http://item.jd.com/2600248.html, http://item.jd.com/3893501.html, http://item.jd.com/11476089321.html, http://item.jd.com/3234250.html, http://item.jd.com/3355175.html, http://item.jd.com/3749095.html, http://item.jd.com/3899582.html, http://item.jd.com/3801809.html, http://item.jd.com/3888216.html, http://item.jd.com/3984684.html, http://item.jd.com/2569127.html, http://item.jd.com/4499496.html, http://item.jd.com/1856654.html, http://item.jd.com/3821796.html, http://item.jd.com/10532651102.html, http://item.jd.com/3892669.html, http://item.jd.com/10598515910.html, http://item.jd.com/10834639654.html, http://item.jd.com/10471380245.html, http://item.jd.com/4213312.html, http://item.jd.com/4879844.html, http://item.jd.com/10829282573.html, http://item.jd.com/11556525364.html, http://item.jd.com/3789624.html, http://item.jd.com/10932167751.html, http://item.jd.com/10829330789.html, http://list.jd.com/list.html?cat=9987,653,655&page=2&sort=sort_rank_asc&trans=1&JL=6_0_0";
		String[] split = s.split(",");
		for (String string : split) {
			System.out.println("-"+string+"-");
		}
		System.out.println(split.length+"=========");
	}*/
	
	/*@Test
	public void hbaseTest() throws Exception{
		String str = "1234abcd";
		String reverse = StringUtils.reverse(str);
		System.out.println(reverse);
	}*/
	
	/*@Test
	public void xpathTest() throws Exception{
		IDownload download = downloadFactory.getDownLoad();
		IProcess process = processFactory.getProcess();
		IStore store = storeFactory.getStore();
		IRepository repository = repositoryFactory.getRepository();
		
		repository.add("http://item.jd.com/4417116.html");
		
		String url = repository.poll();
		//下载页面，获取Page对象
		Page page = download.download(url);
		//解析Page对象
		String content = page.getContent();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(content);	
		
		String parameterXpath = "//*[@id=\"detail\"]/div[2]/div[1]/div[1]/ul";
		
		Object[] evaluateXPath = rootNode.evaluateXPath(parameterXpath);
	}*/
	
	@Test
	public void test1(){
		String time = new Date().getTime()+"_";
		Random random = new Random();
		for(int i = 0;i<5;i++){
			time += random.nextInt(10);
		}
		String md5 = MD5Utils.getMD5(time);
		System.out.println(md5);
	}
}
