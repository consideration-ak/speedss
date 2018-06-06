package com.dengpan.pan.speedss;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import java.io.File;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String apkUrl = "https://download.speedss.top/v2rayNG.apk";
    @BindView(R.id.tv_get_account)
    TextView tv_get_account;
    @BindView(R.id.btn_create_account)
    Button btn_create_account;
    @BindView(R.id.et_self_account)
    EditText et_account;
    @BindView(R.id.et_self_password)
    EditText et_password;
    Gson gson;

    DownloadManager downloadManager;

    @BindView(R.id.btn_downloadApk)
    Button btn_download;
    MaterialDialog dialog;
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        gson = new Gson();
    }

    @OnClick(R.id.btn_downloadApk)
    void onDownnloadApkClick() {
        downloadWithOkGo();
//        downloadWithDownloadManager();

    }

    //用系统下载管理器下载
    private void downloadWithDownloadManager() {
        //使用DownLoadManager来下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        //将文件下载到自己的Download文件夹下,必须是External的
        //这是DownloadManager的限制
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "v2rayNG.apk");

        request.setTitle("标题");
        request.setDescription("描述");
//设置Notification的显示，和隐藏。
        request.setNotificationVisibility(0);
        request.setDestinationUri(Uri.fromFile(file));
        request.setMimeType("application/vnd.android.package-archive");
        //添加请求 开始下载
        long downloadId = downloadManager.enqueue(request);

    }

    private void downloadWithOkGo() {
        HttpHeaders headers = new HttpHeaders();
//        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
//        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        OkGo.get(apkUrl)
                .tag(this)
                .headers(headers)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        installApk(file);

                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        btn_download.setText("下载完成"+ floatToString(progress*100)+"%");

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(MainActivity.this, "下载失败" + e.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
    }

    /**
     * 下载完成后安装APK
     * @param file
     */
    private void installApk(File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//4.0以上系统弹出安装成功打开界面
//        startActivity(intent);
    }

    /**
     *  去小数点后2位
     * @param num
     * @return
     */

    public static String floatToString(float num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    @OnClick(R.id.btn_create_account)
    public void createAccount() {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(account)) {
            //随机生成一个邮箱地址
            account = generateRandomNumber(9) + "@qq.com";
        } else {
            if (!isEmail(account)) {
                Toast.makeText(this, "请输入正确的邮箱地址", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (TextUtils.isEmpty(password)) {
            password = "11111111";
        }
        initDialog();

        getAccount(account, password);

    }

    private boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
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

    private void getAccount(final String account, final String password) {
        OkGo.post("https://speedss.top/auth/register")//
                .tag(this)//
                .params("email", account)//  这里不要使用params，upString 与 params 是互斥的，只有 upString 的数据会被上传
                .params("passwd", password)
                .params("name", getRandomStr())
                .params("fingerprint", generateRandomNumber(10) + "")
//                .upString("这是要上传的长文本数据！")//
//	.upString("这是要上传的长文本数据！", MediaType.parse("application/xml")) // 比如上传xml数据，这里就可以自己指定请求头
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
//                        Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
//                        Log.e("Main",s);
//                        Log.e("Main",response.body().toString());
                        if (!TextUtils.isEmpty(s)) {
                            Result result = gson.fromJson(s, Result.class);
                            if (result != null && result.getRet() == 1) {
                                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "注册失败" + result.getMsg(), Toast.LENGTH_LONG).show();
                            }
                            tv_get_account.setText("账号：" + account + "\n密码：" + password);

                        } else {
                            Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                    }
                });
    }

    protected long generateRandomNumber(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        return (long) (Math.random() * 9 * Math.pow(10, n - 1)) + (long) Math.pow(10, n - 1);
    }

    private String getRandomStr() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 10);
    }
}
