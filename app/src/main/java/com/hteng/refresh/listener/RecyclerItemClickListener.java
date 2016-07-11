package com.hteng.refresh.listener;

import android.view.View;

/**
 * RecyclerView单击item监听器
 * 开发者：jimmyzhang
 * 日期：16/7/11
 * Copyright (C) 2016 深圳市华腾科技有限公司
 * 版权所有
 */
public interface RecyclerItemClickListener {

    /**
     * 监听点击item事件
     * @param view
     * @param position
     */
    public void onItemClick(View view, int position);
}
