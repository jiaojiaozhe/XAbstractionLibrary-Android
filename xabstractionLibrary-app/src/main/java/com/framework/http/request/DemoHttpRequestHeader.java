package com.framework.http.request;

/**
 * demo接口请求头
 */
public class DemoHttpRequestHeader extends BaseRequestHeader {
    private String hKey1;
    private Float hKey2;

    public DemoHttpRequestHeader(String hKey,Float hKey2){
        super();
        sethKey1(hKey);
        sethKey2(hKey2);
    }

    public String gethKey1() {
        return hKey1;
    }

    public void sethKey1(String hKey1) {
        this.hKey1 = hKey1;
    }

    public Float gethKey2() {
        return hKey2;
    }

    public void sethKey2(Float hKey2) {
        this.hKey2 = hKey2;
    }
}
