package com.framework.http.http2;

import com.alibaba.fastjson.TypeReference;
import com.framework.model.BaseModel;
import com.framework.model.result.TBaseResult;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;

/**
 * Created by lanbiao on 2018/06/15
 * @param <ResultData> 接口请求回调，在这个地方主要实现一些业务相关的回调
 */
public abstract class BaseHttpResponseCallBackBlock<ResultData extends BaseModel> implements IBaseHttpResponseCallBackBlock<ResultData> {

    private TypeReference typeReference;

    protected BaseHttpResponseCallBackBlock(TypeReference type){
        super();
        typeReference = type;
    }

    public void setTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
    }

    public TypeReference getTypeReference() {
        return typeReference;
    }

    //需要解析的数据类型
    private TypeReference<TBaseResult<ResultData>> typeClass;

    protected BaseHttpResponseCallBackBlock(){
        super();
        setTypeClass(new TypeReference<TBaseResult<ResultData>>(){});
    }

    public TypeReference<TBaseResult<ResultData>> getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(TypeReference<TBaseResult<ResultData>> clazzObj) {
        typeClass = clazzObj;
    }

    /**
     * 判断当前请求是否是空数据，因为对于空数据的定义是和业务有较大的关系，所以需要业务工程师实现
     * @param request 当前请求对象
     * @param result 请求解析回调的对象
     * @param resultObj 请求解析前响应的对象，json数据
     * @return 返回是否存在有效数据，如果不愿处理，建议直接返回true
     */
    public abstract Boolean existsData(XIBaseHttpRequestDelegate request, TBaseResult<ResultData> result, Object resultObj);
}
