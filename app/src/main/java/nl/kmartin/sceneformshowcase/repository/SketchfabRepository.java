package nl.kmartin.sceneformshowcase.repository;

import android.content.Context;

import nl.kmartin.sceneformshowcase.api.SketchfabApi;
import nl.kmartin.sceneformshowcase.api.SketchfabApiService;
import nl.kmartin.sceneformshowcase.api.SketchfabModelDownloadResponse;
import nl.kmartin.sceneformshowcase.api.SketchfabModelResponse;

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
