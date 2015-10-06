package com.jin.dayjob;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jin on 2015-06-06.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }

    public static MyApplication getsInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
