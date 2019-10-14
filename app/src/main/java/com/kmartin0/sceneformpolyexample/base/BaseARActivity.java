package com.kmartin0.sceneformpolyexample.base;

import android.animation.ObjectAnimator;
import androidx.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.kmartin0.sceneformpolyexample.R;
import com.kmartin0.sceneformpolyexample.util.CaptureSceneHelper;
import com.kmartin0.sceneformpolyexample.util.Constants;
import com.kmartin0.sceneformpolyexample.util.DialogUtils;
import com.kmartin0.sceneformpolyexample.util.node.CustomNode;


import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class BaseARActivity<VDB extends ViewDataBinding, BVM extends BaseViewModel>
		extends BaseActivity<VDB, BVM> implements CaptureSceneHelper.CaptureSceneHelperCallbacks {

	@BindView(R.id.btn_add)
	ImageButton btnAdd;

	@BindView(R.id.btn_remove)
	ImageButton btnRemove;

	public ArFragment arFragment;
	public AnchorNode anchorNode;
	public CustomNode node;

	private CaptureSceneHelper captureSceneHelper;
	protected Uri modelURI;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initArFragment();
		modelURI = (Uri) Objects.requireNonNull(getIntent().getExtras()).get(Constants.AR_MODEL_URI);
		captureSceneHelper = new CaptureSceneHelper(this, arFragment.getArSceneView(), this);
		if (!validateFileFormat(modelURI))
			DialogUtils.createErrorDialog(this, getString(R.string.ar_error), getString(R.string.unable_read_file));
	}

	/**
	 * Initialize the ArFragment.
	 */
	public void initArFragment() {
		arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_ar);
		arFragment.getPlaneDiscoveryController().hide();
		arFragment.getPlaneDiscoveryController().setInstructionView(null);
	}

	/**
	 * Performs a hit test to determine where in the 3D world space the object should be placed,
	 * the placeObject method to actually place the object.
	 *
	 * @param model the model to be placed.
	 */
	public void addObject(Uri model) {
		if (validateFileFormat(model)) {
			Frame frame = arFragment.getArSceneView().getArFrame();
			android.graphics.Point pt = getScreenCenter();
			List<HitResult> hits;
			if (frame != null) {
				hits = frame.hitTest(pt.x, pt.y);
				for (HitResult hit : hits) {
					Trackable trackable = hit.getTrackable();
					if (trackable instanceof Plane &&
							((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
						placeObject(arFragment, hit.createAnchor(), model);
						break;
					}
				}
			}
		}
	}

	/**
	 * Takes the ARCore anchor from the hitTest result and builds the Sceneform nodes.
	 * It starts the asynchronous loading of the 3D model using the ModelRenderable builder.
	 * Once the model is loaded as a Renderable, call addNodeToScene.
	 * If there was an error, show an alert dialog.
	 *
	 * @param fragment The ArFragment
	 * @param anchor   ARCore Anchor from the hit test result
	 * @param model    Uri of the model to be loaded
	 */
	public void placeObject(ArFragment fragment, Anchor anchor, Uri model) {
		RenderableSource.SourceType sourceType = null;
		if (getSourceType(model).equals(".gltf")) sourceType = RenderableSource.SourceType.GLTF2;
		else if (getSourceType(model).equals(".glb")) sourceType = RenderableSource.SourceType.GLB;

		showLoading(true);
		ModelRenderable.builder()
				.setSource(fragment.getContext(), RenderableSource.builder().setSource(
						Objects.requireNonNull(fragment.getContext()),
						model,
						sourceType).build())
				.setRegistryId(model)
				.build()
				.thenAccept(modelRenderable -> {
					addNodeToScene(fragment, anchor, modelRenderable);
					showLoading(false);
				})
				.exceptionally(throwable -> {
					runOnUiThread(() -> {
						DialogUtils.createErrorDialog(this, getString(R.string.ar_error), throwable.getMessage());
						showLoading(false);
					});
					return null;
				});

		arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
			// Create the Anchor.
			Anchor newAnchor = hitResult.createAnchor();
			AnchorNode newAnchorNode = new AnchorNode(newAnchor);
			newAnchorNode.setParent(arFragment.getArSceneView().getScene());
			if (anchorNode != null && anchorNode.isActive()) {
				moveObject(newAnchorNode);
			}
		});
	}

	/**
	 * Builds two nodes and attaches them to the ArSceneView's scene object.
	 * The first node is of type AnchorNode. Anchor nodes are positioned based on the pose of an
	 * ARCore Anchor. As a result, they stay positioned in the sample place relative to the real world.
	 * <p>
	 * The second Node is of base class Node where the @renderable is loaded into.
	 * Connects the nodes to each other and then connects the AnchorNode to the Scene
	 *
	 * @param fragment   The ArFragment
	 * @param anchor     ARCore Anchor from the hit test result
	 * @param renderable Renderable of the object to be shown.
	 */
	public void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable) {
		AnchorNode anchorNode = new AnchorNode(anchor);
		CustomNode andyNode = new CustomNode();

		andyNode.setRenderable(renderable);
		andyNode.setParent(anchorNode);

		fragment.getArSceneView().getScene().addChild(anchorNode);

		this.node = andyNode;
		this.anchorNode = anchorNode;

		updateControlButtons(true);
	}

	/**
	 * Shows the add or remove button based on @isNodeSet
	 *
	 * @param isNodeSet boolean when true shows remove button else the remove button.
	 */
	public void updateControlButtons(boolean isNodeSet) {
		if (isNodeSet) {
			btnAdd.setVisibility(View.INVISIBLE);
			btnRemove.setVisibility(View.VISIBLE);
		} else {
			btnAdd.setVisibility(View.VISIBLE);
			btnRemove.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * @return the center of the screen.
	 */
	public android.graphics.Point getScreenCenter() {
		View vw = findViewById(android.R.id.content);
		return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
	}

	/**
	 * Move the AnchorNode to another location.
	 *
	 * @param newAnchorNode AnchorNode of the new anchor.
	 */
	public void moveObject(AnchorNode newAnchorNode) {
		ObjectAnimator objectAnimation = new ObjectAnimator();
		objectAnimation.setAutoCancel(true);
		objectAnimation.setTarget(node);

		objectAnimation.setObjectValues(node.getWorldPosition(), newAnchorNode.getWorldPosition());

		objectAnimation.setPropertyName("worldPosition");

		// The Vector3Evaluator is used to evaluator 2 vector3 and return the next
		// vector3.  The default is to use lerp.
		objectAnimation.setEvaluator(new Vector3Evaluator());
		// This makes the animation linear (smooth and uniform).
		objectAnimation.setInterpolator(new LinearInterpolator());
		// Duration in ms of the animation.
		objectAnimation.setDuration(500);
		objectAnimation.start();
	}

	/**
	 * Removes the anchorNode from the scene.
	 */
	@OnClick(R.id.btn_remove)
	public void removeObjectFromPlane() {
		if (isAnchorSet()) {
			arFragment.getArSceneView().getScene().removeChild(anchorNode);
			node.reset();
			updateControlButtons(false);
		}
	}

	/**
	 * @return true if scene contains anchorNode
	 */
	public boolean isAnchorSet() {
		return arFragment.getArSceneView().getScene().getChildren().contains(anchorNode);
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
		Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Photo saved", Snackbar.LENGTH_LONG);
		snackbar.setAction("Open in Photos", v -> captureSceneHelper.openImageForFilename(BaseARActivity.this, filename));
		snackbar.show();
		showLoading(false);
		arFragment.getArSceneView().getPlaneRenderer().setVisible(true);
	}

	/**
	 * Display the message of the failed capture and set the ArSceneView back to it's normal state.
	 *
	 * @param message String message to be displayed.
	 */
	@Override
	public void onCaptureFail(String message) {
		Toast.makeText(BaseARActivity.this, message, Toast.LENGTH_LONG).show();
		arFragment.getArSceneView().getPlaneRenderer().setVisible(false);
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
	protected abstract void initViewModelBinding();

	/**
	 * Validate the filetype of the model.
	 * Only accept .gltf and .glb
	 */
	private boolean validateFileFormat(Uri modelURI) {
		if (modelURI == null) return false;

		String uri = modelURI.toString();
		String ext = uri.substring(uri.lastIndexOf("."));
		return ext.equals(".gltf") || ext.equals(".glb");
	}

	private String getSourceType(Uri modelURI) {
		String uri = modelURI.toString();
		return uri.substring(uri.lastIndexOf("."));
	}
}
