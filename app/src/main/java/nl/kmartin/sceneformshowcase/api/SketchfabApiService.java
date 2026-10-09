package nl.kmartin.sceneformshowcase.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface SketchfabApiService {

	@GET("models/{uid}")
	Single<SketchfabModelResponse> getSketchfabModel(@Path("uid") String uid);

	@GET("models/{uid}/download")
	Single<SketchfabModelDownloadResponse> getSketchfabModelDownloadInformation(@Path("uid") String uid);
}
