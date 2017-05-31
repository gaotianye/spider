package com.gaotianye.springboot.spider.download;

import org.springframework.stereotype.Component;

/**
 * 其他下载方式
 */
import com.gaotianye.springboot.spider.page.Page;

/**
 * 其他下载方式（非HttpClient）
 * @author gaotianye
 */
@Component
public class OtherDownloadImpl implements IDownload {

	@Override
	public Page download(String url) {
		return null;
	}

}
