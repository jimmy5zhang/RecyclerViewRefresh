package com.hteng.refresh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hteng.refresh.adapter.ContactAdapter;
import com.hteng.refresh.biz.ContactsHelper;
import com.hteng.refresh.listener.RecyclerItemClickListener;
import com.hteng.refresh.vo.ContactVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 * 开发者：jimmyzhang
 * 日期：16/7/11
 * Copyright (C) 2016 深圳市华腾科技有限公司
 * 版权所有
 */
public class MainActivity extends AppCompatActivity {

    public static int PAGE_NO = 0;
    public static final int PAGE_SIZE = 15;
    public static final int READ_CONTACTS_REQUEST_CODE = 0x0001;

    private boolean isLoadMoreAble = true,isAllowReadContacts = false;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<ContactVo> contactList,requestList;
    private LinearLayoutManager linearLayoutManager;
    private ContactAdapter contactAdapter;
    private ContactsHelper contactsHelper;
    private Handler handler;

    private void initView(){
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.hteng_refresh_layout);
        recyclerView = (RecyclerView)findViewById(R.id.hteng_recycler_view);
    }

    private void initData(){
        contactsHelper = new ContactsHelper(this);
        contactAdapter = new ContactAdapter(this,contactList);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        handler = new Handler();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_REQUEST_CODE);
        }else {
            updateContacts();
        }
    }

    private void initListeners(){

        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isAllowReadContacts){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PAGE_NO = 0;
                                contactList = contactsHelper.getContactsByPage(PAGE_SIZE,PAGE_NO);
                                contactAdapter.setContactList(contactList);
                                refreshLayout.setRefreshing(false);
                            }
                        },1000);
                    }
                }
        });

        //加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isAllowReadContacts){
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        int lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = linearLayoutManager.getItemCount();
                        if (lastVisibleItem == (totalItemCount -1) && isSlidingToLast) {
                            if(isLoadMoreAble){
                                //加载更多功能的代码
                                isLoadMoreAble = false;
                                refreshLayout.setRefreshing(true);
                                if(null != contactList && !contactList.isEmpty()){
                                    PAGE_NO = contactList.get(contactList.size()-1).getOffset();
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestList = contactsHelper.getContactsByPage(PAGE_SIZE,PAGE_NO);
                                        if(null != requestList && !requestList.isEmpty()){
                                            if(null == contactList){
                                                contactList = new ArrayList<ContactVo>();
                                            }
                                            contactList.addAll(requestList);
                                        }
                                        contactAdapter.setContactList(contactList);
                                        isLoadMoreAble = true;
                                        refreshLayout.setRefreshing(false);
                                    }
                                },1000);
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if(dy > 0){
                    //大于0表示，正在向右滚动
                    isSlidingToLast = true;
                }else{
                    //小于等于0 表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });

        /**
         * 单击item
         */
        contactAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ContactVo contactVo = contactList.get(position);
                String contactName = contactVo.getContactName();
                if(!TextUtils.isEmpty(contactName)){
                    Toast.makeText(MainActivity.this, "你点击了:"+contactName, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateContacts();
            }
        }
    }

    private void updateContacts(){
        isAllowReadContacts = true;
        contactList = contactsHelper.getContactsByPage(PAGE_SIZE,PAGE_NO);
        contactAdapter.setContactList(contactList);
    }
}
