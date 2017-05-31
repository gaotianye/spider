package com.gaotianye.springboot.spider.download;

import org.springframework.stereotype.Component;

import com.gaotianye.springboot.spider.page.Page;
import com.gaotianye.springboot.spider.utils.PageUtils;

/**
 * 通过HttpClient4 方式下载content内容，
 * 并将其set到Page对象中，返回给调用者
 * @author gaotianye
 */
@Component
public class HttpClientDownloadImpl implements IDownload {
	@Override
	public Page download(String url) {
		Page page = new Page();
		//通过HttpClient方式获取指定url的content信息
		String content = PageUtils.getContent(url);
		page.setContent(content);
		page.setUrl(url);
		return page;
	}

}
