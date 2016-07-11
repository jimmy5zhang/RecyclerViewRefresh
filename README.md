# RecyclerViewRefresh

## 采用RecyclerView和SwipeRefreshLayout实现下拉刷新和上拉自动加载更多

### 1.下拉刷新

<code>
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
    
### 2.自动加载更多


<code>
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
</code>