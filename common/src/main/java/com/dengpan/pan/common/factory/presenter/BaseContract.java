package com.dengpan.pan.common.factory.presenter;

import android.support.annotation.StringRes;

import com.dengpan.pan.common.widget.RecyclerAdapter;

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
    interface RecyclerView<P extends Presenter,M> extends View<P>{
        // 拿到一个适配器，然后自己自主的进行刷新
        RecyclerAdapter<M> getRecyclerAdapter();
        //适配器数据更改后触发
        void onAdapterDataChanged();
    }
}
