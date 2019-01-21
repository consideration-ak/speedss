package com.dengpan.pan.common.utils;

import android.util.Log;

public class LogUtil {

    private static boolean isLog = true;

    public static void logI(String tag,String msg){
        if(isLog){
            Log.i("==="+tag,msg);
        }
    }

    public static void logE(String tag,String msg){
        if(isLog){
            Log.e("==="+tag,msg);
        }
    }
}
