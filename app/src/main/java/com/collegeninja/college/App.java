package com.collegeninja.college;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.collegeninja.college.utils.AppConstants;

public class App extends MultiDexApplication implements AppConstants {
    private static Context sContext;
    private static App sAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sAppInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getsContext() {
        return sContext;
    }

    public static App getsAppInstance() {
        return sAppInstance;
    }
}
