package com.dengpan.pan.factory.data;

import com.dengpan.pan.factory.model.db.MailAccount;
import com.dengpan.pan.factory.model.db.MailAccount_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class DbHelper {
    /**
     * 保存注册得数据到数据库
     * @param account
     * @param password
     * @return
     */
    public static boolean save(String account, String password) {
        MailAccount mailAccount = new MailAccount(account, password);
        boolean save = mailAccount.save();
        return save;
    }

    /**
     * 查找所有得账户列表
     * @return
     */

    public static List<MailAccount> getAccountList(){
        return SQLite.select()
                .from(MailAccount.class).orderBy(MailAccount_Table.createDate,true).queryList();
    }
}
