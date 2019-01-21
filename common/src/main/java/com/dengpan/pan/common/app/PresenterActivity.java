package com.dengpan.pan.common.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.dengpan.pan.common.R;
import com.dengpan.pan.common.factory.presenter.BaseContract;

public abstract class PresenterActivity<Presenter extends BaseContract.Presenter> extends BaseActivity implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;
    protected ProgressDialog mLoadingDialog;

    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }

    protected abstract Presenter initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.destroy();
        }
    }

    @Override
    public void showLoading() {
        ProgressDialog dialog = mLoadingDialog;
        if(dialog == null){
            dialog = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            mLoadingDialog = dialog;
        }
        dialog.setMessage("Loading");
        dialog.show();
    }

    @Override
    public void showError(int strRes) {
        if(strRes == 0){
            BaseApplication.showToast("请求结果错误");
        }else {
            BaseApplication.showToast(strRes);
        }
        hideLoadingDialog();
    }

    public void hideLoadingDialog() {
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
            mLoadingDialog= null;
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.mPresenter =presenter;
    }
}
