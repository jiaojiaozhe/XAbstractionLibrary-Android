package com.framework.http.request;

import com.framework.model.BaseModel;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 基础请求对象
 */
public class BaseRequest extends BaseModel {

    /**
     * 接口请求名
     */
    private String command;

    /**
     * 请求header
     */
    private BaseRequestHeader requestHeader;

    private BaseRequest(){
        this(null,null);
    }

    public BaseRequest(String command,BaseRequestHeader requestHeader){
        super();
        setCommand(command);
        setRequestHeader(requestHeader);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public BaseRequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(BaseRequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    private void getParams(Class cls,HashMap<String,Object> params){
        if(cls == null || cls.equals(Object.class) || cls.equals(BaseModel.class)){
            return;
        }

        getParams(cls.getSuperclass(),params);

        Field[] t = cls.getDeclaredFields();
        for (Field field : t) {
            field.setAccessible(true);
            String name = field.getName();
            if(!name.equalsIgnoreCase("requestHeader")) {
                try {
                    Object value = field.get(this);
                    params.put(name, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String,Object> getRequestParams(){
        HashMap<String,Object> requestParams = new HashMap<>();
        getParams(getClass(),requestParams);
        return requestParams;
    }

    private void getHeaders(Class cls,HashMap<String,String> params){
        if(cls == null || cls.equals(Object.class) || cls.equals(BaseModel.class)){
            return;
        }

        getHeaders(cls.getSuperclass(),params);

        Field[] t = cls.getDeclaredFields();
        for (Field field : t) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object value = field.get(this.getRequestHeader());
                params.put(name, value.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String,String> getRequestHeaders(){
        HashMap<String,String> requestHeaders = new HashMap<>();
        if(getRequestHeader() != null){
            BaseRequestHeader header = requestHeader;
            getHeaders(header.getClass(),requestHeaders);
        }
        return requestHeaders;
    }
}
