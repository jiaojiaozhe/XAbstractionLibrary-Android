package com.xframework_uicommon.xview.xpullrefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import cn.appsdream.nestrefresh.normalstyle.NestRefreshLayout;


/**
 * Created by lanbiao on 2018/06/15
 * 滑动列表自定义头部
 */
public class XCustomHeaderView extends android.support.v7.widget.AppCompatTextView implements NestRefreshLayout.LoaderDecor {
    public XCustomHeaderView(Context context) {
        super(context);
    }

    public XCustomHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XCustomHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollRate(int y) {

    }

    @Override
    public void setState(int state) {
        if(state == STATE_NORMAL){
            //未达到临界点的滑动过程中状态
        }else if(state == STATE_READY){
            //准备加载状态，相当于放手就加载的意思
        }else if(state == STATE_REFRESHING){
            //加载过程中
        }else if(state == STATE_ALL){

        }else if(state == STATE_SUCCESS){

        }
    }
}
