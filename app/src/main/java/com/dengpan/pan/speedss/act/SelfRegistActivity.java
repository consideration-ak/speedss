package com.dengpan.pan.speedss.act;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dengpan.pan.common.app.PresenterActivity;
import com.dengpan.pan.factory.model.Result;
import com.dengpan.pan.factory.presenter.register.RegistContract;
import com.dengpan.pan.factory.presenter.register.RegistPresenter;
import com.dengpan.pan.factory.presenter.register.SelfRegistPresenter;
import com.dengpan.pan.speedss.AccountListActivity;
import com.dengpan.pan.speedss.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelfRegistActivity extends PresenterActivity<RegistContract.Presenter> implements RegistContract.View {


    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.etSelfMail)
    EditText etSelfMail;
    @BindView(R.id.etVerifyCode)
    EditText etVerifyCode;
    @BindView(R.id.etSelfPassword)
    EditText etSelfPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_self_reigist;
    }

    @Override
    protected RegistContract.Presenter initPresenter() {
        return new SelfRegistPresenter(this);
    }

    @Override
    public void progress(String str) {
        tvStatus.setText(str);
    }

    @Override
    public void showResult(Result result, String account, String password) {
        Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void downloadApkOK(Uri uri) {

    }


    @OnClick({R.id.btnGetCode, R.id.btnRegister,R.id.btnMailList})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:
                getCode();
                break;
            case R.id.btnRegister:
                String code = etVerifyCode.getText().toString();
                if(TextUtils.isEmpty(code)){
                    progress("请输入验证码再点击注册");
                    return;
                }
                register(etSelfMail.getText().toString(),etSelfPassword.getText().toString(),code);
                break;
            case R.id.btnMailList:
                AccountListActivity.show(this);
                break;
        }
    }

    private void register(String account, String password, String code) {
        if(TextUtils.isEmpty(password)){
            password = "12345678";
        }
        mPresenter.register(account,password,code);
    }

    private void getCode() {
        mPresenter.senMailCode(etSelfMail.getText().toString());
    }


}
