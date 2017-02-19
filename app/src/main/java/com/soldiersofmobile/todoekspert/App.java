package com.soldiersofmobile.todoekspert;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

public class App extends Application {

    private LoginManager loginManager;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        loginManager = new LoginManager(preferences);
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }
}
