package com.soldiersofmobile.todoekspert;

import android.content.SharedPreferences;
import android.os.AsyncTask;

public class LoginManager {

    private SharedPreferences preferences;

    public LoginManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    private LoginAsyncTask loginAsyncTask;
    private LoginAsyncTask.LoginCallback loginCallback;

    public void login(String username, String password) {

        if (loginAsyncTask == null || loginAsyncTask.isFinished()) {
            loginAsyncTask = new LoginAsyncTask(preferences);
            loginAsyncTask.setLoginCallback(loginCallback);

            loginAsyncTask.execute(username, password);
        }
    }

    public boolean hasToLogin() {
        return preferences.getString(LoginAsyncTask.TOKEN, "").isEmpty();
    }

    public void logout() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(LoginAsyncTask.TOKEN);
        edit.remove(LoginAsyncTask.USER_ID);
        edit.apply();
    }

    public void setLoginCallback(LoginAsyncTask.LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        if (loginAsyncTask != null) {
            loginAsyncTask.setLoginCallback(loginCallback);
        }
    }
}
