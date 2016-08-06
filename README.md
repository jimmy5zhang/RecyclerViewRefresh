# RecyclerViewRefresh

## 采用RecyclerView和SwipeRefreshLayout实现下拉刷新和上拉自动加载更多

### 1.下拉刷新

```
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
```
    
### 2.自动加载更多


```
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
```

### 3.点击item实现

#### (1).编写单击事件接口

```
   public interface RecyclerItemClickListener {
   
       /**
        * 监听点击item事件
        * @param view
        * @param position
        */
       public void onItemClick(View view, int position);
   }
   
```

#### (2).在RecyclerView.Adapter里面添加监听

```
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
   
```

#### (3).实现点击item事件

```
   contactAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
       @Override
       public void onItemClick(View view, int position) {
          
       }
   });
   
```