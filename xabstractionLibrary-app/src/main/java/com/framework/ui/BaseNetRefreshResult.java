package com.framework.ui;

import com.framework.model.result.BaseResult;

import java.util.List;


/**
 *  下拉网络请求结果
 */
public abstract class BaseNetRefreshResult<MODEL> extends BaseResult {
    public abstract List<MODEL> getDataList();
}
