package com.kmartin0.sceneformpolyexample.ui.dashboard;


import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kmartin0.sceneformpolyexample.api.PolyResponse;
import com.kmartin0.sceneformpolyexample.base.BaseViewModel;
import com.kmartin0.sceneformpolyexample.repository.PolyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DashboardViewModel extends BaseViewModel {

	private PolyRepository polyRepository;
	private MutableLiveData<List<PolyResponse>> pollyAssets = new MutableLiveData<>();
	private MutableLiveData<String> error = new MutableLiveData<>();

	public DashboardViewModel(@NonNull Application application) {
		super(application);
		polyRepository = new PolyRepository(application.getApplicationContext());
		pollyAssets.setValue(new ArrayList<>());
	}

	public void getPolyAsset(String assetID) {
		polyRepository.getPolyAsset(assetID)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new SingleObserver<PolyResponse>() {
					@Override
					public void onSubscribe(Disposable d) {
						isLoading.setValue(true);
					}

					@Override
					public void onSuccess(PolyResponse pollyResponse) {
//						isLoading.setValue(false);
						Objects.requireNonNull(pollyAssets.getValue()).add(pollyResponse);
						pollyAssets.setValue(pollyAssets.getValue());
					}

					@Override
					public void onError(Throwable e) {
//						isLoading.setValue(false);
						error.setValue(e.getMessage());
						e.printStackTrace();
					}
				});
	}

	public MutableLiveData<List<PolyResponse>> getPollyAssets() {
		return pollyAssets;
	}

	public MutableLiveData<String> getError() {
		return error;
	}
}
