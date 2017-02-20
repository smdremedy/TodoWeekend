package com.soldiersofmobile.todoekspert;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private LoginManager loginManager;
    private TodoApi todoApi;
    private Converter<ResponseBody, ErrorResponse> converter;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(this);


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

        converter = retrofit.responseBodyConverter(ErrorResponse.class,
            new Annotation[0]);

        todoApi = retrofit.create(TodoApi.class);

        loginManager = new LoginManager(preferences, todoApi, converter);
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public TodoApi getTodoApi() {
        return todoApi;
    }

    public Converter<ResponseBody, ErrorResponse> getConverter() {
        return converter;
    }
}
