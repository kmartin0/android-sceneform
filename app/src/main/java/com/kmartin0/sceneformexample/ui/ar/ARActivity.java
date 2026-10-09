package com.kmartin0.sceneformexample.ui.ar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.kmartin0.sceneformexample.R;
import com.kmartin0.sceneformexample.databinding.ActivityArBinding;
import com.kmartin0.sceneformexample.util.CaptureSceneHelper;
import com.kmartin0.sceneformexample.util.Constants;

import java.io.File;
import java.util.Objects;

public class ARActivity extends SingleModelARActivity<ActivityArBinding, ARViewModel>
		implements CaptureSceneHelper.CaptureSceneHelperCallbacks {

	private Uri modelURI;
	private CaptureSceneHelper captureSceneHelper;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		modelURI = (Uri) Objects.requireNonNull(getIntent().getExtras())
				.get(Constants.AR_MODEL_URI);

		captureSceneHelper = new CaptureSceneHelper(
				this,
				getArFragment().getArSceneView(),
				this
		);

		initListeners();
	}

	private void initListeners() {
		binding.btnAdd.setOnClickListener(v -> addObjectToPlane());
		binding.btnRemove.setOnClickListener(v -> removeObjectFromPlane());
		binding.btnBack.setOnClickListener(v -> finishActivity());
		binding.btnCapture.setOnClickListener(v -> captureScene());

		setHoldListener(
				binding.btnRotateCounterClockwise,
				() -> getNode().startRotateCounterClockwise(),
				() -> getNode().stopRotateCounterClockwise()
		);

		setHoldListener(
				binding.btnRotateClockwise,
				() -> getNode().startRotateClockwise(),
				() -> getNode().stopRotateClockwise()
		);

		setHoldListener(
				binding.btnRotateUpward,
				() -> getNode().startRotateUpward(),
				() -> getNode().stopRotateUpward()
		);

		setHoldListener(
				binding.btnRotateDownward,
				() -> getNode().startRotateDownward(),
				() -> getNode().stopRotateDownward()
		);

		setHoldListener(
				binding.btnEnlarge,
				() -> getNode().startEnlarge(),
				() -> getNode().stopEnlarge()
		);

		setHoldListener(
				binding.btnShrink,
				() -> getNode().startShrink(),
				() -> getNode().stopShrink()
		);

		setHoldListener(
				binding.btnMoveUp,
				() -> getNode().startMoveUp(),
				() -> getNode().stopMoveUp()
		);

		setHoldListener(
				binding.btnMoveDown,
				() -> getNode().startMoveDown(),
				() -> getNode().stopMoveDown()
		);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void setHoldListener(View view, Runnable startAction, Runnable stopAction) {
		view.setOnTouchListener((v, event) -> {
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					if (!isAnchorSet()) {
						return false;
					}

					startAction.run();
					return true;

				case MotionEvent.ACTION_UP:
					if (isAnchorSet()) {
						stopAction.run();
					}

					v.performClick();
					return true;

				case MotionEvent.ACTION_CANCEL:
					if (isAnchorSet()) {
						stopAction.run();
					}

					return true;

				default:
					return false;
			}
		});
	}

	/**
	 * Adds a model to the anchor if no object is on the plane.
	 */
	public void addObjectToPlane() {
		addObject(modelURI);
	}

	/**
	 * Removes the anchorNode from the scene.
	 */
	public void removeObjectFromPlane() {
		removeObject();
	}

	/**
	 * Finish activity.
	 */
	public void finishActivity() {
		finish();
	}

	@Override
	protected void onModelAddedToScene() {
		binding.btnAdd.setVisibility(View.INVISIBLE);
		binding.btnRemove.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onModelRemovedToScene() {
		binding.btnAdd.setVisibility(View.VISIBLE);
		binding.btnRemove.setVisibility(View.INVISIBLE);
	}

	/**
	 * Create a snapshot of the current AR surface view.
	 */
	public void captureScene() {
		captureSceneHelper.captureScene(this);
	}

	/**
	 * Display a Snackbar containing a success message and a button which redirects
	 * the user to the created snapshot.
	 *
	 * @param image File of the created snapshot.
	 */
	@Override
	public void onCaptureSuccess(File image) {
		Snackbar snackbar = Snackbar.make(
				findViewById(android.R.id.content),
				getString(R.string.photo_saved),
				Snackbar.LENGTH_LONG
		);

		snackbar.setAction(
				getString(R.string.open_photo),
				v -> captureSceneHelper.openSavedImage(this, image)
		);

		snackbar.show();

		showLoading(false);
		getArFragment().getArSceneView().getPlaneRenderer().setVisible(true);
	}

	/**
	 * Display the message of the failed capture and set the ArSceneView
	 * back to its normal state.
	 *
	 * @param message String message to be displayed.
	 */
	@Override
	public void onCaptureFail(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		getArFragment().getArSceneView().getPlaneRenderer().setVisible(false);
		showLoading(false);
	}

	/**
	 * Show loading circle when starting the snapshot.
	 */
	@Override
	public void onCaptureStart() {
		showLoading(true);
	}

	@Override
	protected Integer getLayoutId() {
		return R.layout.activity_ar;
	}

	@Override
	protected void initViewModelBinding() {
		binding.setViewModel(viewModel);
	}

	@Override
	protected Class<ARViewModel> getVMClass() {
		return ARViewModel.class;
	}
}