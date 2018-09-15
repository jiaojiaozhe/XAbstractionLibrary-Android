package com.framework.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.xframework_parse.xresult.XBaseResult;

/**
 * Created By lanbiao on 2018/5/31
 * 公共业务接口请求结果对象
 */
public class BaseResult extends XBaseResult {

    /**
     * 请求结果错误码
     */
    @JSONField(name="code")
    private int errorCode = -1;

    /**
     * 请求结果描述，一般伴随请求错误时出现
     */
    @JSONField(name="msg")
    private String serverMsg = "";

    private boolean status;
    private float total = -1.0f;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public static BaseResult setFailResult(int errorCode, String errorMsg){
        BaseResult result = new BaseResult();
        result.setErrorCode(errorCode);
        result.setServerMsg(errorMsg);
        return result;
    }

    public static BaseResult loseParamError(){
        return setFailResult(10001,"缺少必要的参数");
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    @Override
    public boolean isSuccess() {
        Long errorCode = Long.valueOf(getErrorCode());
        if(errorCode == 0){
            return true;
        }
        return false;
    }

    @Override
    public String getServerMsg() {
        return serverMsg;
    }
}
