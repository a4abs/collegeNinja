package com.collegeninja.college;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

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

    public static void clearPreference() {
        try {
            SharedPreferences sharedPreferences = sContext.getSharedPreferences("com.collegeninja.college", 0);
            sharedPreferences.edit().clear().apply();
        } catch (Exception e) {
        }
    }

    public static void writeUserPrefs(String key, String value) {
        try {
            SharedPreferences settings;
            settings = sContext.getSharedPreferences("com.collegeninja.college", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            //App.showToast(sContext, "writeUserPrefs", "" + e);
        }
    }

    public static String readUserPrefs(String key) {
        String value = "";
        try {
            SharedPreferences settings;
            settings = sContext.getSharedPreferences("com.collegeninja.college", Context.MODE_PRIVATE);
            value = settings.getString(key, "");

        } catch (Exception e) {
            //App.showToast(sContext, "readUserPrefs", "" + e);
        }
        return value;
    }
}
