package com.framework.ui.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xframework_uicommon.xview.xloadingview.XIBaseLoadingViewDelegate;
import com.zhht.xabstractionlibrary.R;

public class BaseLoadingView extends RelativeLayout implements XIBaseLoadingViewDelegate {

    /**
     * 请求加载状态区
     */
    private LinearLayout loadStateLayout = null;

    /**
     * 加载过程中显示的文字
     */
    private TextView loadStateText = null;

    public BaseLoadingView(Context context) {
        super(context);
    }

    public BaseLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public BaseLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    public void visibleLoading(Boolean bVisible) {
        if(bVisible){
            setVisibility(VISIBLE);
            loadStateLayout.setVisibility(VISIBLE);
            loadStateText.setVisibility(VISIBLE);
        }else {
            setVisibility(GONE);
            loadStateLayout.setVisibility(GONE);
            loadStateText.setVisibility(GONE);
        }
    }

    @Override
    public void initView() {
        loadStateLayout = (LinearLayout)findViewById(R.id.request_loading_id);
        loadStateText = (TextView)findViewById(R.id.request_loading_state_id);
    }

    @Override
    public void startLoad() {
        loadStateLayout.setVisibility(VISIBLE);
        loadStateText.setVisibility(VISIBLE);
        loadStateText.setText("开始准备加载");
    }

    @Override
    public void loadProgress(long progress, long totalProgress) {
        loadStateLayout.setVisibility(VISIBLE);
        loadStateText.setVisibility(VISIBLE);
        loadStateText.setText("加载过程中");
    }

    @Override
    public void completeLoad(boolean bSuccess) {
        if(bSuccess){
            loadStateLayout.setVisibility(GONE);
            loadStateText.setVisibility(GONE);
        }else {
            loadStateLayout.setVisibility(GONE);
            loadStateText.setVisibility(GONE);
        }
    }
}
