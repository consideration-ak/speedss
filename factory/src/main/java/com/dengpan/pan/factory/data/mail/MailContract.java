package com.dengpan.pan.factory.data.mail;

import com.dengpan.pan.common.factory.data.DataSource;

public interface MailContract {

    /**
     * 生成邮箱
     * @param name 定制得个性名称
     * @param domain 域名
     */
    void generateMail(String name, String domain, DataSource.Callback callback);

    /**
     * 接受邮箱
     * @param account 邮箱地址
     */
    void receiveMailCode(String account, DataSource.Callback callback);

    /**
     * 获取邮箱得验证码
     * @param url
     */
    void getMailCode(String url, DataSource.Callback callback);

}
