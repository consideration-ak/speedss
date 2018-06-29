package com.dengpan.pan.common.factory.presenter;

public class BasePresenter <V extends BaseContract.View> implements BaseContract.Presenter {
    private V mView;

    public BasePresenter(V view) {
        setView(view);
    }

    private void setView(V view) {
        this.mView =view;
        this.mView.setPresenter(this);
    }
    public final V getView(){
        return mView;
    }

    @Override
    public void start() {
        if(mView !=  null){
            mView.showLoading();
        }
    }

    @Override
    public void destroy() {
        if(mView != null){
            mView.setPresenter(null);
            mView = null;
        }
    }
}
