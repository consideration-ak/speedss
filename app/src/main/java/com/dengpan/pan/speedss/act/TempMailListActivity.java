package com.dengpan.pan.speedss.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dengpan.pan.common.app.BaseActivity;
import com.dengpan.pan.factory.data.DataUtil;
import com.dengpan.pan.factory.model.bean.TempMailAddressBean;
import com.dengpan.pan.speedss.R;
import com.dengpan.pan.speedss.adapter.TempListAdapter;

import butterknife.BindView;

public class TempMailListActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    TempListAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_temp_mail_list;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        adapter = new TempListAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.replaceData(DataUtil.getTempMailData());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TempMailAddressBean item = TempMailListActivity.this.adapter.getItem(position);
                WebViewActivity.show(TempMailListActivity.this,item);
            }
        });
    }
}
