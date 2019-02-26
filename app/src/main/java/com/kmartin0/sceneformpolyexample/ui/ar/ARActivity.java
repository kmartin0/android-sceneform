package com.kmartin0.sceneformpolyexample.ui.ar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.kmartin0.sceneformpolyexample.R;
import com.kmartin0.sceneformpolyexample.base.BaseARActivity;
import com.kmartin0.sceneformpolyexample.databinding.ActivityArBinding;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Helpful sources:
 * https://github.com/google-ar/sceneform-android-sdk/issues/201
 * https://github.com/google-ar/sceneform-android-sdk/issues/238
 * https://developers.google.com/ar/reference/java/com/google/ar/sceneform/Scene#addOnUpdateListener(com.google.ar.sceneform.Scene.OnUpdateListener)
 */
public class ARActivity extends BaseARActivity<ActivityArBinding, ARViewModel> {

	@BindView(R.id.btn_add)
	ImageButton btnAdd;

	@BindView(R.id.btn_remove)
	ImageButton btnRemove;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Adds a model to the anchor if no object is on the plane.
	 */
	@OnClick(R.id.btn_add)
	public void addObjectToPlane() {
		if (!isAnchorSet()) {
			addObject(modelURI);
		}
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
				node.startRotateCounterClockwise();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopRotateCounterClockwise();
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
				node.startRotateClockwise();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopRotateClockwise();
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
				node.startRotateUpward();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopRotateUpward();
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
				node.startRotateDownward();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopRotateDownward();
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
				node.startEnlarge();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopEnlarge();
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
				node.startShrink();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopShrink();
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
				node.startMoveUp();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopMoveUp();
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
				node.startMoveDown();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				node.stopMoveDown();
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
	public void goBack() {
		finish();
	}

	/**
	 * @param clickable boolean Sets the add and remove button clickable status.
	 */
	private void setControlButtonsClickable(boolean clickable) {
		btnAdd.setClickable(clickable);
		btnRemove.setClickable(clickable);
	}

	/**
	 * Shows a loading indicator based on @visibility.
	 *
	 * @param visibility boolean of the visibility for the loading indicator.
	 */
	@Override
	public void showLoading(boolean visibility) {
		super.showLoading(visibility);
		setControlButtonsClickable(!visibility);
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
