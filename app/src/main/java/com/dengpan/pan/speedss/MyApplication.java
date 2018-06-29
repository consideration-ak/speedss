package com.dengpan.pan.speedss;

import android.app.Application;
import android.util.Log;

import com.dengpan.pan.common.app.BaseApplication;
import com.dengpan.pan.factory.Factory;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.logging.Level;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化工厂类，和网络请求框架
        Factory.init();
    }
}
