package com.nerszaks.goeuro;

import android.app.Application;

import com.nerszaks.goeuro.api.ApiHelper;
import com.nerszaks.goeuro.api.ApiMethods;

public class GoEuroApp extends Application {

    private static GoEuroApp instance;
    private ApiMethods service;

    public static GoEuroApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ApiHelper apiHelper = new ApiHelper();
        service = apiHelper.getService();
    }

    public ApiMethods getService() {
        return service;
    }
}
