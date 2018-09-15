package com.xframework_parse.xresult;

import com.xframework_base.xmodel.XBaseModel;

/**
 * Created by lanbiao on 2018/5/31
 * 接口请求结果对象
 */
public abstract class XBaseResult extends XBaseModel {

    /**
     * 判断是否成功
     * @return true成功 false失败
     */
    public abstract boolean isSuccess();

    /**
     * 获取请求提示信息
     * @return 一般在失败的情况下，会有提示信息
     */
    public abstract String getServerMsg();

}
