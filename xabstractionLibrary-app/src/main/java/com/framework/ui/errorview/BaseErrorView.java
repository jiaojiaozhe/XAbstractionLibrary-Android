package com.framework.ui.errorview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xframework_uicommon.xview.xerrorview.XIBaseErrorViewDelegate;
import com.xframework_uicommon.xview.xerrorview.XIBaseRetryDelegate;
import com.zhht.xabstractionlibrary.R;

import java.lang.ref.WeakReference;

public class BaseErrorView extends RelativeLayout implements XIBaseErrorViewDelegate {

    /**
     * 请求错误内容区
     */
    private LinearLayout errorLayout = null;

    /**
     * 错误提示显示
     */
    private TextView errorTipView = null;

    /**
     * 重试按钮
     */
    private Button errorRetryBtn = null;

    private WeakReference<XIBaseRetryDelegate> delegate;

    public BaseErrorView(Context context) {
        super(context);
    }

    public BaseErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public BaseErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void setDelegate(XIBaseRetryDelegate retryNewDelegate) {
        if(retryNewDelegate == null){
            delegate = null;
        }else {
            delegate = new WeakReference<XIBaseRetryDelegate>(retryNewDelegate);
        }
    }

    public XIBaseRetryDelegate getDelegate() {
        XIBaseRetryDelegate oldRetryDelegate = null;
        if(delegate != null){
            oldRetryDelegate = delegate.get();
        }
        return oldRetryDelegate;
    }

    @Override
    public void initView() {
        errorLayout = (LinearLayout)findViewById(R.id.error_content_id);
        errorTipView = (TextView) findViewById(R.id.error_tip_id);
        errorRetryBtn = (Button)findViewById(R.id.error_retry_id);
        errorRetryBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDelegate() != null){
                    getDelegate().retryControl();
                }
            }
        });
    }

    @Override
    public void visibleErrorView(Boolean bError) {
        if(!bError){
            setVisibility(GONE);
            errorLayout.setVisibility(GONE);
            errorTipView.setText("请求成功");
        }else {
            setVisibility(VISIBLE);
            errorLayout.setVisibility(VISIBLE);
            errorTipView.setText("请求出错");
        }
    }
}
