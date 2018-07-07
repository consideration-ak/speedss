package com.dengpan.pan.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.io.Serializable;
import java.util.UUID;

@Table(database = AppDatabase.class)
public class MailAccount extends BaseDbModel implements Serializable{
    @Column
    @PrimaryKey
    private String id;
    @Column
    private String mail;
    @Column
    private String password;
    @Column
    private String createDate;

    public MailAccount() {
    }

    public MailAccount(String mail, String password) {
        this.id = UUID.randomUUID().toString();
        this.mail = mail;
        this.password = password;
        this.createDate = System.currentTimeMillis()+"";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
