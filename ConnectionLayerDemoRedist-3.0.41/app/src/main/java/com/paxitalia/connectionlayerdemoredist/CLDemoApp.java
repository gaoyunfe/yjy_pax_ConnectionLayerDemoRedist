package com.paxitalia.connectionlayerdemoredist;

import android.app.Application;
import android.content.Context;

public class CLDemoApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        CLDemoApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CLDemoApp.context;
    }
}