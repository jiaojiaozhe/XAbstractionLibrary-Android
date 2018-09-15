package com.xframework_base;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lanbiao on 2018/07/09
 * 基础App的jar，主要包含常用的依赖控制，常用的工具、一些配置等等
 */
public class XBaseApp extends Application {

    /**
     * 责任链节点对象
     */
    private XBaseApp nextApp;

    /*确保在任何地方任何时候能获取到当前的Application*/
    public static Application getApplication(){
        Application application = null;
        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method2 = activityThreadClass.getMethod(
                    "currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            Object localObject = method2.invoke(null, (Object[]) null);

            final Method method = activityThreadClass
                    .getMethod("getApplication");
            application = (Application) method.invoke(localObject, (Object[]) null);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApp(getApplicationContext());
    }

    /**
     *
     * @param context
     */
    public void initApp(Context context){
        if(nextApp != null){
            nextApp.initApp(context);
        }
        //ToDo:基础jar中需要被初始化，预留

    }

    /**
     * 添加初始化某块节点
     * @param app 待初始化的模块
     */
    public XBaseApp addInitNote(XBaseApp app){
        if(app == null)
            return this;
        if(!(app instanceof XBaseApp))
            return this;

        XBaseApp next = nextApp;
        if(next == null){
            nextApp = app;
        }else {
            while (next.nextApp != null){
                next = nextApp.nextApp;
            }
            next.nextApp = app;
        }
        return app;
    }
}
