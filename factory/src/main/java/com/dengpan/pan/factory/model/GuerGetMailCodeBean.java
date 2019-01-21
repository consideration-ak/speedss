package com.dengpan.pan.factory.model;

import java.util.List;

public class GuerGetMailCodeBean {


    /**
     * list : [{"mail_id":"458531653","mail_from":"speedss.top@gmail.com","mail_subject":"Speedss 邮箱验证","mail_excerpt":"尊敬的用户：\n  您的邮箱验证代码为: X6qnJF5r，请在网页中填写，完成验证。(本验证代码有效期 30 分钟)Speedss","mail_timestamp":"1547534042","mail_read":"0","mail_date":"06:34:02","att":"0","mail_size":"2953"}]
     * count : 1
     * email : xingfu@guerrillamailblock.com
     * alias : gwzpr5+2morjmprlu
     * ts : 1547533996
     * sid_token : auf6h9clmg8c5ddr8n5fra23j7
     * stats : {"sequence_mail":"55,384,356","created_addresses":38163242,"received_emails":"10,593,641,439","total":"10,538,257,083","total_per_hour":"192650"}
     * auth : {"success":true,"error_codes":[]}
     */

    private String count;
    private String email;
    private String alias;
    private int ts;
    private String sid_token;
    private StatsBean stats;
    private AuthBean auth;
    private List<ListBean> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public String getSid_token() {
        return sid_token;
    }

    public void setSid_token(String sid_token) {
        this.sid_token = sid_token;
    }

    public StatsBean getStats() {
        return stats;
    }

    public void setStats(StatsBean stats) {
        this.stats = stats;
    }

    public AuthBean getAuth() {
        return auth;
    }

    public void setAuth(AuthBean auth) {
        this.auth = auth;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class StatsBean {
        /**
         * sequence_mail : 55,384,356
         * created_addresses : 38163242
         * received_emails : 10,593,641,439
         * total : 10,538,257,083
         * total_per_hour : 192650
         */

        private String sequence_mail;
        private int created_addresses;
        private String received_emails;
        private String total;
        private String total_per_hour;

        public String getSequence_mail() {
            return sequence_mail;
        }

        public void setSequence_mail(String sequence_mail) {
            this.sequence_mail = sequence_mail;
        }

        public int getCreated_addresses() {
            return created_addresses;
        }

        public void setCreated_addresses(int created_addresses) {
            this.created_addresses = created_addresses;
        }

        public String getReceived_emails() {
            return received_emails;
        }

        public void setReceived_emails(String received_emails) {
            this.received_emails = received_emails;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal_per_hour() {
            return total_per_hour;
        }

        public void setTotal_per_hour(String total_per_hour) {
            this.total_per_hour = total_per_hour;
        }
    }

    public static class AuthBean {
        /**
         * success : true
         * error_codes : []
         */

        private boolean success;
        private List<?> error_codes;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<?> getError_codes() {
            return error_codes;
        }

        public void setError_codes(List<?> error_codes) {
            this.error_codes = error_codes;
        }
    }

    public static class ListBean {
        /**
         * mail_id : 458531653
         * mail_from : speedss.top@gmail.com
         * mail_subject : Speedss 邮箱验证
         * mail_excerpt : 尊敬的用户：
         您的邮箱验证代码为: X6qnJF5r，请在网页中填写，完成验证。(本验证代码有效期 30 分钟)Speedss
         * mail_timestamp : 1547534042
         * mail_read : 0
         * mail_date : 06:34:02
         * att : 0
         * mail_size : 2953
         */

        private String mail_id;
        private String mail_from;
        private String mail_subject;
        private String mail_excerpt;
        private String mail_timestamp;
        private String mail_read;
        private String mail_date;
        private String att;
        private String mail_size;

        public String getMail_id() {
            return mail_id;
        }

        public void setMail_id(String mail_id) {
            this.mail_id = mail_id;
        }

        public String getMail_from() {
            return mail_from;
        }

        public void setMail_from(String mail_from) {
            this.mail_from = mail_from;
        }

        public String getMail_subject() {
            return mail_subject;
        }

        public void setMail_subject(String mail_subject) {
            this.mail_subject = mail_subject;
        }

        public String getMail_excerpt() {
            return mail_excerpt;
        }

        public void setMail_excerpt(String mail_excerpt) {
            this.mail_excerpt = mail_excerpt;
        }

        public String getMail_timestamp() {
            return mail_timestamp;
        }

        public void setMail_timestamp(String mail_timestamp) {
            this.mail_timestamp = mail_timestamp;
        }

        public String getMail_read() {
            return mail_read;
        }

        public void setMail_read(String mail_read) {
            this.mail_read = mail_read;
        }

        public String getMail_date() {
            return mail_date;
        }

        public void setMail_date(String mail_date) {
            this.mail_date = mail_date;
        }

        public String getAtt() {
            return att;
        }

        public void setAtt(String att) {
            this.att = att;
        }

        public String getMail_size() {
            return mail_size;
        }

        public void setMail_size(String mail_size) {
            this.mail_size = mail_size;
        }
    }

    @Override
    public String toString() {
        return "GuerGetMailCodeBean{" +
                "count='" + count + '\'' +
                ", email='" + email + '\'' +
                ", alias='" + alias + '\'' +
                ", ts=" + ts +
                ", sid_token='" + sid_token + '\'' +
                ", stats=" + stats +
                ", auth=" + auth +
                ", list=" + list +
                '}';
    }
}
