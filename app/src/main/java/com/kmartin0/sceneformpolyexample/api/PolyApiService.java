package com.kmartin0.sceneformpolyexample.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.kmartin0.sceneformpolyexample.util.Constants.POLY_API_KEY;


public interface PolyApiService {

	@GET("https://poly.googleapis.com/v1/assets/{assetId}?key=" + POLY_API_KEY)
	Single<PolyResponse> getPolyAssetById(@Path("assetId") String assetId);
}
