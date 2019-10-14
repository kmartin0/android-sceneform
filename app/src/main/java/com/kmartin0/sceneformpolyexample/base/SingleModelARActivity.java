package com.kmartin0.sceneformpolyexample.base;

import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

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
import com.kmartin0.sceneformpolyexample.util.DialogUtils;
import com.kmartin0.sceneformpolyexample.util.DimenUtils;
import com.kmartin0.sceneformpolyexample.ui.ar.node.CustomNode;

import java.util.List;
import java.util.Objects;

public abstract class SingleModelARActivity<VDB extends ViewDataBinding, BVM extends BaseViewModel>
		extends BaseActivity<VDB, BVM> {

	private ArFragment arFragment;
	private AnchorNode anchorNode;
	private CustomNode node;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initArFragment();
	}

	/**
	 * Initialize the ArFragment.
	 */
	public void initArFragment() {
		arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_ar);
	}

	/**
	 * Performs a hit test to determine where in the 3D world space the object should be placed,
	 * the placeObject method to actually place the object.
	 *
	 * @param model the model to be placed.
	 */
	protected void addObject(Uri model) {
		if (!isModelFileFormatValid(model)) {
			DialogUtils.createErrorDialog(this, getString(R.string.ar_error), getString(R.string.unable_read_file));
			return;
		}

		if (!isAnchorSet()) {
			Frame frame = arFragment.getArSceneView().getArFrame();
			android.graphics.Point pt = DimenUtils.getScreenCenter(this);
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
	private void placeObject(ArFragment fragment, Anchor anchor, Uri model) {
		showLoading(true);
		ModelRenderable.builder()
				.setSource(fragment.getContext(), RenderableSource.builder().setSource(
						Objects.requireNonNull(fragment.getContext()),
						model,
						getSourceType(model)).build())
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
	private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable) {
		AnchorNode anchorNode = new AnchorNode(anchor);
		CustomNode customNode = new CustomNode();

		customNode.setRenderable(renderable);
		customNode.setParent(anchorNode);

		fragment.getArSceneView().getScene().addChild(anchorNode);

		this.node = customNode;
		this.anchorNode = anchorNode;

		setupMoveObjectListener();
	}

	private void setupMoveObjectListener() {
		arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
			// Create the Anchor.
			Anchor newAnchor = hitResult.createAnchor();
			AnchorNode newAnchorNode = new AnchorNode(newAnchor);
			newAnchorNode.setParent(arFragment.getArSceneView().getScene());
			if (anchorNode != null && anchorNode.isActive()) {
				moveObject(newAnchorNode);
			}
		});
		onModelAddedToScene();
	}

	/**
	 * Move the AnchorNode to another location.
	 *
	 * @param newAnchorNode AnchorNode of the new anchor.
	 */
	protected void moveObject(AnchorNode newAnchorNode) {
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
	protected void removeObject() {
		if (isAnchorSet()) {
			arFragment.getArSceneView().getScene().removeChild(anchorNode);
			node.reset();
			onModelRemovedToScene();
		}
	}

	/**
	 * @return true if scene contains anchorNode
	 */
	public boolean isAnchorSet() {
		return arFragment.getArSceneView().getScene().getChildren().contains(anchorNode);
	}

	/**
	 * Validate the filetype of the model.
	 */
	private boolean isModelFileFormatValid(Uri modelURI) {
		return getSourceType(modelURI) != null;
	}

	private RenderableSource.SourceType getSourceType(Uri modelURI) {
		String uri = modelURI.toString();
		String ext = uri.substring(uri.lastIndexOf("."));

		RenderableSource.SourceType sourceType;

		switch (ext.toLowerCase()) {
			case ".gltf":
				sourceType = RenderableSource.SourceType.GLTF2;
				break;
			case ".glb":
				sourceType = RenderableSource.SourceType.GLB;
				break;
			default:
				sourceType = null;
		}
		return sourceType;
	}

	public CustomNode getNode() {
		return node;
	}

	public ArFragment getArFragment() {
		return arFragment;
	}

	protected abstract void onModelAddedToScene();

	protected abstract void onModelRemovedToScene();

}
