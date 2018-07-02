package com.dengpan.pan.speedss;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class RegistActivity extends PresenterActivity<RegistContract.Presenter> implements RegistContract.View,EasyPermissions.PermissionCallbacks{

    private static final int INSTALL_PACKAGES_REQUESTCODE = 1001;
    private static final int GET_UNKNOWN_APP_SOURCES = 1002;
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

    //需要用到的权限
    private String[] params = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    MaterialDialog dialog;

    private String mail_host = "@www.bccto.me";
    //安装应用过的Uri
    private Uri uri;
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
    protected void initBefore() {
        super.initBefore();
        //获取权限
        if(!judgePermissions()){
            EasyPermissions.requestPermissions(this,"应用必须要用到的权限",1003,params);
        }

    }
    private boolean judgePermissions(){

        return EasyPermissions.hasPermissions(this,params);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_get_account.setText("默认密码8个1...");
    }

    @Override
    public void downloadApkOK(Uri uri) {
        this.uri = uri;
        //在android 8 上，是否应用安装
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if(!b){
                goToSetting();
            }else {
                installApk(uri);
            }
        }else {
            installApk(uri);
        }
    }

    /**
     * 跳往设置界面
     */
    private void goToSetting() {
        Uri contentUri = Uri.parse("package:"+getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,contentUri);
        startActivityForResult(intent, INSTALL_PACKAGES_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INSTALL_PACKAGES_REQUESTCODE){
            downloadApkOK(uri);
        }
    }
    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == INSTALL_PACKAGES_REQUESTCODE){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                installApk(uri);
//            } else {
//                goToSetting();
//            }
//        }else if(requestCode == 1003){
//            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
//        }
//    }

    /**
     * 下载完成后安装APK
     * @param
     */

    private void installApk(Uri uri) {


        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0以上的系统，要使用FileProvider的方式构建Uri
//            Uri uri = FileProvider.getUriForFile(this, "com.hm.retrofitrxjavademo.fileprovider", new File(path));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        startActivity(intent);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //判断是否是AndroidN以及更高的版本
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = uri;
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        startActivity(intent);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//4.0以上系统弹出安装成功打开界面
//        startActivity(intent);
    }
    @OnClick(R.id.btn_downloadApk)
    public void downloadApkClick(){
        Log.e("","点击了下载的类型");
        mPresenter.downloadClientApk(Common.apkUrl);
        //利用浏览器下载，但是不能提醒安装
//        downloadWithBrowser();
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
    }


    /**
     * 打开浏览器再调用系统的下载器
     */
    private void downloadWithBrowser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(Common.apkUrl));
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        initBefore();
    }
}
