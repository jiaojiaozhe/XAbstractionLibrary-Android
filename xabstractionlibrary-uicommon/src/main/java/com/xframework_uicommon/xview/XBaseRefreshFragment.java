package com.xframework_uicommon.xview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xframework_uicommon.R;
import com.xframework_uicommon.xadapter.XBaseRefreshAdapter;
import com.xframework_uicommon.xview.xpullrefreshview.XNestRefreshLayout;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import cn.appsdream.nestrefresh.base.AbsRefreshLayout;
import cn.appsdream.nestrefresh.base.OnPullListener;
import cn.appsdream.nestrefresh.normalstyle.NestRefreshLayout;

/**
 * 下拉滑动滚动相关抽象fragment，基于RecyclerView
 * @param <M> itemView对应的数据模型
 */
public abstract class XBaseRefreshFragment<M> extends XBaseFragment implements OnPullListener{

    /**
     * 正在加载状态中
     */
    private Boolean bRefreshing;

    /**
     * 加载状态锁
     */
    private Serializable refreshLock = new Serializable(){};

    /**
     * 滑动列表控件
     */
    private RecyclerView mRecyclerView;

    /**
     * RecyclerView的抽象adapter
     */
    private XBaseRefreshAdapter aDapter;

    /**
     * 上拉下拉控件
     */
    private XNestRefreshLayout mRefreshLayout;

    /**
     * 创建对应的业业务holder
     * @param parent 父容器view，系统传递
     * @param viewType 视图类型
     * @return 返回对应的holder
     */
    public abstract XBaseRefreshViewHolder<M> createViewHolder(ViewGroup parent,int viewType);

    /**
     * 获取refresh事件的监听者对象
     * @return 返回refresh事件监听者
     */
    public abstract OnPullListener getRefreshEventListener();

    /**
     * 下拉刷新头，支持重写
     * @return 返回指定协议的view
     */
    public NestRefreshLayout.LoaderDecor refreshHeaderView(){
        return (NestRefreshLayout.LoaderDecor)View.inflate(getContext(), R.layout.layout_header,null);
    }

    /**
     * 上拉加载更多，支持重写
     * @return 返回指定协议的view
     */
    public NestRefreshLayout.LoaderDecor loadMoreView(){
        return (NestRefreshLayout.LoaderDecor)View.inflate(getContext(),R.layout.layout_footer,null);
    }

    public XBaseRefreshFragment(){
        super();
        setbRefreshing(false);
    }

    public Boolean getbRefreshing() {
        synchronized (refreshLock) {
            return bRefreshing;
        }
    }

    public void setbRefreshing(Boolean bRefreshing) {
        synchronized (refreshLock) {
            this.bRefreshing = bRefreshing;
        }
    }

    /**
     * 设置是否支持下拉刷新
     * @param enable true支持 否则不支持
     */
    public void setPullRefreshEnable(Boolean enable){
        if(mRefreshLayout != null){
            mRefreshLayout.setPullRefreshEnable(enable);
        }
    }

    /**
     * 设置是否支持上拉刷新
     * @param enable true支持 否则不支持
     */
    public void setPullLoadMoreEnable(Boolean enable){
        if(mRefreshLayout != null){
            mRefreshLayout.setPullLoadEnable(enable);
        }
    }

    /**
     * 重置数据源集合
     * @param dataSources 待重置的数据源集合
     */
    private void setDataSource(List<M> dataSources){
        getAdapter().setDataSource(dataSources);
    }

    /**
     * 添加数据源集合
     * @param dataSources 待添加的数据源集合
     */
    private void appendDataSources(List<M> dataSources){
        getAdapter().appendDataSources(dataSources);
    }

    /**
     * 添加单个数据源集合
     * @param dataSource 待添加的单个数据源
     */
    private void  appendDataSource(M dataSource){
        getAdapter().appendDataSource(dataSource);
    }

    /**
     * 清除数据源
     */
    private void clearDataSource(){
        getAdapter().clearDataSource();
    }

    private void setAdapter(XBaseRefreshAdapter adapter) {
        this.aDapter = adapter;
    }

    public XBaseRefreshAdapter getAdapter() {
        if(aDapter == null){
            aDapter = new XBaseRefreshAdapter() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return XBaseRefreshFragment.this.createViewHolder(parent,viewType);
                }
            };
        }
        return aDapter;
    }

    /**
     * 获得目前的数据源集合
     * @return 目前RecyclerView的数据源集合
     */
    private List<M> getDataSource(){
        return getAdapter().getDataSources();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.xbase_refresh_layout;
    }

    @Override
    protected void onViewCreated(Bundle saveInstanceState, View rootView) {
        if(rootView != null) {
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewId);
            mRefreshLayout = (XNestRefreshLayout) rootView.findViewById(R.id.refreshViewId);

            //layout的配置
            if(mRefreshLayout != null){
                mRefreshLayout.setHeaderView((View) refreshHeaderView());
                mRefreshLayout.setFooterView((View) loadMoreView());
                mRefreshLayout.setOnLoadingListener(this);
                setPullRefreshEnable(true);
                setPullLoadMoreEnable(true);
            }

            //recyclerView的配置
            if(mRecyclerView != null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(getAdapter());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mRecyclerView != null){
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 当下拉刷新时调用
     * @param listLoader 事件响应的对象控件
     */
    public void onRefresh(AbsRefreshLayout listLoader){
        if(!getbRefreshing()){
            setbRefreshing(true);
            setbLoading(true);
            refreshView();

            if(getRefreshEventListener() != null){
                getRefreshEventListener().onRefresh(listLoader);
            }
        }
    }

    /**
     * 下拉刷新操作完的收尾工作
     * @param dataList 刷新到的数据
     * @param bError 是否存在错误
     */
    public void refreshControl(List<M> dataList, Boolean bError){
        if(bError){
            setbError(true);
            mRefreshLayout.onLoadFinished();
        }else {
            if(dataList != null && dataList.size() > 0){
                setbIgnoreShowError(true);
                setDataSource(dataList);
                setbNotData(false);
                mRefreshLayout.onLoadFinished();
            }else {
                setbNotData(true);
                setDataSource(null);
                mRefreshLayout.onLoadFinished();
            }
        }
        setbRefreshing(false);
        setbLoading(false);
        refreshView();
    }

    /**
     * 当上拉刷新时调用
     * @param listLoader 事件响应的对象控件
     */
    public void onLoading(AbsRefreshLayout listLoader){
        if(!getbRefreshing()){
            setbRefreshing(true);
            setbLoading(true);
            refreshView();

            if(getRefreshEventListener() != null){
                getRefreshEventListener().onLoading(listLoader);
            }
        }
    }

    /**
     * 上拉加载更多操作完的收尾工作
     * @param dataList 刷新到的数据
     * @param bError 是否存在错误
     */
    public void loadMoreControl(List<M> dataList,Boolean bError){
        if(!bError){
            if(dataList != null && dataList.size() > 0){
                mRefreshLayout.onLoadFinished();
                appendDataSources(dataList);
            }else {
                mRefreshLayout.onLoadFinished();
            }
        }else {
            mRefreshLayout.onLoadFinished();
        }
        setbRefreshing(false);
        setbLoading(false);
        refreshView();
    }


    /**
     * XBaseRefreshFragment的viewHolder
     * @param <M> 对应的数据模型
     */
    public abstract static class XBaseRefreshViewHolder<M> extends RecyclerView.ViewHolder{

        public XBaseRefreshViewHolder(View itemView) {
            super(itemView);

            Class cls = this.getClass();
            Field[] t = cls.getDeclaredFields();
            for (Field field : t) {
                Class<?> type = field.getType();
                boolean assignableFrom = View.class.isAssignableFrom(type);
                if (assignableFrom) {
                    field.setAccessible(true);
                    try {
                        field.set(this, itemView.findViewById(itemView.getContext().getResources().getIdentifier(field.getName(), "id", itemView.getContext().getPackageName())));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
            itemView.setTag(this);
        }

        /**
         * 绑定的数据到viewHolder
         * @param data 待绑定的数据
         */
        public abstract void bindData(M data);
    }
}
