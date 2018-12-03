package com.framework.http.request;

import com.framework.model.BaseModel;

public class TBaseRequest extends BaseRequest {

    /*接口请求token*/
    private String k;

    /*App版本号*/
    private String version;

    /*接口请求逻辑对象*/
    private BaseModel data;

    public TBaseRequest(String command,BaseModel data, BaseRequestHeader requestHeader) {
        super(command, requestHeader);
        this.k = "d2707ef7-9774-4e28-b624-d1e7b99dcbbb";
        this.version = "1.5";
        this.data = data;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BaseModel getData() {
        return data;
    }

    public void setData(BaseModel data) {
        this.data = data;
    }
}
