package com.kmartin0.sceneformpolyexample.base;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ProgressBar;

import com.kmartin0.sceneformpolyexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

	protected VDB binding;
	protected VM viewModel;

	@BindView(R.id.progress_circle)
	@Nullable
	ProgressBar progressBar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, getLayoutId());
		viewModel = ViewModelProviders.of(this).get(getVMClass());
		initViewModelBinding();
		binding.setLifecycleOwner(this);
		ButterKnife.bind(this);
		initProgressBar();
	}

	private void initProgressBar() {
		if (progressBar != null) {
			progressBar.getIndeterminateDrawable()
					.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
		}

//		viewModel.isLoading().observe(this, this::showLoading);
	}

	public void showLoading(boolean visibility) {

//		if (progressBar != null) progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
//		if (progressBar != null) Log.i("TAGZ", "Progress Visibility: " + progressBar.getVisibility());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getMenuLayoutId() != -1) getMenuInflater().inflate(getMenuLayoutId(), menu);
		return true;
	}

	/**
	 * @return Integer the res id of the activity layout.
	 */
	@LayoutRes
	protected abstract Integer getLayoutId();

	/**
	 * Set the viewModel in the ActivityBinding.
	 */
	protected abstract void initViewModelBinding();

	/**
	 * @return Class of the ViewModel.
	 */
	protected abstract Class<VM> getVMClass();

	/**
	 * Override this method with a valid menu id or -1.
	 *
	 * @return the menu resource id to be used.
	 */
	@MenuRes
	protected int getMenuLayoutId() {
		return R.menu.menu_main;
	}

}
