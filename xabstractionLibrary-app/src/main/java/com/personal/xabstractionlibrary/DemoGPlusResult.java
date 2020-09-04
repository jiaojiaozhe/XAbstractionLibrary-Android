package com.personal.xabstractionlibrary;

import com.framework.model.result.BaseResult;

public class DemoGPlusResult extends BaseResult {
    private DistanceCountGplusResultData data;

    public DistanceCountGplusResultData getData() {
        return data;
    }

    public void setData(DistanceCountGplusResultData data) {
        this.data = data;
    }
}
