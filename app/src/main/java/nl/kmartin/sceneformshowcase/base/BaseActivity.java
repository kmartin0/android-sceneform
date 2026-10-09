package nl.kmartin.sceneformshowcase.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import nl.kmartin.sceneformshowcase.R;
import nl.kmartin.sceneformshowcase.util.EdgeToEdgeUtils;

public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected VDB binding;
    protected VM viewModel;

    @Nullable
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        enableEdgeToEdge();
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModel = ViewModelProviders.of(this).get(getVMClass());

        applySystemBarInsets(binding.getRoot());
        initViewModelBinding();
        binding.setLifecycleOwner(this);

        progressBar = findViewById(R.id.progress_circle);
        initProgressBar();
    }

    protected void enableEdgeToEdge() {
        EdgeToEdgeUtils.enable(this);
    }

    private void initProgressBar() {
        if (progressBar != null) {
            progressBar.getIndeterminateDrawable()
                    .setColorFilter(
                            Color.RED,
                            android.graphics.PorterDuff.Mode.MULTIPLY
                    );
        }

        viewModel.isLoading().observe(this, this::showLoading);
    }

    public void showLoading(boolean visibility) {
        if (progressBar != null) {
            progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuLayoutId() != -1) {
            getMenuInflater().inflate(getMenuLayoutId(), menu);
        }
        return true;
    }

    /**
     * Keeps the content clear of the system bars. Override to inset specific views instead.
     *
     * @param root the root view of the activity layout.
     */
    protected void applySystemBarInsets(View root) {
        EdgeToEdgeUtils.applyPadding(root);
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