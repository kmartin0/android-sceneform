package com.kmartin0.sceneformexample.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SketchfabApi {

	private static final String baseSketchfabApiUrl = "https://api.sketchfab.com/v3/";

	public static SketchfabApiService create() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseSketchfabApiUrl)
				.client(createOkHttpClient())
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();

		return retrofit.create(SketchfabApiService.class);
	}

	private static OkHttpClient createOkHttpClient() {
		return new OkHttpClient.Builder()
				.addInterceptor(new HttpLoggingInterceptor()
						.setLevel(HttpLoggingInterceptor.Level.BODY))
				.addInterceptor(new AuthorizationInterceptor())
				.build();
	}
}
