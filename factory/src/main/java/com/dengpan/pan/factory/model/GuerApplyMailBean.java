package com.dengpan.pan.factory.model;

import java.util.List;

public class GuerApplyMailBean {


    /**
     * alias_error :
     * alias : gwz25r+agqjwxhojcil2k
     * email_addr : 876563492@guerrillamailblock.com
     * email_timestamp : 1547522562
     * site_id : 1
     * sid_token : m2cqt84bl22f5n9f3e2u4p5v06
     * site : emjd
     * auth : {"success":true,"error_codes":[]}
     */

    private String alias_error;
    private String alias;
    private String email_addr;
    private int email_timestamp;
    private int site_id;
    private String sid_token;
    private String site;
    private AuthBean auth;

    public String getAlias_error() {
        return alias_error;
    }

    public void setAlias_error(String alias_error) {
        this.alias_error = alias_error;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail_addr() {
        return email_addr;
    }

    public void setEmail_addr(String email_addr) {
        this.email_addr = email_addr;
    }

    public int getEmail_timestamp() {
        return email_timestamp;
    }

    public void setEmail_timestamp(int email_timestamp) {
        this.email_timestamp = email_timestamp;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public String getSid_token() {
        return sid_token;
    }

    public void setSid_token(String sid_token) {
        this.sid_token = sid_token;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public AuthBean getAuth() {
        return auth;
    }

    public void setAuth(AuthBean auth) {
        this.auth = auth;
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
}
