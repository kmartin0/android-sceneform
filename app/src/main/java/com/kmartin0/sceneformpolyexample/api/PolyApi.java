package com.kmartin0.sceneformpolyexample.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolyApi {
    private static final String basePollyUrl = "https://poly.googleapis.com/v1/";
    static Context context;

    public static PolyApiService create(Context context) {
        PolyApi.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(basePollyUrl)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(PolyApiService.class);
    }

    private static OkHttpClient createOkHttpClient() {
        int cacheSize = 4 * 1024 * 1024;
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (isNetworkAvailable()) {
                        request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                    } else {
                        request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                    }
                    return chain.proceed(request);
                })
                .build();
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
