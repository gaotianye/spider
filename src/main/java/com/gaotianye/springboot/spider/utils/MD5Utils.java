/**    
 * @Title: MD5Util.java  
 * @Package com.nova.utils   
 * @author summer    
 * @date 2012-6-26 上午09:36:51  
 * @version V1.0    
 */
package com.gaotianye.springboot.spider.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5Util加密算法
 * @author gaotianye
 *
 */
public class MD5Utils {
    private static Logger logger=LoggerFactory.getLogger(MD5Utils.class);
    /**
     * @Title: getMD5
     * @Description: (获取MD5加密后字符串)
     * @param password
     * @return
     * @param String
     * @throws
     */
    public static String getMD5(String password) {
        byte[] source = password.getBytes();
        String s = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return s;
    }

    /**
     * 获取文件的MD5值
     * 
     * @param path
     * @return
     */
    public static String getFileMD5(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return getFileMD5(file);
    }

    /**
     * 获取文件的MD5值
     * 
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        String md5 = null;
        try {
            fis = new FileInputStream(file);
            md5 = DigestUtils.md5Hex(fis);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return md5;
    }

}
