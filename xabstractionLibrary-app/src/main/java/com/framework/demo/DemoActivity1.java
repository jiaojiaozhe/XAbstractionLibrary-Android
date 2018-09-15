package com.framework.demo;

import com.framework.ui.BaseActivity;
import com.xframework_uicommon.xview.XBaseFragment;

public class DemoActivity1 extends BaseActivity {
    @Override
    public XBaseFragment loadContentFragment() {
        return new DemoFragment();
    }
}
