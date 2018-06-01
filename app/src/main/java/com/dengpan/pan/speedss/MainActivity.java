package com.dengpan.pan.speedss;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import com.lzy.okgo.callback.StringCallback;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_get_account)
    TextView tv_get_account;
    @BindView(R.id.btn_create_account)
    Button btn_create_account;
    @BindView(R.id.et_self_account)
    EditText et_account;
    @BindView(R.id.et_self_password)
    EditText et_password;
    Gson gson;
    MaterialDialog dialog;
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gson = new Gson();
    }
    @OnClick(R.id.btn_create_account)
    public void createAccount(){
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        if(TextUtils.isEmpty(account)){
            //随机生成一个邮箱地址
            account = generateRandomNumber(9)+"@qq.com";
        }else {
            if(!isEmail(account)){
                Toast.makeText(this,"请输入正确的邮箱地址",Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(TextUtils.isEmpty(password)){
            password = "11111111";
        }
        initDialog();

        getAccount(account,password);

    }

    private boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    private void initDialog() {
       MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
//                .title(R.string.title)
                .content("请求中")
               .progress(true,0);
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
//                .upString("这是要上传的长文本数据！")//
//	.upString("这是要上传的长文本数据！", MediaType.parse("application/xml")) // 比如上传xml数据，这里就可以自己指定请求头
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
//                        Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
//                        Log.e("Main",s);
//                        Log.e("Main",response.body().toString());
                        if(!TextUtils.isEmpty(s)){
                            Result result = gson.fromJson(s,Result.class);
                            if(result != null && result.getRet() ==1){
                                Toast.makeText(MainActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this,"注册失败"+result.getMsg(),Toast.LENGTH_LONG).show();
                            }
                            tv_get_account.setText("账号："+account+"\n密码："+password);

                        }else{
                            Toast.makeText(MainActivity.this,"注册失败",Toast.LENGTH_LONG).show();
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

    protected long generateRandomNumber(int n){
        if(n<1){
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
    }
}
