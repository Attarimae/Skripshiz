package com.example.skripsi.API;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private APIInterface apiInterface;

    private final String BASE_URL = "https://65db-2001-448a-20b0-f7a4-7949-a780-733f-5c7c.ngrok-free.app";

    public APIInterface getApiService(Context context) {
        // Initialize ApiService if not initialized yet
        if (apiInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context))
                    .build();
            apiInterface = retrofit.create(APIInterface.class);
        }

        return apiInterface;
    }

    private OkHttpClient okhttpClient(Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthorizationInterceptor(context))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }
}
