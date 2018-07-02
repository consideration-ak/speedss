package com.dengpan.pan.factory.presenter.register;

import android.net.Uri;

import com.dengpan.pan.common.factory.presenter.BaseContract;
import com.dengpan.pan.factory.model.Result;

public interface RegistContract {
    interface View extends BaseContract.View<Presenter>{
        /**
         * 展示注册的账号的进度
         * @param str
         */
        void progress(String str);

        /**
         * 注册成功后
         */
        void showResult(Result result,String account,String password);

        void downloadApkOK(Uri uri);
    }
    interface Presenter extends BaseContract.Presenter{
        //1 生成一个邮箱
        void applyMail(String accout, String password);
        //2 发送一个邮箱验证码
        void senMailCode(String accout);
        //3 接收邮件 轮询方式接收
        void receiveMail(String account);
        //4 解析邮件
        void parseMail(String url);
        //5 开始注册
        void register(String account,String password,String code);


        void downloadClientApk(String path);

    }

}
