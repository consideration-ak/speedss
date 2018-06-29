package com.dengpan.pan.common.factory.data;

import android.support.annotation.StringRes;

public interface DataSource {
    /**
     * 同时关注成功和失败的回调
     * @param <T>
     */
    interface Callback<T> extends SucceedCallback<T>,FailedCallback{

    }

    /**
     * 失败的回调
     */
    interface FailedCallback{
        void onDataNotAvailable(@StringRes int strRes);
    }

    /**
     * 成功的回调
     * @param <T> 请求返回的实体
     */
    interface SucceedCallback<T>{
        void onDataLoad(T t);
    }

    /**
     * 销毁
     */
    void dispose();
}
