package com.framework.demo;

import com.framework.model.HomeData;
import com.framework.ui.BaseNetRefreshResult;

import java.util.List;

public class DemoHomeResult<MODEL> extends BaseNetRefreshResult {
    private HomeData data;

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }

    @Override
    public List<MODEL> getDataList() {
        return (List<MODEL>) data.getOpertationVO();
    }
}
