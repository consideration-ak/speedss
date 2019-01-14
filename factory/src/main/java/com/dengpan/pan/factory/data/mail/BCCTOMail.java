package com.dengpan.pan.factory.data.mail;

import android.text.TextUtils;
import android.util.Log;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.factory.Factory;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.model.ApplyMail;
import com.dengpan.pan.factory.model.MailResult;
import com.dengpan.pan.factory.net.NetWork;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.Call;
import okhttp3.Response;

public class BCCTOMail implements MailContract {
    @Override
    public void generateMail(String name, String domain, final DataSource.Callback callback) {
        OkGo.post(Common.APPLY_MAIL)
                .headers(NetWork.getMailHeaders())
                .params("mail",name+domain)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        if(!TextUtils.isEmpty(s)){
                            ApplyMail applyMail = Factory.getGson().fromJson(s, ApplyMail.class);
                            if(applyMail.isSuccess()){
                                callback.onDataLoad(applyMail);
                                //1 点击发送邮箱
//                                getEmailCode(account);
                                //2 等待邮箱接收
//                                showToast(applyMail.getUser());
                                return;
                            }
                        }
                        callback.onDataNotAvailable(R.string.ERROR_APPLY_MAIL);

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
//                        showToast("激活邮件失败（网络）");
                        callback.onDataNotAvailable(R.string.ERROR_APPLY_MAIL_NET);
                    }
                });
    }

    @Override
    public void receiveMailCode(String account, final DataSource.Callback callback) {
        OkGo.getInstance().cancelTag("getMailCode");
        OkGo.post(Common.GETMAIL_URL)
                .headers(NetWork.getMailCoderHeaders())
                .tag("getMailCode")
                .params("mail",account)
                .params("time",(System.currentTimeMillis()/1000))
                .params("_",System.currentTimeMillis()-60*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        if(!TextUtils.isEmpty(s)){
                            MailResult result = Factory.getGson().fromJson(s, MailResult.class);
                            callback.onDataLoad(result);
                        }else {
                            callback.onDataNotAvailable(R.string.ERROR_RECEIVE_MAIN_URL_NET);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        callback.onDataNotAvailable(R.string.ERROR_RECEIVE_MAIN_URL_NET);
                    }
                });
    }

    @Override
    public void getMailCode(String url, final DataSource.Callback callback) {
        OkGo.get(url)
                .headers(NetWork.getMailCoderHeaders())
//                .params("If-None-Match","\"dd3e83b417fbfe861146392f1734996f80755598\"")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        Log.e("内容为:",s);
                        Document parse = Jsoup.parse(s);
                        String text = parse.select("body > p:nth-child(8) > b").text();
                        Log.e("内容为:",text);
                        //开始注册
                        callback.onDataLoad(text);

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        callback.onDataNotAvailable(R.string.ERROR_PARSE_MAIL_CODE_NET);
                    }
                });
    }
}
