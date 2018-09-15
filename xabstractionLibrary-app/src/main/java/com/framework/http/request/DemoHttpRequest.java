package com.framework.http.request;

/**
 * 请求对象
 */
public class DemoHttpRequest extends BaseRequest {
    private String key1;
    private int key2;

    private DemoHttpRequest(){
        this(null,null,0);
    }

    public DemoHttpRequest(String command,String k1,int k2){
        this(command,k1,k2,null);
    }

    public DemoHttpRequest(String command,String k1,int k2,BaseRequestHeader requestHeader){
        super(command,requestHeader);
        setKey1(k1);
        setKey2(k2);
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public int getKey2() {
        return key2;
    }

    public void setKey2(int key2) {
        this.key2 = key2;
    }
}
