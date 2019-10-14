package com.kmartin0.sceneformpolyexample.repository;

import android.content.Context;

import com.kmartin0.sceneformpolyexample.api.PolyApi;
import com.kmartin0.sceneformpolyexample.api.PolyApiService;
import com.kmartin0.sceneformpolyexample.api.PolyResponse;

import io.reactivex.Single;

public class PolyRepository {

	private PolyApiService polyApi;

	public PolyRepository(Context context) {
		polyApi = PolyApi.create();
	}

	public Single<PolyResponse> getPolyAsset(String id) {
		return polyApi.getPolyAssetById(id);
	}
}
