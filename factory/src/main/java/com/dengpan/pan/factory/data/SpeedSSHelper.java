package com.dengpan.pan.factory.data;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dengpan.pan.common.Common;
import com.dengpan.pan.common.factory.data.DataSource;
import com.dengpan.pan.common.utils.StringUtil;
import com.dengpan.pan.factory.Factory;
import com.dengpan.pan.factory.R;
import com.dengpan.pan.factory.model.Result;
import com.dengpan.pan.factory.net.NetWork;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.dengpan.pan.common.app.BaseApplication.showToast;

public class SpeedSSHelper {
    /**
     * 给邮箱发送验证码
     * @param account
     * @param callback
     */
    public static void sendMailCode(final String account, final DataSource.Callback<Result> callback) {
        OkGo.post(Common.SENCODE_URL)
                .headers(NetWork.getSpeedHeaders())
                .params("email",account)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(this.getClass().getSimpleName(),s);
                        if(!TextUtils.isEmpty(s)){
                            Result result = Factory.getGson().fromJson(s, Result.class);
                            if(result.isSucceed()){
                                //2 等待邮箱接收
                                //定时器，循环的得到邮件内容 //建议延迟5s 再获取内容
                                callback.onDataLoad(result);
                                return;
                            }
                            callback.onDataLoad(result);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        callback.onDataNotAvailable(R.string.ERROR_SEND_MAIL_CODE_NET);
                    }
                });
    }

    /**
     * 账号的正式注册
     * @param account
     * @param password
     * @param code
     * @param callback
     */
    public static void register(String account, String password, String code, final DataSource.Callback<Result> callback) {
        OkGo.post(Common.REGISTER_URL)//
                .headers(NetWork.getSpeedHeaders())
                .params("email", account)//  这里不要使用params，upString 与 params 是互斥的，只有 upString 的数据会被上传
                .params("passwd", password)
                .params("code","396927375@qq.com")
                .params("name", StringUtil.getRandomStr())
                .params("verifycode",code)
                .params("fingerprint", StringUtil.generateRandomNumber(10) + "")
//                .upString("这是要上传的长文本数据！")//
//	.upString("这是要上传的长文本数据！", MediaType.parse("application/xml")) // 比如上传xml数据，这里就可以自己指定请求头
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s)) {
                            Result result = Factory.getGson().fromJson(s, Result.class);
                            if (result != null && result.getRet() == 1) {
                                callback.onDataLoad(result);
                            } else {
                                callback.onDataNotAvailable(R.string.ERROR_REGISTER_FAIL_WITH_RERSULT);
                            }

                        } else {
                            callback.onDataNotAvailable(R.string.ERROR_REGISTER_FAIL_WITH_RERSULT);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        callback.onDataNotAvailable(R.string.ERROR_REGISTER_FAIL_NET);
                    }
                });
    }
}
