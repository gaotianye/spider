package com.gaotianye.springboot.spider.starter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gaotianye.springboot.spider.constant.Constant;
import com.gaotianye.springboot.spider.download.IDownload;
import com.gaotianye.springboot.spider.factory.DownloadFactory;
import com.gaotianye.springboot.spider.factory.ProcessFactory;
import com.gaotianye.springboot.spider.factory.RepositoryFactory;
import com.gaotianye.springboot.spider.factory.StoreFactory;
import com.gaotianye.springboot.spider.page.Page;
import com.gaotianye.springboot.spider.process.IProcess;
import com.gaotianye.springboot.spider.repository.IRepository;
import com.gaotianye.springboot.spider.store.IStore;
import com.gaotianye.springboot.spider.utils.SleepUtils;
import com.gaotianye.springboot.spider.utils.StringUtils;

/**
 * TODO 多线程 速度特别的慢
 * @author gaotianye
 *
 */
@RestController
@RequestMapping("spider")
public class StartController {
	private static Logger logger = LoggerFactory.getLogger(StartController.class);
	
	/**
	 * 配置文件信息
	 */
	@Resource
	private DownloadFactory downloadFactory;
	@Resource
	private ProcessFactory processFactory;
	@Resource
	private StoreFactory storeFactory;
	@Resource
	private RepositoryFactory repositoryFactory;
	
	@RequestMapping(value = "starter", method = RequestMethod.GET)
	public void starter() {
		logger.info("####################spider start####################");
		/**
		 * TODO 问题：
		 * 使用注解方式，获取download和process对象，但是报错：
		 * Field download in com.gaotianye.springboot.spider.scheduler.Spider required a single bean, but 2 were found:
		 * 	- httpClientDownloadImpl: defined in file [E:\CelLoud\spider\target\classes\com\gaotianye\springboot\spider\download\HttpClientDownloadImpl.class]
			- otherDownloadImpl: defined in file [E:\CelLoud\spider\target\classes\com\gaotianye\springboot\spider\download\OtherDownloadImpl.class]
		 * 代码：
		 *	download = downloadFactory.getDownLoad();
			process = processFactory.getFactory();
			
			但是下面这种写法就没有问题
		 */
		final IDownload download = downloadFactory.getDownLoad();
		final IProcess process = processFactory.getProcess();
		final IStore store = storeFactory.getStore();
		final IRepository repository = repositoryFactory.getRepository();
		
		//线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(Constant.NTHREAD);
		
		if(download!=null && process!=null && store!=null && repository!=null){
			//初始化操作，如果set中存在信息，则不会存储成功
			repository.addHigher("https://list.jd.com/list.html?cat=9987,653,655&page=129&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=6#J_main");
//			repository.addHigher("https://list.jd.com/list.html?cat=9987,653,655&page=2&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=6#J_main");
//			repository.addHigher("https://list.jd.com/list.html?cat=9987,653,655");
//			repository.add("https://item.jd.com/1971822255.html");
//			repository.add("http://item.jd.com/1137831.html");
			while(true){
				//多线程执行，有可能会item和list同时执行（某些线程发现list空，但是item有值）
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						//获取url
						String url = repository.poll();
						if(StringUtils.isEmpty(url)){
							logger.info("队列中暂时没有需要抓取的url，休息10s");
							SleepUtils.sleep(10000);
						}else{
							//下载页面，获取Page对象
							Page page = download.download(url);
							//如果页面下载失败，则continue
							if(page.getContent()==null){
								SleepUtils.sleep();
							}else{
								//如果页面下载成功，则解析Page对象,返回boolean类型。
								//如果解析失败，则continue
								if(!process.process(page)){
									SleepUtils.sleep();
								}else{
									//获取临时urls
									List<String> urls = page.getUrls();
									//判断是商品列表页面，还是是商品详情页面（TODO 也有可能是其他页面）
									//商品列表页面urls不为空
									if(urls.isEmpty()){
										logger.info("当前解析的页面是商品详情页面，需要存储Page对象。url={}",url);
										//存储解析得到Page对象
										store.store(page);
										//如果是商品列表页面，则将临时url存储到队列中
									}else{
										logger.info("当前解析的页面是商品列表页面，需要将临时的url存储到队列中。url={}",url);
										repository.addAll(urls);
									}
									SleepUtils.sleep();
								}
							}
						}
					}
				});
			}
		}else{
			logger.error("初始化信息不完善！");
			return ;
		}
	}
}
