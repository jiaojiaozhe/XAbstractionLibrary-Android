package com.framework.parse;

import com.framework.http.IBaseHttpResponseCallBackBlock;
import com.framework.model.result.BaseResult;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;

/**
 * Created by lanbiao on 2018/06/15
 * @param <Result> 接口请求回调，在这个地方主要实现一些业务相关的回调
 */
public abstract class BaseHttpResponseCallBackBlock<Result extends BaseResult> implements IBaseHttpResponseCallBackBlock<Result> {

    /**
     * 需要解析的类型
     */
    private Class<Result> clazz;

    public BaseHttpResponseCallBackBlock(Class<Result> cls){
        super();
        setClazz(cls);
    }

    public Class<Result> getClazz() {
        return clazz;
    }

    public void setClazz(Class<Result> clazz) {
        this.clazz = clazz;
    }

    /**
     * 判断当前请求是否是空数据，因为对于空数据的定义是和业务有较大的关系，所以需要业务工程师实现
     * @param request 当前请求对象
     * @param result 请求解析回调的对象
     * @param resultObj 请求解析前响应的对象，json数据
     * @return 返回是否存在有效数据，如果不愿处理，建议直接返回true
     */
    public abstract Boolean existsData(XIBaseHttpRequestDelegate request, Result result, Object resultObj);
}
