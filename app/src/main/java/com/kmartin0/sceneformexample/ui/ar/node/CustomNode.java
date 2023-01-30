package com.kmartin0.sceneformexample.ui.ar.node;

import com.google.ar.sceneform.Node;

/**
 * Custom Node which can rotate and resize a node.
 */
public class CustomNode extends Node {

	private RotationController rotationController;
	private ScaleController scaleController;
	private PositionController positionController;

	public CustomNode() {
		rotationController = new RotationController(this);
		scaleController = new ScaleController(this);
		positionController = new PositionController(this);
	}

	public void startRotateClockwise() {
		rotationController.startRotateClockwise();
	}

	public void stopRotateClockwise() {
		rotationController.stopRotateClockwise();
	}

	public void startRotateCounterClockwise() {
		rotationController.startRotateCounterClockwise();
	}

	public void stopRotateCounterClockwise() {
		rotationController.stopRotateCounterClockwise();
	}

	public void startRotateUpward() {
		rotationController.startRotateUpward();
	}

	public void stopRotateUpward() {
		rotationController.stopRotateUpward();
	}

	public void startRotateDownward() {
		rotationController.startRotateDownward();
	}

	public void stopRotateDownward() {
		rotationController.stopRotateDownward();
	}

	public void startEnlarge() {
		scaleController.startEnlarge();
	}

	public void stopEnlarge() {
		scaleController.stopEnlarge();
	}

	public void startShrink() {
		scaleController.startShrink();
	}

	public void stopShrink() {
		scaleController.stopShrink();
	}

	public void startMoveUp() {
		positionController.startMoveUp();
	}

	public void stopMoveUp() {
		positionController.stopMoveUp();
	}

	public void startMoveDown() {
		positionController.startMoveDown();
	}

	public void stopMoveDown() {
		positionController.stopMoveDown();
	}

	/**
	 * Resets the node.
	 */
	public void reset() {
		rotationController.reset();
		scaleController.reset();
		positionController.reset();
	}

}
