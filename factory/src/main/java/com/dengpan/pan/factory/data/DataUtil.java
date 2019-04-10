package com.dengpan.pan.factory.data;

import com.dengpan.pan.factory.model.bean.TempMailAddressBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 本地提供数据
 */
public class DataUtil {
    public static List<? extends TempMailAddressBean> getTempMailData() {
        List<TempMailAddressBean> list = new ArrayList<>();
        TempMailAddressBean bean = new TempMailAddressBean("http://24mail.chacuo.net/","chacuoNet");
        TempMailAddressBean bean1 = new TempMailAddressBean("https://bccto.me/","bccto");
        TempMailAddressBean bean2 = new TempMailAddressBean("https://temp-mail.org/zh/","temp-mail");
        TempMailAddressBean bean3 = new TempMailAddressBean("http://www.yopmail.com/zh/","yopmail");
        TempMailAddressBean bean4 = new TempMailAddressBean("https://www.666email.com/","666email");
        TempMailAddressBean bean5 = new TempMailAddressBean("https://10minutemail.net/","10minutemail");
        TempMailAddressBean bean6 = new TempMailAddressBean("https://www.guerrillamail.com/","guerrillamail");
//        TempMailAddressBean bean4 = new TempMailAddressBean("https://www.666email.com/","666email");

        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);

        return list;
    }
}
