package com.framework.ui;

import android.support.v4.app.Fragment;

import com.framework.demo.CustomActionBar;
import com.xframework_uicommon.xview.XBaseActivity;
import com.xframework_uicommon.xview.XBaseFragment;


public abstract class BaseActivity extends XBaseActivity {
    @Override
    public Fragment loadThemeFragment() {
        return new CustomActionBar();
    }
}
