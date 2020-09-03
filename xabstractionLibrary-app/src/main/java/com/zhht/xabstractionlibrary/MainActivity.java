package com.zhht.xabstractionlibrary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.framework.http.HttpRequestManager;
//import com.framework.http.IBaseHttpResponseCallBackBlock;
//import com.framework.http.request.TBaseRequest;
//import com.framework.model.BaseModel;
//import com.framework.model.HomeData;
//import com.framework.model.result.BaseResult;
//import com.framework.model.result.HomeResult;
//import com.framework.model.result.TBaseResult;
//import com.framework.parse.BaseJSON;
//import com.xframework_network.xhttp.XIBaseHttpRequestDelegate;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DistanceCountGplusDataModel distanceCountGplusDataModel = new DistanceCountGplusDataModel("116.316218","39.977609","");
//        TBaseRequest baseRequest = new TBaseRequest("/api/v1/park/queryByDistanceCountGplus",distanceCountGplusDataModel,null);
//        HttpRequestManager.getInstance().postRequest(baseRequest, null, new IBaseHttpResponseCallBackBlock<DemoGPlusResult>() {
//            @Override
//            public void callBack(XIBaseHttpRequestDelegate request, DemoGPlusResult result, Object resultObj) {
//                if(result != null){
//                    if(result.isSuccess()){
//
//                    }
//                }
//            }
//        });

//        DistanceCountGplusDataModel distanceCountGplusDataModel = new DistanceCountGplusDataModel("116.316218","39.977609","");
//        TBaseRequest baseRequest = new TBaseRequest("/api/v1/park/queryByDistanceCountGplus",distanceCountGplusDataModel,null);
//        HttpRequestManager.getInstance().postRequest(baseRequest, null, new BaseHttpResponseCallBackBlock<TBaseResult>(new TypeReference<TBaseResult<DistanceCountGplusDataModel>>(){}) {
//
//            @Override
//            public void callBack(XIBaseHttpRequestDelegate request, TBaseResult result, Object resultObj) {
//                if(result != null){
//                    if(result.isSuccess()){
//
//                    }
//                }
//            }
//
//            @Override
//            public Boolean existsData(XIBaseHttpRequestDelegate request, TBaseResult result, Object resultObj) {
//                return null;
//            }
//        });

//        AssetManager assetManager = getApplicationContext().getAssets();
//        InputStreamReader isr = null;
//        try {
//            InputStream inputStream = assetManager.open("parseData2.json");
//            isr = new InputStreamReader(inputStream,"UTF-8");
//            BufferedReader br = new BufferedReader(isr);
//            char json[] = new char[4 * 1024];
//            br.read(json,0,4 * 1024);
//            String jsonString = new String(json);
//
//            TBaseResult homeResult = BaseJSON.getBaseInstence().parseObject(jsonString,new TypeReference<TBaseResult<HomeData>>(){});
//            BaseJSON.getBaseInstence().parseObject(jsonString,new TBaseResult2<TBaseResult<HomeData>>(){});
////            HomeResult homeResult = BaseJSON.getBaseInstence().parseObject(jsonString,HomeResult.class);
//
//            br.close();
//            isr.close();
//            inputStream.close();
//            Log.d("test", "onCreate: " + br.toString());
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
    }
}
