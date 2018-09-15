package com.framework.model.result;

import com.framework.model.BaseModel;

public class TBaseResult<T extends BaseModel> extends BaseResult {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
