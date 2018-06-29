package com.dengpan.pan.factory.presenter.register;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.common.factory.presenter.BasePresenter;
import com.dengpan.pan.common.utils.StringUtil;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.data.MailHelper;
import com.dengpan.pan.factory.data.SpeedSSHelper;
import com.dengpan.pan.factory.model.ApplyMail;
import com.dengpan.pan.factory.model.MailResult;
import com.dengpan.pan.factory.model.Result;


public class RegistPresenter extends BasePresenter<RegistContract.View> implements RegistContract.Presenter {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            receiveMail((String) msg.obj);
        }
    };
    private String account;
    private String password;
    public RegistPresenter(RegistContract.View view) {
        super(view);
    }

    /**
     * 申请邮箱
     * @param accout
     * @param password
     */
    @Override
    public void applyMail(String accout, String password) {
        this.account = null;
        this.password = null;
        if(getView() != null){
            getView().progress("正在生成邮箱.....");
        }
        if(TextUtils.isEmpty(accout.split("@")[0])){
            accout= StringUtil.generateRandomNumber(9)+accout;
        }

        if(TextUtils.isEmpty(password)){
            //默认8个1
            password = "11111111";
        }
        this.account = accout;
        this.password = password;
        MailHelper.applyMail(accout, new DataSource.Callback<ApplyMail>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                if(getView()!= null){
                    getView().showError(strRes);
                }
            }

            @Override
            public void onDataLoad(ApplyMail applyMail) {
                senMailCode(applyMail.getUser());
            }
        });
    }

    /**
     * 发送验证码
     * @param account
     */
    @Override
    public void senMailCode(final String account) {
        if(getView() != null){
            getView().progress("正在发送邮箱验证码....");
        }
        SpeedSSHelper.sendMailCode(account,new DataSource.Callback<Result>(){

            @Override
            public void onDataLoad(Result result) {
                //开始接收邮件
                receiveMail(account);
            }

            @Override
            public void onDataNotAvailable(int strRes) {
                if(getView()!= null){
                    getView().showError(strRes);
                }
            }
        });
    }

    /**
     * 接收验证码
     * @param account
     */

    @Override
    public void receiveMail(final String account) {
        if(getView() != null){
            getView().progress("正在接收邮箱验证码.....");
        }

       MailHelper.receiveMailCode(account,(System.currentTimeMillis()/1000),new DataSource.Callback<MailResult>(){

                    @Override
                    public void onDataLoad(MailResult result) {
                        if(result.isSuccess()){
                            //获取网址
                            if(result.getMail().size()>0){
//                                String[] split = account.split("@");
                                String s = account.replace("@", "(a)").replaceFirst("\\.", "-_-");
                                String format = String.format(Common.baseMailUrl, s, result.getMail().get(0).get(4));
                                Log.e("网页的地址为：",format);
//                                    parseCode(format);
                                parseMail(format);
                                //解析网页获得code
                                return;
                            } else {
                                Message message= Message.obtain();
                                message.obj = account;
                                handler.sendMessageDelayed(message,2000);
                            }

                        }
                    }

                    @Override
                    public void onDataNotAvailable(int strRes) {
                        if(getView()!= null){
                            getView().showError(strRes);
                        }
                    }
                });

    }

    /**
     * 解析邮件的验证码
     * @param url
     */

    @Override
    public void parseMail(String url) {
        if(getView() != null){
            getView().progress("正在接收邮箱验证码2.....");
        }
        MailHelper.visitMail(url,new DataSource.Callback<String>(){

            @Override
            public void onDataLoad(String s) {
                if(TextUtils.isEmpty(s)) {
                    getView().showError(R.string.ERROR_SEND_MAIL_CODE);
                    return;
                }
                register(account,password,s);
            }

            @Override
            public void onDataNotAvailable(int strRes) {
                if(getView()!= null){
                    getView().showError(strRes);
                }
            }
        });
    }

    @Override
    public void register(final String account, final String password, String code) {
        if(getView() != null){
            getView().progress("正在注册.....");
        }
        SpeedSSHelper.register(account,password,code,new DataSource.Callback<Result>(){

            @Override
            public void onDataLoad(Result result) {
                if(getView()!= null){
                    getView().showResult(result,account,password);
                }
            }

            @Override
            public void onDataNotAvailable(int strRes) {
                if(getView()!= null){
                    getView().showError(strRes);
                }
            }
        });
    }

    @Override
    public void downloadClientApk(String path) {

    }
}
