package com.dengpan.pan.factory;

import com.dengpan.pan.common.app.BaseApplication;
import com.dengpan.pan.factory.net.NetWork;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Factory {

    private static final Factory instance;
    private final Gson gson;
    static {
        instance = new Factory();
    }
    private Factory(){
        gson = new GsonBuilder()
                .create();
    }
    public static void init(){
        NetWork.init();
    }

    public static Gson getGson() {
        return instance.gson;
    }
    public static BaseApplication app(){
        return BaseApplication.getInstance();
    }
}
