package com.dengpan.pan.speedss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dengpan.pan.common.app.PresenterActivity;
import com.dengpan.pan.common.widget.RecyclerAdapter;
import com.dengpan.pan.factory.model.db.MailAccount;
import com.dengpan.pan.factory.model.db.MailAccountProxy;
import com.dengpan.pan.factory.presenter.listaccount.AccountListContract;
import com.dengpan.pan.factory.presenter.listaccount.AccountListPresenter;
import com.dengpan.pan.speedss.utils.CopyUtil;
import com.dengpan.pan.speedss.utils.TimeRestUtil;

import java.util.List;

import butterknife.BindView;

public class AccountListActivity extends PresenterActivity<AccountListContract.Presenter> implements AccountListContract.View {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    public static void show(Context context){
        Intent intent = new Intent(context,AccountListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account_list;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new Adapter());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getAccountList();
    }

    @Override
    protected AccountListContract.Presenter initPresenter() {
        return new AccountListPresenter(this);
    }

    @Override
    public void load(List<MailAccountProxy> list) {
        mAdapter.replace(list);
    }

    @Override
    public RecyclerAdapter<MailAccountProxy> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((Adapter)getRecyclerAdapter()).cancelAllTimers();
    }

    @Override
    public void onAdapterDataChanged() {

    }
    class Adapter extends RecyclerAdapter<MailAccountProxy>{
        public SparseArray<CountDownTimer> countDownTimerMap =new SparseArray<>();

        @Override
        protected int getItemViewType(int position, MailAccountProxy mailAccountProxy) {
            return R.layout.cell_account_list;
        }

        @Override
        protected ViewHolder<MailAccountProxy> onCreateViewHolder(View root, int viewType) {
            return new ViewHoler(root);
        }
        /**
         * 清空资源
         */
        public void cancelAllTimers() {
            if (countDownTimerMap == null) {
                return;
            }
            for (int i = 0,length = countDownTimerMap.size(); i < length; i++) {
                CountDownTimer cdt = countDownTimerMap.get(countDownTimerMap.keyAt(i));
                if (cdt != null) {
                    cdt.cancel();
                }
            }
        }
    }
    class ViewHoler extends RecyclerAdapter.ViewHolder<MailAccountProxy>{
        @BindView(R.id.account)
        TextView tv_account;
//        @BindView(R.id.password)
//        TextView tv_password;
        @BindView(R.id.time_rest)
        TextView tv_time;
        CountDownTimer countDownTimer;

        public ViewHoler(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(MailAccountProxy mailAccount) {
            if(countDownTimer != null){
                countDownTimer.cancel();
            }else{

                if(TimeRestUtil.RestTime(mailAccount.getCreateDate()).equals("已过期")){
                    tv_time.setText(TimeRestUtil.RestTime(mailAccount.getCreateDate()));
                }else{
                    countDownTimer = new CountDownTimer(TimeRestUtil.restTimeLong(mailAccount.getCreateDate()),1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tv_time.setText(TimeRestUtil.RestTime(millisUntilFinished));
                        }

                        @Override
                        public void onFinish() {
                            tv_time.setText("已过期");
                        }
                    }.start();
                    ((Adapter)getRecyclerAdapter()).countDownTimerMap.put(tv_time.hashCode(), countDownTimer);
                }
            }
            tv_account.setText(mailAccount.getMail());
//            tv_password.setText(mailAccount.getPassword());
            tv_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CopyUtil.copy(AccountListActivity.this,tv_account.getText().toString());
                    Toast.makeText(AccountListActivity.this,"已经复制账户到粘贴板",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
