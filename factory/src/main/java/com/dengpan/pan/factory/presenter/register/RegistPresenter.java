package com.dengpan.pan.factory.presenter.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.common.factory.presenter.BasePresenter;
import com.dengpan.pan.common.utils.LogUtil;
import com.dengpan.pan.common.utils.StringUtil;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.data.DbHelper;
import com.dengpan.pan.factory.data.MailHelper;
import com.dengpan.pan.factory.data.SpeedSSHelper;
import com.dengpan.pan.factory.model.Result;
import com.dengpan.pan.factory.net.DownloadHelper;


public class RegistPresenter extends BasePresenter<RegistContract.View> implements RegistContract.Presenter {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            receiveMail((String) msg.obj);
        }
    };
    private String account;
    private String password;

    public RegistPresenter(RegistContract.View view) {
        super(view);
//        MailHelper.getInstance().setContract(new BCCTOMail());
    }

    /**
     * 申请邮箱
     *
     * @param accout
     * @param password
     */
    @Override
    public void applyMail(String accout, String password) {
        this.account = null;
        this.password = null;
        if (getView() != null) {
            getView().progress("正在生成邮箱.....");
        }
        if (TextUtils.isEmpty(accout.split("@")[0])) {
            accout = StringUtil.generateRandomNumber(9) + accout;
        }

        if (TextUtils.isEmpty(password)) {
            //默认8个1
            password = "11111111";
        }
        this.account = accout;
        this.password = password;
        MailHelper.getInstance().applyMail(accout, new DataSource.Callback<String>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                if (getView() != null) {
                    getView().showError(strRes);
                }
            }

            @Override
            public void onDataLoad(String account) {
                senMailCode(account);
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param account
     */
    @Override
    public void senMailCode(final String account) {
        if (getView() != null) {
            getView().progress("正在发送邮箱验证码....");
        }
        LogUtil.logI("发送验证码的账户",account);
        SpeedSSHelper.sendMailCode(account, new DataSource.Callback<Result>() {

            @Override
            public void onDataLoad(Result result) {
                //开始接收邮件
                if (result.getRet() == 1) {
                    receiveMail(account);
                } else {
                    if (getView() != null) {
                        getView().showError(0);
                    }
                }
            }

            @Override
            public void onDataNotAvailable(int strRes) {
                if (getView() != null) {
                    getView().showError(strRes);
                }
            }
        });
    }

    /**
     * 接收验证码
     *
     * @param account
     */

    @Override
    public void receiveMail(final String account) {
        if (getView() != null) {
            getView().progress("正在接收邮箱验证码.....");
        }
        MailHelper.getInstance().receiveMailCode(account, (System.currentTimeMillis() / 1000), new DataSource.Callback<String>() {

            @Override
            public void onDataLoad(String code) {
                if (!TextUtils.isEmpty(code)) {
                    //获取网址
                    register(account, password, code);
                    return;
                } else {
                    Message message = Message.obtain();
                    message.obj = account;
                    handler.sendMessageDelayed(message, 3000);
                }
            }

            @Override
            public void onDataNotAvailable(int strRes) {
//                Message message = Message.obtain();
//                message.obj = account;
//                handler.sendMessageDelayed(message, 2000);
                getView().showError(strRes);
            }
        });

    }

    /**
     * 解析邮件的验证码
     *
     * @param url
     */

    @Override
    public void parseMail(String url) {
//
//
//        if (getView() != null) {
//            getView().progress("正在接收邮箱验证码2.....");
//        }
//        MailHelper.getInstance().visitMail(url, new DataSource.Callback<String>() {
//
//            @Override
//            public void onDataLoad(String s) {
//                if (TextUtils.isEmpty(s)) {
//                    getView().showError(R.string.ERROR_SEND_MAIL_CODE);
//                    return;
//                }
//                register(account, password, s);
//            }
//
//            @Override
//            public void onDataNotAvailable(int strRes) {
//                if (getView() != null) {
//                    getView().showError(strRes);
//                }
//            }
//        });
    }

    /**
     * 正式利用邮件注册账号
     *
     * @param account
     * @param password
     * @param code
     */
    @Override
    public void register(final String account, final String password, String code) {
        if (getView() != null) {
            getView().progress("正在注册.....");
        }
        LogUtil.logI("正在注册","邮箱"+account+"验证码"+code);
        SpeedSSHelper.register(account, password, code, new DataSource.Callback<Result>() {

            @Override
            public void onDataLoad(Result result) {
                if (getView() != null) {
                    getView().showResult(result, account, password);
                    boolean b = DbHelper.save(account, password);
                    if (!b) {
                        getView().showError(R.string.ERROR_SAVA_FAIL);
                    }
                }
            }

            @Override
            public void onDataNotAvailable(int strRes) {
                if (getView() != null) {
                    getView().showError(strRes);
                }
            }
        });
    }

    @Override
    public void downloadClientApk(String path) {
        long id = DownloadHelper.getInstance().startDownloading(path, "application/vnd.android.package-archive", "v2rayNG.apk");
        DownloadHelper.getInstance().setListener(new DownloadHelper.DownloadListener() {
            @Override
            public void downloadSucceed(Uri uri) {
                if (getView() != null) {
                    getView().downloadApkOK(uri);
                }
            }
        });
    }
}
