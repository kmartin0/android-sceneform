package com.kmartin0.sceneformpolyexample.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolyApi {
	private static final String basePollyUrl = "https://poly.googleapis.com/v1/";


	public static PolyApiService create() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(basePollyUrl)
				.client(createOkHttpClient())
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();

		return retrofit.create(PolyApiService.class);
	}

	private static OkHttpClient createOkHttpClient() {
		return new OkHttpClient.Builder()
				.addInterceptor(new HttpLoggingInterceptor()
						.setLevel(HttpLoggingInterceptor.Level.BODY))
				.build();
	}
}
