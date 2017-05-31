package com.gaotianye.springboot.spider.process;

import com.gaotianye.springboot.spider.page.Page;

/**
 * 解析接口
 * @author gaotianye
 */
public interface IProcess {
	boolean process(Page page);
}
