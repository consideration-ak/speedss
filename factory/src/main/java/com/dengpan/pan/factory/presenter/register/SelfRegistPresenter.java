package com.dengpan.pan.factory.presenter.register;

import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.common.factory.presenter.BasePresenter;
import com.dengpan.pan.common.utils.LogUtil;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.data.DbHelper;
import com.dengpan.pan.factory.data.SpeedSSHelper;
import com.dengpan.pan.factory.model.Result;

/**
 * @author Paddy
 * @time 2019-08-25 00:23
 * @class describe
 */
public class SelfRegistPresenter extends BasePresenter<RegistContract.View> implements
RegistContract.Presenter{
    public SelfRegistPresenter(RegistContract.View view) {
        super(view);
    }

    @Override
    public void applyMail(String accout, String password) {


    }

    @Override
    public void senMailCode(final String accout) {
        if (getView() != null) {
            getView().progress("正在发送邮箱验证码....");
        }
        LogUtil.logI("发送验证码的账户",accout);
        SpeedSSHelper.sendMailCode(accout, new DataSource.Callback<Result>() {

            @Override
            public void onDataLoad(Result result) {
                //开始接收邮件
                if (result.getRet() == 1) {
                    getView().progress("邮箱发送成功，请查收");
                } else {
                    if (getView() != null) {
                        getView().showError(0);
                    }
                    getView().progress("邮箱发送失败"+result.getMsg());

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
    public void receiveMail(String account) {

    }

    @Override
    public void parseMail(String url) {

    }

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
                    getView().progress("注册成功");
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
                    getView().progress("注册失败");
                }
            }
        });
    }

    @Override
    public void downloadClientApk(String path) {

    }
}
