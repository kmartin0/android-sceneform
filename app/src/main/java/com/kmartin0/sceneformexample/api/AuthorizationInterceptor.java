package com.kmartin0.sceneformexample.api;

import androidx.annotation.NonNull;

import com.kmartin0.sceneformexample.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Token " + Constants.SKETCHFAB_API_KEY)
                .build();

        return chain.proceed(request);
    }
}
