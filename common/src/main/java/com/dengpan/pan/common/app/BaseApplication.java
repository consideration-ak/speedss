package com.dengpan.pan.common.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application{
    private static BaseApplication instance;
    private List<Activity> activities = new ArrayList<>();

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });

    }

    public static void showToast(@StringRes int strRes) {
        showToast(instance.getString(strRes));
    }
    public static void showToast(final String msg) {
        // Toast 只能在主线程中显示，所有需要进行线程转换，
        // 保证一定是在主线程进行的show操作
        final Activity topActivity = instance.activities.get(instance.activities.size()-1);

        topActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(topActivity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
