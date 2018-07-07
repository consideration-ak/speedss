package com.dengpan.pan.factory.presenter.listaccount;

import android.accounts.Account;

import com.dengpan.pan.common.factory.presenter.BasePresenter;
import com.dengpan.pan.factory.data.DbHelper;
import com.dengpan.pan.factory.model.db.MailAccount;
import com.dengpan.pan.factory.model.db.MailAccountProxy;

import java.util.ArrayList;
import java.util.List;

public class AccountListPresenter extends BasePresenter<AccountListContract.View> implements AccountListContract.Presenter {

    public AccountListPresenter(AccountListContract.View view) {
        super(view);
    }

    @Override
    public void getAccountList() {
        List<MailAccount> accountList = DbHelper.getAccountList();
        List<MailAccountProxy> proxies = new ArrayList<>();
        for (MailAccount account:accountList){
            MailAccountProxy proxy = new MailAccountProxy(account.getId(),account.getMail(),account.getPassword(),account.getCreateDate());
            proxies.add(proxy);
        }
        if(getView()!= null)
            getView().load(proxies);
    }
}
