package com.collegeninja.college;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.collegeninja.college.activity.ProfileActivity;
import com.collegeninja.college.utils.AppConstants;
import com.fdscollege.college.R;

public class App extends MultiDexApplication implements AppConstants {
    private static Context sContext;
    private static App sAppInstance;
    private static Dialog showMessageAlert;

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
        showMessageAlert = new Dialog(this);
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

    public static void showMessageAlert(String message) {

        showMessageAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showMessageAlert.setCancelable(false);
        showMessageAlert.setContentView(R.layout.dialog_message);

        Button btnSettings = (Button) showMessageAlert.findViewById(R.id.yndialog);
        btnSettings.setText(R.string.ok);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageAlert.dismiss();
            }
        });

        showMessageAlert.show();

    }
}
