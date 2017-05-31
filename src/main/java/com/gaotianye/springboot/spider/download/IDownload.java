package com.gaotianye.springboot.spider.download;

import com.gaotianye.springboot.spider.page.Page;

/**
 * 下载接口
 * @author gaotianye
 */
public interface IDownload {
	Page download(String url);
}
