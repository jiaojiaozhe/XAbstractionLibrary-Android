package com.framework.demo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.framework.model.OpertationsModel;
import com.framework.ui.BaseNetRefreshFragment;
import com.xframework_base.xutils.XAppInfo;
import com.zhht.xabstractionlibrary.R;

import java.util.HashMap;
import java.util.Map;

public class DemoNetRefreshFragment extends BaseNetRefreshFragment<OpertationsModel,DemoHomeResult> {
    @Override
    public XBaseRefreshViewHolder createViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(getContext(), R.layout.demo_netrefresh_item_layout,null);
        return new Demo_NetRefresh_Fragment_Holder(view);
    }

    @Override
    public void refreshRequestParams(HashMap requestParams) {
        HashMap maps = new HashMap();
        maps.put("cityId", "1");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", maps);
        map.put("version", XAppInfo.getAppVersion());
        requestParams.put("param", JSON.toJSONString(map));
        requestParams.put("command", "opertationsActivity/getDynamicImageList");
    }

    @Override
    public void loadMoreRequestParams(HashMap requestParams) {
        HashMap maps = new HashMap();
        maps.put("cityId", "1");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", maps);
        map.put("version", XAppInfo.getAppVersion());
        requestParams.put("param", JSON.toJSONString(map));
        requestParams.put("command", "opertationsActivity/getDynamicImageList");
    }


    public class Demo_NetRefresh_Fragment_Holder extends XBaseRefreshViewHolder<OpertationsModel>{
        private TextView test_nonet_list_item_id;

        public Demo_NetRefresh_Fragment_Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(OpertationsModel data) {
            if(data != null)
                test_nonet_list_item_id.setText(data.getCityName());
        }
    }
}
