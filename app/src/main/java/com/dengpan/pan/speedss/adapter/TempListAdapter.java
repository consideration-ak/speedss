package com.dengpan.pan.speedss.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dengpan.pan.factory.model.bean.TempMailAddressBean;
import com.dengpan.pan.speedss.R;

import java.util.List;

public class TempListAdapter extends BaseQuickAdapter<TempMailAddressBean,BaseViewHolder> {
    public TempListAdapter() {
        super(R.layout.item_temp_mail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, TempMailAddressBean item) {
        helper.setText(R.id.tv_temp_mail_name,item.getNetName());
    }
}
