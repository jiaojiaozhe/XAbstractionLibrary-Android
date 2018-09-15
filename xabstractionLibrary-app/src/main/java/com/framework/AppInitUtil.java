package com.framework;

import android.content.Context;

import com.framework.http.NetWorkApp;
import com.framework.parse.ParseApp;
import com.framework.ui.UIApp;
import com.xframework_base.XBaseApp;

/**
 * Created by lanbiao on 2018/07/09
 * 抽象化架构的实现层初始化
 */
public class AppInitUtil extends XBaseApp {

    public AppInitUtil(){
        ParseApp parseApp = new ParseApp();
        NetWorkApp netWorkApp = new NetWorkApp();
        UIApp uiApp = new UIApp();

        addInitNote(netWorkApp).addInitNote(parseApp).addInitNote(uiApp);
    }

    @Override
    public void initApp(Context context) {
        super.initApp(context);

        //ToDo:App应用层初始化
    }
}
