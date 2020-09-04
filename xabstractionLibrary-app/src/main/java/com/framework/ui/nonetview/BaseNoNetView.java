package com.framework.ui.nonetview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetRetryDelegate;
import com.xframework_uicommon.xview.xnonetview.XIBaseNoNetViewDelegate;
import com.personal.xabstractionlibrary.R;

import java.lang.ref.WeakReference;

public class BaseNoNetView extends RelativeLayout implements XIBaseNoNetViewDelegate {

    /**
     * 无网内容区
     */
    private LinearLayout noNetLayout;

    /**
     * 无网提示
     */
    private TextView noNetTipView;

    /**
     * 无网再试
     */
    private Button noNetSettingBtn;

    /**
     * 设置网络的回调代理
     */
    private WeakReference<XIBaseNoNetRetryDelegate> delegate = null;

    public BaseNoNetView(Context context) {
        super(context);
    }

    public BaseNoNetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseNoNetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public BaseNoNetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void setDelegate(XIBaseNoNetRetryDelegate newTryDelegate) {
        if(newTryDelegate == null){
            delegate = null;
        }else {
            delegate = new WeakReference<>(newTryDelegate);
        }
    }

    public XIBaseNoNetRetryDelegate getDelegate() {
        if(delegate == null)
            return null;
        else
            return delegate.get();
    }

    @Override
    public void visibleNoNet(boolean bNoNet) {
        if(bNoNet){
            setVisibility(VISIBLE);
            noNetLayout.setVisibility(VISIBLE);
            noNetTipView.setVisibility(VISIBLE);
            noNetTipView.setText("无网耶.");
        }else {
            setVisibility(GONE);
            noNetLayout.setVisibility(GONE);
            noNetTipView.setVisibility(GONE);
            noNetTipView.setText("网络正常.");
        }
    }

    @Override
    public void initView() {
        noNetLayout = (LinearLayout)findViewById(R.id.nonet_content_id);
        noNetTipView = (TextView) findViewById(R.id.nonet_tip_id);
        noNetSettingBtn = (Button) findViewById(R.id.nonet_set_id);

        noNetSettingBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDelegate() != null){
                    getDelegate().retryNoNet(true);
                }
            }
        });
    }
}
