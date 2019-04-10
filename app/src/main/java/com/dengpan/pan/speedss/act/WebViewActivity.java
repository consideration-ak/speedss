package com.dengpan.pan.speedss.act;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dengpan.pan.common.app.BaseActivity;
import com.dengpan.pan.factory.model.bean.TempMailAddressBean;
import com.dengpan.pan.speedss.R;
import com.dengpan.pan.speedss.RegistActivity;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.ll_container)
    LinearLayout ll_container;
    TempMailAddressBean bean;
    private AgentWeb mAgentWeb;

    public static void show(Context context,TempMailAddressBean bean){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("data",bean);
        context.startActivity(intent);
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_web_view;
    }
    @Override
    protected boolean initArgs(Bundle args) {
        bean = (TempMailAddressBean) getIntent().getSerializableExtra("data");
        return super.initArgs(args);
    }

    @Override
    protected void initData() {
        super.initData();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll_container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(bean.getNetAddress());

    }
    @OnClick({R.id.tv_go_register})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_go_register:
                goToRegister();
                break;
        }
    }

    private void goToRegister() {
        RegistActivity.showNoFinish(this,RegistActivity.class);
    }
}
