package com.dengpan.pan.factory.model.bean;

import java.io.Serializable;

public class TempMailAddressBean implements Serializable{


    public TempMailAddressBean(String netAddress, String netName) {
        this.netAddress = netAddress;
        this.netName = netName;
    }

    private String netAddress;
    private String netName;

    public String getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }
}
