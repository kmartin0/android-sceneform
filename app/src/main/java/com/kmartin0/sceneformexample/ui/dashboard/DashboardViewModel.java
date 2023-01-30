package com.kmartin0.sceneformexample.ui.dashboard;


import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.kmartin0.sceneformexample.api.SketchfabModelDownloadResponse;
import com.kmartin0.sceneformexample.api.SketchfabModelResponse;
import com.kmartin0.sceneformexample.base.BaseViewModel;
import com.kmartin0.sceneformexample.model.SketchfabModel;
import com.kmartin0.sceneformexample.repository.SketchfabRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DashboardViewModel extends BaseViewModel {

    private SketchfabRepository sketchfabRepository;
    private MutableLiveData<List<SketchfabModel>> sketchfabModels = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    private MutableLiveData<String> sketchfabDownloadUrl = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        sketchfabRepository = new SketchfabRepository(application.getApplicationContext());
        sketchfabModels.setValue(new ArrayList<>());
    }

    public void fetchSketchfabModels(String assetID) {

        sketchfabRepository.getSketchfabModel(assetID).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<SketchfabModelResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        isLoading.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull SketchfabModelResponse sketchfabModel) {
                        isLoading.setValue(false);
                        Objects.requireNonNull(sketchfabModels.getValue()).add(new SketchfabModel(sketchfabModel));
                        sketchfabModels.setValue(sketchfabModels.getValue());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
						isLoading.setValue(false);
						error.setValue(e.getMessage());
						e.printStackTrace();
                    }
                });
    }

    public void fetchSketchfabDownloadUrl(String assetID) {
        this.sketchfabDownloadUrl.setValue(null);

        sketchfabRepository.getSketchfabModelDownloadInformation(assetID).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<SketchfabModelDownloadResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        isLoading.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull SketchfabModelDownloadResponse sketchfabModel) {
                        isLoading.setValue(false);
                        sketchfabDownloadUrl.setValue(sketchfabModel.getGlb().getUrl());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                        error.setValue(e.getMessage());
                        e.printStackTrace();
                    }
                });

    }

    public MutableLiveData<List<SketchfabModel>> fetchSketchfabModels() {
        return sketchfabModels;
    }

    public MutableLiveData<String> getSketchfabDownloadUrl() {
        return sketchfabDownloadUrl;
    }

    public MutableLiveData<String> getError() {
        return error;
    }
}
