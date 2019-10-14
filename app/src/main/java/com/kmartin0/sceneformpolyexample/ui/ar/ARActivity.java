package com.kmartin0.sceneformpolyexample.ui.ar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.kmartin0.sceneformpolyexample.R;
import com.kmartin0.sceneformpolyexample.base.SingleModelARActivity;
import com.kmartin0.sceneformpolyexample.databinding.ActivityArBinding;
import com.kmartin0.sceneformpolyexample.util.CaptureSceneHelper;
import com.kmartin0.sceneformpolyexample.util.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ARActivity extends SingleModelARActivity<ActivityArBinding, ARViewModel>
		implements CaptureSceneHelper.CaptureSceneHelperCallbacks {

	@BindView(R.id.btn_add)
	ImageButton btnAdd;

	@BindView(R.id.btn_remove)
	ImageButton btnRemove;

	private Uri modelURI;
	private CaptureSceneHelper captureSceneHelper;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		modelURI = (Uri) Objects.requireNonNull(getIntent().getExtras()).get(Constants.AR_MODEL_URI);
		captureSceneHelper = new CaptureSceneHelper(this, getArFragment().getArSceneView(), this);
	}

	/**
	 * Adds a model to the anchor if no object is on the plane.
	 */
	@OnClick(R.id.btn_add)
	public void addObjectToPlane() {
		addObject(modelURI);
	}

	/**
	 * Removes the anchorNode from the scene.
	 */
	@OnClick(R.id.btn_remove)
	public void removeObjectFromPlane() {
		removeObject();
	}

	/**
	 * Rotates the node to the left when the motion event is DOWN.
	 * stops rotating when the motion event is UP
	 */
	@OnTouch(R.id.btn_rotate_counter_clockwise)
	public boolean rotateLeft(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startRotateCounterClockwise();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopRotateCounterClockwise();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Rotates the node to the right when the motion event is DOWN.
	 * stops rotating when the motion event is UP
	 */
	@OnTouch(R.id.btn_rotate_clockwise)
	public boolean rotateRight(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startRotateClockwise();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopRotateClockwise();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Rotates the node in an upward direction when the motion event is DOWN.
	 * stops rotating when the motion event is UP.
	 */
	@OnTouch(R.id.btn_rotate_upward)
	public boolean rotateUpward(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startRotateUpward();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopRotateUpward();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Rotates the node in an downward direction when the motion event is DOWN.
	 * stops rotating when the motion event is UP.
	 */
	@OnTouch(R.id.btn_rotate_downward)
	public boolean rotateBackward(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startRotateDownward();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopRotateDownward();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Enlarges the node when the motion event is DOWN.
	 * stops enlarging when the motion event is UP
	 */
	@OnTouch(R.id.btn_enlarge)
	public boolean enlarge(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startEnlarge();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopEnlarge();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Shrinks the node when the motion event is DOWN.
	 * stops shrinking when the motion event is UP
	 */
	@OnTouch(R.id.btn_shrink)
	public boolean shrink(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startShrink();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopShrink();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Moves the node in an upward direction when the motion event is DOWN.
	 * stops rotating when the motion event is UP.
	 */
	@OnTouch(R.id.btn_move_up)
	public boolean lift(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startMoveUp();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopMoveUp();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Moves the node in an downward direction when the motion event is DOWN.
	 * stops rotating when the motion event is UP.
	 */
	@OnTouch(R.id.btn_move_down)
	public boolean down(MotionEvent motionEvent) {
		if (!isAnchorSet()) return false;

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				getNode().startMoveDown();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				getNode().stopMoveDown();
				return true;
			}
			default:
				return false;
		}
	}

	/**
	 * Finish activity
	 */
	@OnClick(R.id.btn_back)
	public void finishActivity() {
		finish();
	}

	@Override
	protected void onModelAddedToScene() {
		btnAdd.setVisibility(View.INVISIBLE);
		btnRemove.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onModelRemovedToScene() {
		btnAdd.setVisibility(View.VISIBLE);
		btnRemove.setVisibility(View.INVISIBLE);
	}

	/**
	 * Create a snapshot of the current ar surface view.
	 */
	@OnClick(R.id.btn_capture)
	public void captureScene() {
		captureSceneHelper.captureScene();
	}

	/**
	 * Display a Snack bar message containing a success message and a button which redirects
	 * the user to the created snapshot.
	 *
	 * @param filename String filename of the created snapshot
	 */
	@Override
	public void onCaptureSuccess(String filename) {
		Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.photo_saved), Snackbar.LENGTH_LONG);
		snackbar.setAction(getString(R.string.open_photo), v -> captureSceneHelper.openImageForFilename(this, filename));
		snackbar.show();
		showLoading(false);
		getArFragment().getArSceneView().getPlaneRenderer().setVisible(true);
	}

	/**
	 * Display the message of the failed capture and set the ArSceneView back to it's normal state.
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
