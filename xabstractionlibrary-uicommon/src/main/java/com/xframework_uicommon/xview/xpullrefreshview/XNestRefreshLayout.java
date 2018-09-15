package com.xframework_uicommon.xview.xpullrefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.appsdream.nestrefresh.normalstyle.NestRefreshLayout;

public class XNestRefreshLayout extends NestRefreshLayout {
    public XNestRefreshLayout(Context context) {
        super(context);
    }

    public XNestRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XNestRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public XNestRefreshLayout(View listView) {
        super(listView);
    }

    @Override
    public void setHeaderView(View headerView) {
        super.setHeaderView(headerView);
    }

    @Override
    public void setFooterView(View footerView) {
        super.setFooterView(footerView);
    }
}
