package com.soldiersofmobile.todoekspert;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class LoginManager {

    private SharedPreferences preferences;
    private final TodoApi todoApi;
    private final Converter<ResponseBody, ErrorResponse> converter;

    public LoginManager(SharedPreferences preferences, TodoApi todoApi, Converter<ResponseBody, ErrorResponse> converter) {
        this.preferences = preferences;
        this.todoApi = todoApi;
        this.converter = converter;
    }

    private LoginAsyncTask loginAsyncTask;
    private LoginAsyncTask.LoginCallback loginCallback;

    public void login(String username, String password) {

        if (loginAsyncTask == null || loginAsyncTask.isFinished()) {
            loginAsyncTask = new LoginAsyncTask(preferences, todoApi, converter);
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

    public String getToken() {
        return preferences.getString(LoginAsyncTask.TOKEN, "");
    }
}
