package com.dengpan.pan.factory.data.mail;

import android.text.TextUtils;
import android.util.Log;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.common.utils.LogUtil;
import com.dengpan.pan.factory.Factory;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.model.ApplyMail;
import com.dengpan.pan.factory.model.GuerApplyMailBean;
import com.dengpan.pan.factory.model.GuerGetMailCodeBean;
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
    public void generateMail(String name, String domain, final DataSource.Callback callback) {
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
                        if(!TextUtils.isEmpty(s)){
                            GuerApplyMailBean applyMail = Factory.getGson().fromJson(s, GuerApplyMailBean.class);
                            LogUtil.logI("生成Email:",applyMail.getEmail_addr());
                                callback.onDataLoad(applyMail.getEmail_addr());
                                //1 点击发送邮箱
//                                getEmailCode(account);
                                //2 等待邮箱接收
//                                showToast(applyMail.getUser());
                                return;
                        }
                        callback.onDataNotAvailable(R.string.ERROR_APPLY_MAIL);
                    }
                });
    }

    @Override
    public void receiveMailCode(final String account, final DataSource.Callback callback) {
        //f=check_email&seq=1&site=guerrillamail.com&in=wangzhe&_=1547523859933
        String[] accounts = account.split("@");
        Map<String,String> params = new HashMap<>();
        params.put("f","check_email");
        params.put("seq","1");
        params.put("site","guerrillamail.com");//这里是可以变动得
        params.put("in","设置 取消");
        params.put("_",System.currentTimeMillis()+"");
        OkGo.get(Common.GET_MAIL_CODE)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(!TextUtils.isEmpty(s)){
                            GuerGetMailCodeBean getCode = Factory.getGson().fromJson(s, GuerGetMailCodeBean.class);
                            LogUtil.logI("获取Email:",getCode.toString());
                            if(getCode == null || getCode.getList() ==null || getCode.getList().size() == 0){
                                callback.onDataLoad("");
                            }else {
                                getMailCode(getCode.getList().get(0).getMail_excerpt(),callback);
                            }
                            //1 点击发送邮箱
//                                getEmailCode(account);
                            //2 等待邮箱接收
//                                showToast(applyMail.getUser());
                            return;
                        }else {
                            callback.onDataLoad("");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        callback.onDataNotAvailable(R.string.ERROR_PARSE_MAIL_CODE_NET);
                    }
                });
    }

    /**
     * 负责解析 code 并且返回
     * @param content 邮件得内容   尊敬的用户：
    您的邮箱验证代码为: X6qnJF5r，请在网页中填写，完成验证。(本验证代码有效期 30 分钟)Speedss
     * @param callback
     */
    @Override
    public void getMailCode(String content, DataSource.Callback callback) {
        LogUtil.logI("获取EmailCode:","Content："+content);
        if(TextUtils.isEmpty(content)) callback.onDataNotAvailable(R.string.ERROR_PARSE_MAIL_CODE_NET);
        try {
            String code = content.split("，")[0].split(": ")[1];
            callback.onDataLoad(code);
        }catch (Exception e){
            callback.onDataNotAvailable(R.string.ERROR_PARSE_MAIL_CODE_NET);
        }
    }
}
