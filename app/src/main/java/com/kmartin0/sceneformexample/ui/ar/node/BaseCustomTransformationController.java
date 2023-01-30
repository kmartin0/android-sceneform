package com.kmartin0.sceneformexample.ui.ar.node;

import androidx.annotation.NonNull;

public abstract class BaseCustomTransformationController {

	protected CustomNode node;

	public BaseCustomTransformationController(@NonNull CustomNode node) {
		this.node = node;
	}

	public abstract void reset();

}
