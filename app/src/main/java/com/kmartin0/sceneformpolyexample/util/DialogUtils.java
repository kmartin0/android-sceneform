package com.kmartin0.sceneformpolyexample.util;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class DialogUtils {
	public static void createErrorDialog(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setTitle(title);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}
