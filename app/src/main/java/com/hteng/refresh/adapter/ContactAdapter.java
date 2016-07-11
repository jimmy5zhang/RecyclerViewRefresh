package com.hteng.refresh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hteng.refresh.R;
import com.hteng.refresh.listener.RecyclerItemClickListener;
import com.hteng.refresh.vo.ContactVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人适配器
 * 开发者：jimmyzhang
 * 日期：16/7/11
 * Copyright (C) 2016 深圳市华腾科技有限公司
 * 版权所有
 */
public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    private List<ContactVo> contactList;
    private RecyclerItemClickListener itemClickListener;

    public ContactAdapter(Context context,List<ContactVo> contactList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.setContactList(contactList);
    }

    public void setContactList(List<ContactVo> contactList) {
        if(null == contactList){
            this.contactList = new ArrayList<ContactVo>();
        }else {
            this.contactList = contactList;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_hteng_refresh,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContactVo contactVo = contactList.get(position);
        String contactName = contactVo.getContactName();
        String phoneNumber = contactVo.getPhoneNumber();
        if(!TextUtils.isEmpty(contactName)){
            ((ViewHolder)holder).contactNameTv.setText(contactName);
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            ((ViewHolder)holder).phoneNumberTv.setText(phoneNumber);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactNameTv,phoneNumberTv;

        public ViewHolder(View itemView) {
            super(itemView);
            contactNameTv = (TextView) itemView.findViewById(R.id.hteng_contact_name_tv);
            phoneNumberTv = (TextView)itemView.findViewById(R.id.hteng_phone_number_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(null != itemClickListener){
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }

    }


    public void setOnItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
