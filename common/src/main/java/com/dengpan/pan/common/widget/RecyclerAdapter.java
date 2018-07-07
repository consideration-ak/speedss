package com.dengpan.pan.common.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengpan.pan.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter <Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements View.OnClickListener, View.OnLongClickListener ,AdapterCallback<Data>{
    private List<Data> mDataList  ;
    private AdapterListener mListener;

    public RecyclerAdapter() {
        if(this.mDataList == null){
            mDataList = new ArrayList<>();
        }
    }

    public void setListener(AdapterListener<Data> mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if( mListener!= null){
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemClick(viewHolder,mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if( mListener!= null){
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemClick(viewHolder,mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 监听器
     * @param <Data>
     */
    public interface AdapterListener<Data>{
        void onItemClick(RecyclerAdapter.ViewHolder holder,Data data);
        void onItemLongClick(RecyclerAdapter.ViewHolder holder,Data data);
    }


    /**
     * create a viewholder
     * @param parent RecyclerView
     * @param viewType  约定为界面的 ID (R,layout.xxxx)
     * @return
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //把传进来的xml 初始化为rootview
        View root = inflater.inflate(viewType,parent,false);
        //必须实现的方法
        ViewHolder<Data> holder = onCreateViewHolder(root,viewType);
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        //设置tag 双向绑定
        root.setTag(R.id.tag_recycler_holder,holder);

        holder.unbinder= ButterKnife.bind(holder,root);
        holder.callback = this;
        return holder;
    }

    /**
     * 返回得都是xml id
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position,mDataList.get(position));
    }
    //返回xml id
    protected abstract int getItemViewType(int position,Data data);
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);
    }

    public List<Data> getItems(){
        return mDataList;
    }
    public void add(Data data){
        mDataList.add(data);
        notifyItemInserted(mDataList.size()-1);
    }
    public void add(Data... dataList){
        if(dataList != null && dataList.length>0){
            int statPos = mDataList.size();
            Collections.addAll(mDataList,dataList);
            notifyItemRangeInserted(statPos,dataList.length);
        }
    }

    @Override
    public void update(Data data, ViewHolder<Data> viewHolder) {
        int pos = viewHolder.getAdapterPosition();
        if(pos>=0){
            mDataList.remove(pos);
            mDataList.add(pos,data);
            notifyItemChanged(pos);
        }
    }
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }
    public void replace(Collection dataList){
        if(mDataList == null){
            mDataList = new LinkedList<>();
        }
        mDataList.clear();
        if( dataList == null || dataList.size() ==0){
            return;
        }else{
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }
    public void add(Collection<Data> dataList){
        if(dataList != null && dataList.size()>0){
            int statPos = mDataList.size();
            Collections.addAll(dataList);
            notifyItemRangeInserted(statPos,dataList.size());
        }
    }

    @Override
    public int getItemCount() {
        return mDataList== null? 0:mDataList.size();
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        protected Data mData;
        private Unbinder unbinder;
        private AdapterCallback callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据地触发
         * @param data
         */
        void bind(Data data){
            this.mData = data;
            onBind(data);
        }
        //绑定数据地回调，必须复写
        protected abstract void onBind(Data data);

        public void updateData(Data data){
            if(this.callback !=null){
                this.callback.update(data,this);
            }
        }
        public static class AdapterListenerImpl<Data> implements AdapterListener<Data>{
            @Override
            public void onItemClick(ViewHolder holder, Data data) {

            }

            @Override
            public void onItemLongClick(ViewHolder holder, Data data) {

            }
        }
    }
}
