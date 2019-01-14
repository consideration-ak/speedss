package com.dengpan.pan.factory.data;

import android.text.TextUtils;
import android.util.Log;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.factory.Factory;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.data.mail.Guerrillamail;
import com.dengpan.pan.factory.data.mail.MailContract;
import com.dengpan.pan.factory.model.ApplyMail;
import com.dengpan.pan.factory.model.MailResult;
import com.dengpan.pan.factory.net.NetWork;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 邮件相关的helper
 */
public class MailHelper {
    private MailContract contract;
    private static MailHelper helper = new MailHelper();

    public static MailHelper getInstance(){
        if(helper == null){
            synchronized (MailHelper.class){
                if(helper == null){
                    return new MailHelper();
                }
            }
        }
        return helper;
    }


    private MailHelper() {
        this.contract = new Guerrillamail();
    }

    /**
     * 申请邮件（激活邮件）
     * @param account
     * @param callback
     */
    public void applyMail(String account,final DataSource.Callback<ApplyMail> callback) {
        contract.generateMail(account, "",callback);

    }

    /**
     * 接收邮件地址 获取收到邮件的地址
     * @param account
     * @param time
     * @param callback
     */
    public  void receiveMailCode(String account, long time, final DataSource.Callback<MailResult> callback) {
        contract.receiveMailCode(account,callback);

    }

    /**
     * 访问邮件的地址
     * @param url
     * @param callback
     */
    public  void visitMail(String url, final DataSource.Callback<String> callback) {
        contract.getMailCode(url,callback);
    }
}
