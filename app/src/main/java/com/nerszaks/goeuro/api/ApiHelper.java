package com.nerszaks.goeuro.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 11.06.2016.
 */
public class ApiHelper {

    private final ApiMethods service;

    public ApiHelper() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .baseUrl("http://api.goeuro.com/api/v2/")
                .build();
        service = retrofit.create(ApiMethods.class);
    }

    public ApiMethods getService() {
        return service;
    }
}
