package com.framework.model.result;

import com.framework.model.HomeData;

public class HomeResult extends BaseResult {
    private HomeData data;

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }
}
