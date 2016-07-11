package com.hteng.refresh.vo;

import java.io.Serializable;

/**
 * 联系人实体类
 * 开发者：jimmyzhang
 * 日期：16/7/11
 * Copyright (C) 2016 深圳市华腾科技有限公司
 * 版权所有
 */
public class ContactVo implements Serializable {

    private String contactName;
    private String phoneNumber;
    private int offset;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
