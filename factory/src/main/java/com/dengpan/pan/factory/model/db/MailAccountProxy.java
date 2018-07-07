package com.dengpan.pan.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.io.Serializable;
import java.util.UUID;

public class MailAccountProxy extends BaseDbModel implements Serializable{

    private String id;
    private String mail;
    private String password;
    private String createDate;

    public MailAccountProxy() {
    }

    public MailAccountProxy(String id, String mail, String password, String createDate) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.createDate = createDate;
    }

    public MailAccountProxy(String mail, String password) {
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
