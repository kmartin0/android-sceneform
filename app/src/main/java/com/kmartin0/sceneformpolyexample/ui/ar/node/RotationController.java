package com.kmartin0.sceneformpolyexample.ui.ar.node;


import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

public class RotationController extends BaseCustomTransformationController {

	private final float ROTATION_AXIS = 5f;

	RotationController(CustomNode node) {
		super(node);
	}

	private Scene.OnUpdateListener updateListenerRotateClockwise = frameTime -> {
		node.setLocalRotation(Quaternion.multiply(
				node.getLocalRotation(),
				Quaternion.axisAngle(new Vector3(0, 1f, 0f), ROTATION_AXIS)
		));
	};

	private Scene.OnUpdateListener updateListenerRotateCounterClockwise = frameTime -> {
		node.setLocalRotation(Quaternion.multiply(
				node.getLocalRotation(),
				Quaternion.axisAngle(new Vector3(0, 1f, 0f), -ROTATION_AXIS)
		));
	};

	private Scene.OnUpdateListener updateListenerRotateDownward = frameTime -> {
		node.setLocalRotation(Quaternion.multiply(
				node.getLocalRotation(),
				Quaternion.axisAngle(new Vector3(1f, 0f, 0f), ROTATION_AXIS)
		));
	};

	private Scene.OnUpdateListener updateListenerRotateUpward = frameTime -> {
		node.setLocalRotation(Quaternion.multiply(
				node.getLocalRotation(),
				Quaternion.axisAngle(new Vector3(1f, 0f, 0f), -ROTATION_AXIS)
		));
	};

	public void startRotateClockwise() {
		node.getScene().addOnUpdateListener(updateListenerRotateClockwise);
	}

	public void stopRotateClockwise() {
		node.getScene().removeOnUpdateListener(updateListenerRotateClockwise);
	}

	public void startRotateCounterClockwise() {
		node.getScene().addOnUpdateListener(updateListenerRotateCounterClockwise);
	}

	public void stopRotateCounterClockwise() {
		node.getScene().removeOnUpdateListener(updateListenerRotateCounterClockwise);
	}

	public void startRotateUpward() {
		node.getScene().addOnUpdateListener(updateListenerRotateUpward);
	}

	public void stopRotateUpward() {
		node.getScene().removeOnUpdateListener(updateListenerRotateUpward);
	}

	public void startRotateDownward() {
		node.getScene().addOnUpdateListener(updateListenerRotateDownward);
	}

	public void stopRotateDownward() {
		node.getScene().removeOnUpdateListener(updateListenerRotateDownward);
	}

	@Override
	public void reset() {
		if (node.getScene() != null) {
			node.getScene().removeOnUpdateListener(updateListenerRotateClockwise);
			node.getScene().removeOnUpdateListener(updateListenerRotateCounterClockwise);
			node.getScene().removeOnUpdateListener(updateListenerRotateUpward);
			node.getScene().removeOnUpdateListener(updateListenerRotateDownward);
		}
	}

}
