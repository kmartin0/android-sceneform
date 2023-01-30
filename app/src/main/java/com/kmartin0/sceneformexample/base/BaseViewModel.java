package com.kmartin0.sceneformexample.base;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

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
