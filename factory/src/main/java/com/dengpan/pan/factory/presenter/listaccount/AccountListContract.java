package com.dengpan.pan.factory.presenter.listaccount;

import com.dengpan.pan.common.factory.presenter.BaseContract;
import com.dengpan.pan.factory.model.db.MailAccount;
import com.dengpan.pan.factory.model.db.MailAccountProxy;

import java.util.List;

public interface AccountListContract {
    interface Presenter extends BaseContract.Presenter{
        void getAccountList();
    }
    interface View extends BaseContract.RecyclerView<Presenter,MailAccountProxy>{
        void load(List<MailAccountProxy> list);
    }
}
