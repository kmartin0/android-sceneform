package com.kmartin0.sceneformexample.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kmartin0.sceneformexample.R;
import com.kmartin0.sceneformexample.base.BaseActivity;
import com.kmartin0.sceneformexample.databinding.ActivityDashboardBinding;
import com.kmartin0.sceneformexample.model.SketchfabModel;
import com.kmartin0.sceneformexample.ui.ar.ARActivity;
import com.kmartin0.sceneformexample.ui.barcode.BarcodeActivity;
import com.kmartin0.sceneformexample.util.Constants;
import com.kmartin0.sceneformexample.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity
        extends BaseActivity<ActivityDashboardBinding, DashboardViewModel>
        implements SketchfabModelsAdapter.ItemClickListener {

    private SketchfabModelsAdapter modelAdapter;
    private final List<SketchfabModel> sketchfabModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAssetListView();
        initObservers();
    }

    private void initObservers() {
        viewModel.fetchSketchfabModels().observe(this, sketchfabResponse -> {
            if (sketchfabResponse == null) {
                return;
            }

            sketchfabModels.clear();
            sketchfabModels.addAll(sketchfabResponse);
            modelAdapter.notifyDataSetChanged();
        });

        viewModel.getError().observe(this,
                error -> DialogUtils.showToast(DashboardActivity.this, error));
    }

    private void initAssetListView() {
        modelAdapter = new SketchfabModelsAdapter(sketchfabModels, this);

        binding.modelRecyclerView.setAdapter(modelAdapter);
        binding.modelRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );
        binding.modelRecyclerView.setHasFixedSize(true);

        for (String id : Constants.SKETCHFAB_ASSETS_UIDS) {
            viewModel.fetchSketchfabModels(id);
        }
    }

    private void startArActivity(String assetUri) {
        Intent intent = new Intent(this, ARActivity.class);
        intent.putExtra(Constants.AR_MODEL_URI, Uri.parse(assetUri));
        startActivity(intent);
    }

    /**
     * Pads the root by the top inset and paints it with the status bar color, while the list
     * pads only the bottom so items scroll beneath the navigation bar.
     *
     * @param root the root view of the activity layout.
     */
    @Override
    protected void applySystemBarInsets(View root) {
        root.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        binding.modelRecyclerView.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.white));

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout()
            );
            view.setPadding(bars.left, bars.top, bars.right, 0);
            binding.modelRecyclerView.setPadding(0, 0, 0, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_qr_scanner) {
            new IntentIntegrator(this)
                    .setCaptureActivity(BarcodeActivity.class)
                    .initiateScan();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                DialogUtils.showToast(this, "Scan Cancelled");
            } else {
                startArActivity(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initViewModelBinding() {
        binding.setViewModel(viewModel);
    }

    @Override
    protected Class<DashboardViewModel> getVMClass() {
        return DashboardViewModel.class;
    }

    @Override
    public void onItemClick(SketchfabModel sketchfabModel) {
        viewModel.fetchSketchfabDownloadUrl(sketchfabModel.getUid());

        viewModel.getSketchfabDownloadUrl().observe(this, sketchfabDownloadUrl -> {
            if (sketchfabDownloadUrl != null) {
                startArActivity(sketchfabDownloadUrl);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}