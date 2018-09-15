package com.zhht.xabstractionlibrary;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.framework.model.BaseModel;
import com.framework.model.HomeData;
import com.framework.model.result.HomeResult;
import com.framework.model.result.TBaseResult;
import com.framework.parse.BaseJSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = getApplicationContext().getAssets();
        InputStreamReader isr = null;
        try {
            InputStream inputStream = assetManager.open("parseData2.json");
            isr = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            char json[] = new char[4 * 1024];
            br.read(json,0,4 * 1024);
            String jsonString = new String(json);

            TBaseResult homeResult = BaseJSON.getBaseInstence().parseObject(jsonString,new TypeReference<TBaseResult<HomeData>>(){});

//            HomeResult homeResult = BaseJSON.getBaseInstence().parseObject(jsonString,HomeResult.class);

            br.close();
            isr.close();
            inputStream.close();
            Log.d("test", "onCreate: " + br.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
