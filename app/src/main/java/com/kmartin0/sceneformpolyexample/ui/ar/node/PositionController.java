package com.kmartin0.sceneformpolyexample.ui.ar.node;

import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;

public class PositionController extends BaseCustomTransformationController {

	private final float Y_POSITION_APPEND = 0.03f;

	PositionController(CustomNode node) {
		super(node);
	}

	private Scene.OnUpdateListener updateListenerMoveUp = frameTime -> {
		Vector3 curWorldPosition = node.getWorldPosition();
		node.setWorldPosition(new Vector3(curWorldPosition.x, curWorldPosition.y + Y_POSITION_APPEND, curWorldPosition.z));
	};

	private Scene.OnUpdateListener updateListenerMoveDown = frameTime -> {
		Vector3 curWorldPosition = node.getWorldPosition();
		node.setWorldPosition(new Vector3(curWorldPosition.x, curWorldPosition.y - Y_POSITION_APPEND, curWorldPosition.z));
	};

	public void startMoveUp() {
		node.getScene().addOnUpdateListener(updateListenerMoveUp);
	}

	public void stopMoveUp() {
		node.getScene().removeOnUpdateListener(updateListenerMoveUp);
	}

	public void startMoveDown() {
		node.getScene().addOnUpdateListener(updateListenerMoveDown);
	}

	public void stopMoveDown() {
		node.getScene().removeOnUpdateListener(updateListenerMoveDown);
	}

	@Override
	public void reset() {
		if (node.getScene() != null) {
			node.getScene().removeOnUpdateListener(updateListenerMoveUp);
			node.getScene().removeOnUpdateListener(updateListenerMoveDown);
		}
	}

}
