package com.kmartin0.sceneformpolyexample.util;

import android.app.Activity;
import android.view.View;

public class DimenUtils {

	/**
	 * @return the center of the screen.
	 */
	public static android.graphics.Point getScreenCenter(Activity activity) {
		View vw = activity.findViewById(android.R.id.content);
		return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
	}

}
