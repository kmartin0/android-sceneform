package nl.kmartin.sceneformshowcase.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SketchfabApi {

    private static final String BASE_SKETCHFAB_API_URL = "https://api.sketchfab.com/v3/";

    public static SketchfabApiService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SKETCHFAB_API_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(SketchfabApiService.class);
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(
                        new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .addInterceptor(new AuthorizationInterceptor())
                .build();
    }
}