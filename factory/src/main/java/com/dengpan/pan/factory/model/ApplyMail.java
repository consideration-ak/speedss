package com.dengpan.pan.factory.model;

public class ApplyMail {


    /**
     * delay : 10:00
     * tips :
     * user : 341jfwml@www.bccto.me
     * success : true
     * time : 1529946719
     */

    private String delay;
    private String tips;
    private String user;
    private boolean success;
    private int time;

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
}
