package com.framework.ui.nodataview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataRetryDelegate;
import com.xframework_uicommon.xview.xnodataview.XIBaseNoDataViewDelegate;
import com.personal.xabstractionlibrary.R;

import java.lang.ref.WeakReference;

public class BaseNoDataView extends RelativeLayout implements XIBaseNoDataViewDelegate {

    /**
     * 无数据内容区
     */
    private LinearLayout noDataLayout = null;

    /**
     * 无数据提示
     */
    private TextView noDataTipView = null;

    /**
     * 无数据重试
     */
    private Button noDataRetryBtn = null;

    /**
     * 无数据重试代理
     */
    private WeakReference<XIBaseNoDataRetryDelegate> delegate = null;

    public BaseNoDataView(Context context) {
        super(context);
    }

    public BaseNoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseNoDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public BaseNoDataView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public XIBaseNoDataRetryDelegate getDelegate() {
        XIBaseNoDataRetryDelegate noDataRetryDelegate = null;
        if(delegate != null){
            noDataRetryDelegate = delegate.get();
        }
        return noDataRetryDelegate;
    }

    public void setDelegate(XIBaseNoDataRetryDelegate retryNewDelegate) {
        if(retryNewDelegate == null){
            delegate = null;
        }else{
            delegate = new WeakReference<>(retryNewDelegate);
        }
    }

    @Override
    public void initView() {
        noDataLayout = (LinearLayout) findViewById(R.id.noData_Content_id);
        noDataTipView = (TextView) findViewById(R.id.noData_tip_id);
        noDataRetryBtn = (Button) findViewById(R.id.noData_Set_id);

        noDataRetryBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDelegate() != null){
                    getDelegate().retryNoData();
                }
            }
        });
    }

    @Override
    public void visibleNoData(Boolean bNoData) {
        if(bNoData){
            setVisibility(VISIBLE);
            noDataLayout.setVisibility(VISIBLE);
            noDataTipView.setVisibility(VISIBLE);
            noDataTipView.setText("无数据耶");
        }else {
            setVisibility(GONE);
            noDataLayout.setVisibility(GONE);
            noDataTipView.setVisibility(GONE);
            noDataTipView.setText("明明有数据啊");
        }
    }

}
