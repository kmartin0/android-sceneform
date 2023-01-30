package com.kmartin0.sceneformexample.ui.ar.node;

import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;

public class ScaleController extends BaseCustomTransformationController {

	private final float SCALE_MULTIPLIER = 1.1f;

	ScaleController(CustomNode node) {
		super(node);
	}

	/**
	 * OnUpdateListener which enlarges the node.
	 */
	private Scene.OnUpdateListener updateListenerEnlarge = frameTime -> {
		Vector3 curScale = node.getLocalScale();

		node.setLocalScale(new Vector3(
				curScale.x * SCALE_MULTIPLIER,
				curScale.y * SCALE_MULTIPLIER,
				curScale.z * SCALE_MULTIPLIER));
	};

	/**
	 * OnUpdateListener which shrinks the node.
	 */
	private Scene.OnUpdateListener updateListenerShrink = frameTime -> {
		Vector3 curScale = node.getLocalScale();

		node.setLocalScale(new Vector3(
				curScale.x / SCALE_MULTIPLIER,
				curScale.y / SCALE_MULTIPLIER,
				curScale.z / SCALE_MULTIPLIER));
	};

	public void startEnlarge() {
		node.getScene().addOnUpdateListener(updateListenerEnlarge);
	}

	public void stopEnlarge() {
		node.getScene().removeOnUpdateListener(updateListenerEnlarge);
	}

	public void startShrink() {
		node.getScene().addOnUpdateListener(updateListenerShrink);
	}

	public void stopShrink() {
		node.getScene().removeOnUpdateListener(updateListenerShrink);
	}

	@Override
	public void reset() {
		if (node.getScene() != null) {
			node.getScene().removeOnUpdateListener(updateListenerEnlarge);
			node.getScene().removeOnUpdateListener(updateListenerShrink);
		}
	}
}
