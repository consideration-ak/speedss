package com.dengpan.pan.speedss;



import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.app.PresenterActivity;
import com.dengpan.pan.factory.model.Result;
import com.dengpan.pan.factory.presenter.register.RegistContract;
import com.dengpan.pan.factory.presenter.register.RegistPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends PresenterActivity<RegistContract.Presenter> implements RegistContract.View{
    @BindView(R.id.tv_get_account)
    TextView tv_get_account;
    @BindView(R.id.btn_create_account)
    Button btn_create_account;
    Button btn_download_apk;
    @BindView(R.id.et_self_account)
    EditText et_account;
    @BindView(R.id.et_self_password)
    EditText et_password;
    @BindView(R.id.spinner)
    Spinner spinner;


    MaterialDialog dialog;

    private String mail_host = "@www.bccto.me";
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_regist;
    }


    @Override
    protected RegistContract.Presenter initPresenter() {
        return new RegistPresenter(this);
    }

    @Override
    public void progress(String str) {
        if(dialog == null){
            initDialog();
        }else {
            dialog.setContent(str);
        }
    }




    @Override
    public void showResult(Result result, String account, String password) {
        if(dialog !=null)
        dialog.dismiss();
        et_account.setText(account.split("@")[0]);
        et_password.setText(password);
        tv_get_account.setText("账号：" + account + "\n密码：" + password);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) parent.getAdapter().getItem(position);
                Log.i("---","调用了一次"+text);

                mail_host = text;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        tv_get_account.setText("默认密码8个1...");
    }

    @Override
    public void downloadApkOK() {

    }
    @OnClick(R.id.btn_downloadApk)
    public void downloadApkClick(){
        Log.e("","点击了下载的类型");
        mPresenter.downloadClientApk(Common.apkUrl);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_create_account)
    public void createAccountClick(){

        Log.e("","点击了生成账号的类型");
        initDialog();
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        mPresenter.applyMail(account+mail_host,password);

    }
    private void initDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
//                .title(R.string.title)
                .content("请求中")
                .progress(true, 0);
//                .positiveText(R.string.agree)
//                .negativeText(R.string.disagree)

        dialog = builder.build();
        dialog.show();
    }

    @Override
    public void showError(int strRes) {
        super.showError(strRes);
        if(dialog != null)
        dialog.dismiss();
    }
}
