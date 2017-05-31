package com.gaotianye.springboot.spider.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**  
     * 根据样式格式化时间  
     * "yyyyMMdd"  
     * "yyyyMMddHHmmss"  
     * "yyyyMMddHHmmssssssss"  
     * @param dateFormat  
     * @return  
     */  
    public static String getnowDate(String dateFormat){  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);   
        return sdf.format(new Date());  
    }  
}
