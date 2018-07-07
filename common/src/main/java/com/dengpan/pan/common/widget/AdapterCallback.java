package com.dengpan.pan.common.widget;

public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> viewHolder);
}
