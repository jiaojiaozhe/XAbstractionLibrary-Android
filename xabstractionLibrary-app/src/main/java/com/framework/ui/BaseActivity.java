package com.framework.ui;

import androidx.fragment.app.Fragment;

import com.framework.demo.CustomActionBar;
import com.xframework_uicommon.xview.XBaseActivity;


public abstract class BaseActivity extends XBaseActivity {
    @Override
    public Fragment loadThemeFragment() {
        return new CustomActionBar();
    }
}
