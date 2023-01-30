package com.kmartin0.sceneformexample.repository;

import android.content.Context;

import com.kmartin0.sceneformexample.api.SketchfabApi;
import com.kmartin0.sceneformexample.api.SketchfabApiService;
import com.kmartin0.sceneformexample.api.SketchfabModelDownloadResponse;
import com.kmartin0.sceneformexample.api.SketchfabModelResponse;

import io.reactivex.Single;

public class SketchfabRepository {

	private SketchfabApiService sketchfabApi;

	public SketchfabRepository(Context context) {
		sketchfabApi = SketchfabApi.create();
	}

	public Single<SketchfabModelResponse> getSketchfabModel(String id) {
		return sketchfabApi.getSketchfabModel(id);
	}

	public Single<SketchfabModelDownloadResponse> getSketchfabModelDownloadInformation(String id) {
		return sketchfabApi.getSketchfabModelDownloadInformation(id);
	}
}
