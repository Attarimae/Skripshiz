package com.example.skripsi.API;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {
    private SessionManager sm;

    public AuthorizationInterceptor(Context context) {
        sm = new SessionManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        String authToken = sm.fetchAuthToken();
        if(authToken != null) {
            requestBuilder.header("Authorization", authToken);
        }
        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
