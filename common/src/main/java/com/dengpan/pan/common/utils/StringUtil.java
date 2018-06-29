package com.dengpan.pan.common.utils;

import java.util.UUID;

/**
 * 字符串的工具类 生成随机数
 */

public class StringUtil {
    /**
     * 获取随机的N位数 用于邮件名称
     * @param n
     * @return
     */
    public static String generateRandomNumber(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        long num =(long) (Math.random() * 9 * Math.pow(10, n - 1)) + (long) Math.pow(10, n - 1);
        return num+"";
    }

    /**
     * 获取一个随机的字符串 用于 注册名
     *
     * @return
     */
    public static String getRandomStr() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 10);
    }
}
