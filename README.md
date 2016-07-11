# RecyclerViewRefresh

## 采用RecyclerView和SwipeRefreshLayout实现下拉刷新和上拉自动加载更多


### 1.下拉刷新

<code>

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

</code>