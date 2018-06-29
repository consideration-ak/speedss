package com.dengpan.pan.common.factory.presenter;

import android.support.annotation.StringRes;

public interface BaseContract {
    interface View <P extends Presenter>{
        void showError(@StringRes int strRes);
        void showLoading();
        void setPresenter(P presenter);

    }

    interface Presenter{
        /**
         * 开始
         */
        void start();

        /**
         * 结束
         */
        void destroy();
    }
}
