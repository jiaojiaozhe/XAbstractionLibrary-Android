package com.framework.demo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.ui.BaseLocalRefreshFragment;
import com.personal.xabstractionlibrary.R;

import java.util.ArrayList;

import cn.appsdream.nestrefresh.base.AbsRefreshLayout;
import cn.appsdream.nestrefresh.base.OnPullListener;

public class DemoLocalRefreshFragment<M> extends BaseLocalRefreshFragment<M> {

    private OnPullListener listener = new OnPullListener() {
        @Override
        public void onRefresh(AbsRefreshLayout listLoader) {
            loadPage();
        }

        @Override
        public void onLoading(AbsRefreshLayout listLoader) {
            loadMore();
        }
    };

    @Override
    protected void onViewCreated(Bundle saveInstanceState, View rootView) {
        super.onViewCreated(saveInstanceState, rootView);
        setPullRefreshEnable(true);
        setPullLoadMoreEnable(true);
    }

    @Override
    public XBaseRefreshViewHolder createViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(getContext(), R.layout.demo_localrefresh_item_layout,null);
        return new Demo_LocalRefresh_Fragment_Holder(view);
    }

    @Override
    public OnPullListener getRefreshEventListener() {
        return listener;
    }


    public class Demo_LocalRefresh_Fragment_Holder extends XBaseRefreshViewHolder<M>{

        private TextView test_nonet_list_item_id;

        protected Demo_LocalRefresh_Fragment_Holder(View itemView) {
            super(itemView);
        }


        @Override
        public void bindData(M data) {
            if(data instanceof String)
                test_nonet_list_item_id.setText(data.toString());
        }
    }

    @Override
    protected ArrayList<M> getFirstPageData() {
        final ArrayList<M> dataSource = new ArrayList<>();
        for(int i = 1 ; i <= 30 ; i++){
            dataSource.add((M) (i + ""));
        }
        return dataSource;
    }

    @Override
    protected ArrayList<M> getNextPageData() {
        final ArrayList<M> dataSource = new ArrayList<>();
        for(int i = 1 ; i <= 30 ; i++){
            dataSource.add((M) (i + "新加的"));
        }
        return dataSource;
    }
}
