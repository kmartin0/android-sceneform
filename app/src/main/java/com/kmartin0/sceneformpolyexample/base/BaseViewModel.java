package com.kmartin0.sceneformpolyexample.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public class BaseViewModel extends AndroidViewModel {
	protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

	public BaseViewModel(@NonNull Application application) {
		super(application);
	}

	protected void startLoading() {
		isLoading.setValue(true);
	}

	protected void stopLoading() {
		isLoading.setValue(false);
	}

	public MutableLiveData<Boolean> isLoading() {
		return isLoading;
	}
}
