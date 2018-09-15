package com;

import android.app.Application;

public class App extends Application {

    private static Application mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }

    public static Application getApplication(){
        return mAppContext;
    }
}
