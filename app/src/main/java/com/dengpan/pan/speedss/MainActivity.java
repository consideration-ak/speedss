package com.dengpan.pan.speedss;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
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
    String baseMailUrl = "http://www.bccto.me/win/%s(a)www-_-bccto.me/%s";
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
    private String curAccount;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            checkEmail((String)msg.obj);

        }
    };

    /**
     * 检查是否收到邮件
     * @param
     */
    private void checkEmail(final String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", Common.COOKIE_MAIL);
        OkGo.post(Common.GETMAIL_URL)
                .headers(headers)
                .params("mail",email)
                .params("time",(System.currentTimeMillis()/1000))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        if(!TextUtils.isEmpty(s)){
                            MailResult result = gson.fromJson(s, MailResult.class);
                            if(result.isSuccess()){
                                //获取网址
                                if(result.getMail().size()>0){
                                    String format = String.format(baseMailUrl, curAccount, result.getMail().get(0).get(4));
                                    showToast(format);
                                    Log.e("网页的地址为：",format);
//                                    parseCode(format);
                                    getHtml(format);
                                    //解析网页获得code
                                    return;
                                }
                                else {
                                    sendHandlerMessage(email);
                                }

                            }
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("获取邮箱网址内容地址失败（网络）");
                    }
                });


    }

    /**
     * 获得HTML 然后解析
     * @param url
     *
     */
    private void getHtml(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", Common.COOKIE_MAIL);
        OkGo.get(url)
                .headers(headers)
                .params("If-None-Match","\"dd3e83b417fbfe861146392f1734996f80755598\"")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        Log.e("内容为:",s);
                        Document parse = Jsoup.parse(s);
                        String text = parse.select("body > p:nth-child(8) > b").text();
                        showToast(text);
                        Log.e("内容为:",text);
                        //开始注册
                        registerAccount(curAccount+"@www.bccto.me",text);

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("获取邮箱网址内容地址失败（网络）");
                    }
                });
    }


    /**
     * 解析发送的验证码
     * @param url
     */
    private void parseCode(String url) {
        String cur = url;
        Thread thread = new Thread(new LoadHtml(url));
        thread.start();


    }
    class LoadHtml implements Runnable{
        String url ;

        public LoadHtml(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding","gzip, deflate")
                        .cookie("Cookie", Common.COOKIE_MAIL)
                        .get();
                Elements select = document.select("body > p:nth-child(8) > b");
                String code = select.text();
                Log.e(this.getClass().getSimpleName(),code);
                showOnUi(code);
            } catch (IOException e) {
                e.printStackTrace();
                showOnUi("解析失败");
            }
        }
    }

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
            account = generateRandomNumber(9) + "@www.bccto.me";
            activateMain(account);
        } else {
            if (!isEmail(account)) {
                Toast.makeText(this, "请输入正确的邮箱地址", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (TextUtils.isEmpty(password)) {
            password = "11111111";
        }
//        initDialog();
//
//        getAccount(account, password);

    }
    private void registerAccount(String account,String verifycode){
//        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            password = "11111111";
        }
        initDialog();

        getAccount(account, password,verifycode);

    }

    /**
     * 激活邮箱
     */
    private void activateMain(final String account) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", Common.COOKIE_MAIL);
        OkGo.post("http://www.bccto.me/applymail")
                .headers(headers)
                .params("mail",account)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        if(!TextUtils.isEmpty(s)){
                            ApplyMail applyMail = gson.fromJson(s, ApplyMail.class);
                            if(applyMail.isSuccess()){
                                //1 点击发送邮箱
                                getEmailCode(account);
                                //2 等待邮箱接收
                                showToast(applyMail.getUser());
                                return;
                            }
                        }
                        showToast("激活邮箱失败");

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("激活邮件失败（网络）");
                    }
                });

    }

    /**
     * 获得邮箱验证码
     * @param account
     */
    private void getEmailCode(final String account) {
        final HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", Common.COOKIE_SPEEDS);
        OkGo.post(Common.SENCODE_URL)
                .headers(headers)
                .params("email",account)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        if(!TextUtils.isEmpty(s)){
                            Result result = gson.fromJson(s, Result.class);
                            if(result.isSucceed()){
                                //2 等待邮箱接收
                                //定时器，循环的得到邮件内容 //建议延迟5s 再获取内容
                                sendHandlerMessage(account);

                                showToast(result.getMsg());
                                return;
                            }
                        }
                        showToast("获取邮箱code失败");

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast("获取邮箱code失败（网络）");
                    }
                });
    }

    /**
     * 发送消息循环
     * @param account
     */
    private void sendHandlerMessage(String account) {
        Message message = Message.obtain();
        message.obj = account;
        handler.sendMessageDelayed(message,2000);
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

    private void getAccount(final String account, final String password,String verifycode) {
        OkGo.post("https://speedss.top/auth/register")//
                .tag(this)//
                .params("email", account)//  这里不要使用params，upString 与 params 是互斥的，只有 upString 的数据会被上传
                .params("passwd", password)
                .params("name", getRandomStr())
                .params("verifycode",verifycode)
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
                            et_account.setText(account);
                            et_password.setText(password);

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
        long num =(long) (Math.random() * 9 * Math.pow(10, n - 1)) + (long) Math.pow(10, n - 1);
        this.curAccount= num+"";
        return num;
    }

    private String getRandomStr() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 10);
    }
    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void showOnUi(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);

            }
        });
    }
}
