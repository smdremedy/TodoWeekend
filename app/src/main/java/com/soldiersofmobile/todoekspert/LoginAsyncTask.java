package com.soldiersofmobile.todoekspert;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class LoginAsyncTask extends AsyncTask<String, Integer, String> {

    public static final String USER_ID = "user_id";
    public static final String TOKEN = "token";
    private boolean finished = false;
    private SharedPreferences preferences;

    public LoginAsyncTask(SharedPreferences preferences) {

        this.preferences = preferences;
    }


    public boolean isFinished() {
        return finished;
    }

    interface LoginCallback {
        void showProgress(boolean inProgress);

        void showError(String error);
    }

    private static final LoginCallback DUMMY_CALLBACK = new LoginCallback() {
        @Override
        public void showProgress(boolean inProgress) {
            Log.w("TAG", "showProgree:" + inProgress);
        }

        @Override
        public void showError(String error) {

            Log.w("TAG", "showError:" + error);
        }
    };

    public void setLoginCallback(LoginCallback loginCallback) {
        if (loginCallback == null) {
            this.loginCallback = DUMMY_CALLBACK;
        } else {
            this.loginCallback = loginCallback;
        }
    }

    private LoginCallback loginCallback = DUMMY_CALLBACK;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loginCallback.showProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {

        return validateCredentials(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(String error) {
        super.onPostExecute(error);
        finished = true;
        loginCallback.showProgress(false);
        loginCallback.showError(error);
        loginCallback = null;

    }

    private String validateCredentials(String username, String password) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Converter<ResponseBody, ErrorResponse> converter
                = retrofit.responseBodyConverter(ErrorResponse.class,
                new Annotation[0]);

        TodoApi todoApi = retrofit.create(TodoApi.class);
        Call<User> userCall = todoApi.getLogin(username, password);

        try {
            Response<User> response = userCall.execute();
            if (response.isSuccessful()) {
                User user = response.body();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(USER_ID, user.getObjectId());
                editor.putString(TOKEN, user.getSessionToken());
                editor.apply();


                return null;
            } else {
                ResponseBody errorBody = response.errorBody();
                ErrorResponse errorResponse = converter.convert(errorBody);

                return errorResponse.getError();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }
}
