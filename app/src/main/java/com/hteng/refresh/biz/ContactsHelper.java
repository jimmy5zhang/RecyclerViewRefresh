package com.hteng.refresh.biz;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.hteng.refresh.vo.ContactVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人内容提供者帮助类
 * 开发者：jimmyzhang
 * 日期：16/7/11
 * Copyright (C) 2016 深圳市华腾科技有限公司
 * 版权所有
 */
public class ContactsHelper {

    private Context context;

    public ContactsHelper(Context context) {
        this.context =context;
    }

    /**
     * 获取系统联系人信息
     * @return
     */
    public List<ContactVo> getSystemContactInfos(){

        List<ContactVo> contactList = new ArrayList<ContactVo>();

        // 使用ContentResolver查找联系人数据
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);

        // 遍历查询结果，获取系统中所有联系人
        while (cursor.moveToNext())
        {
            ContactVo contactVo =new ContactVo();
            // 获取联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的名字
            String name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));
            contactVo.setContactName(name);

            // 使用ContentResolver查找联系人的电话号码
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = " + contactId, null, null);

            // 遍历查询结果，获取该联系人的多个电话号码
            while (phones.moveToNext())
            {
                // 获取查询结果中电话号码列中数据。
                String phoneNumber = phones.getString(phones
                        .getColumnIndex(ContactsContract
                                .CommonDataKinds.Phone.NUMBER));
                contactVo.setPhoneNumber(phoneNumber);
            }
            phones.close();

            contactList.add(contactVo);
            contactVo=null;
        }
        cursor.close();

        return contactList;

    }

    /**
     * 分页查询系统联系人信息
     * @param pageSize 每页最大的数目
     * @param currentOffset 当前的偏移量
     * @return
     */
    public List<ContactVo> getContactsByPage(int pageSize, int currentOffset) {

        List<ContactVo> contactList=new ArrayList<ContactVo>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,ContactsContract.CommonDataKinds.Phone.CONTACT_ID, "sort_key"};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, " sort_key COLLATE LOCALIZED asc limit " + pageSize + " offset " + currentOffset);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                ContactVo contactVo =new ContactVo();
                String contactName = cursor.getString(0);
                String phoneNumber = cursor.getString(1);
                int offset = cursor.getInt(2);
                contactVo.setContactName(contactName);
                contactVo.setPhoneNumber(phoneNumber);
                contactVo.setOffset(offset);
                contactList.add(contactVo);
                contactVo=null;
            }
            cursor.close();
        }
        return contactList;
    }

    /**
     * 获得系统联系人的所有记录数目
     * @return
     */
    public int getAllCounts(){
        int num=0;
        // 使用ContentResolver查找联系人数据
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);
        // 遍历查询结果，获取系统中所有联系人
        while (cursor.moveToNext())
        {
            num++;
        }
        cursor.close();
        return num;
    }
}
