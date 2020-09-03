package com.xframework_uicommon.xview.xpullrefreshview;

import android.content.Context;
import android.util.AttributeSet;

import cn.appsdream.nestrefresh.normalstyle.NestRefreshLayout;


/**
 * Created by lanbiao on 2018/06/15
 * 自定义滑动加载更多
 */
public class XCustomFoodView extends androidx.appcompat.widget.AppCompatTextView implements NestRefreshLayout.LoaderDecor {
    public XCustomFoodView(Context context) {
        super(context);
    }

    public XCustomFoodView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XCustomFoodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollRate(int y) {

    }

    @Override
    public void setState(int state) {
        if (state == STATE_READY) {
            setText("自定义：松开加载更多");
        } else if (state == STATE_REFRESHING) {
            setText("自定义：加载中");
        } else if (state == STATE_NORMAL) {
            setText("自定义：加载更多");
        }  else if (state == STATE_ALL) {
            setText("自定义：没有更多了");
        } else {
            setText("");
        }
    }
}
