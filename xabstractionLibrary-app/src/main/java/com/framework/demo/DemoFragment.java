package com.framework.demo;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.framework.model.result.HomeResult;
import com.framework.parse.BaseHttpResponseCallBackBlock;
import com.framework.ui.BaseFragment;
import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;
import com.personal.xabstractionlibrary.R;

import java.util.HashMap;
import java.util.Map;

public class DemoFragment extends BaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.demo_fragment_1;
    }

    @Override
    protected void onViewCreated(Bundle saveInstanceState, View rootView) {

    }

    @Override
    protected void loadPage() {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("cityId", "1");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", userMap);
        //map.put("version", SystemInfos.getVersionName(getContext()));
        params.put("param", JSON.toJSONString(map));
        params.put("command", "opertationsActivity/getDynamicImageList");

        Map<String, String> header = new HashMap<>();
        header.put("key1", "value1");
        header.put("key1", "value2");

        postRequest(header, params, this, new BaseHttpResponseCallBackBlock<HomeResult>(HomeResult.class) {
            @Override
            public void callBack(XIBaseHttpRequestDelegate request, HomeResult result, Object resultObj) {
                if (result != null) {
                    if (result.isSuccess()) {
                        //AppLog.printD("test", "成功了.");
                    } else {
                        //AppLog.printD("test", "失败了");
                    }
                }
            }

            @Override
            public Boolean existsData(XIBaseHttpRequestDelegate request, HomeResult result, Object resultObj) {
                return false;
            }
        });
    }
}
