package com.dengpan.pan.factory.data.mail;

import android.util.Log;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 *
 * 邮箱地址为:https://www.guerrillamail.com/zh/inbox
 *
 */

public class Guerrillamail implements MailContract {

    @Override
    public void generateMail(String name, String domain, DataSource.Callback callback) {
        Map<String,String> params = new HashMap<>();
        params.put("email_user",name);
        params.put("lang","zh");
        params.put("site","guerrillamail.com");
        params.put("in","设置 取消");
        OkGo.post(Common.RIGISTER_URL_IN_GUER)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                    }
                });
    }

    @Override
    public void receiveMailCode(String account, DataSource.Callback callback) {

    }

    @Override
    public void getMailCode(String url, DataSource.Callback callback) {

    }
}
