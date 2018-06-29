package com.dengpan.pan.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        if(initArgs(getIntent().getExtras())){
            setContentView(getContentLayoutId());
            ButterKnife.bind(this);
            initBefore();
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化控件
     */
    protected void initWidget() {

    }

    /**
     * 初始化控件之前的方法
     */
    protected void initBefore() {
    }

    /**
     * 初始化上个页面出来的参数，默认为True 处理之后，可能会返回false
     * @return
     */
    protected boolean initArgs(Bundle args) {
        return true;
    }

    /**
     * 初始化window 在布局之前
     */
    protected void initWindow() {

    }

    /**
     * 获取一个布局的资源ID
     * @return
     */
    protected abstract int getContentLayoutId();

}
