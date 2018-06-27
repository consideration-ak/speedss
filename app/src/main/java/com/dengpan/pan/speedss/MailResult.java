package com.dengpan.pan.speedss;

import java.util.List;

public class MailResult {

    /**
     * to : nk5ubm85@www.bccto.me
     * mail : [["邓攀","dengpan616@qq.com","阿德","2018-06-26 00:18:17","r1YPejLLA4ffKKf3eZO0pZ.eml",1806]]
     * success : true
     * time : 1529943504
     */

    private String to;
    private boolean success;
    private int time;
    private List<List<String>> mail;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<List<String>> getMail() {
        return mail;
    }

    public void setMail(List<List<String>> mail) {
        this.mail = mail;
    }
}
