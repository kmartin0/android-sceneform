package com.kmartin0.sceneformexample.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.core.content.FileProvider;

import android.view.PixelCopy;

import com.google.ar.sceneform.ArSceneView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureSceneHelper {

	private CaptureSceneHelperCallbacks callbacks;
	private ArSceneView arSceneView;
	private Activity activity;

	public CaptureSceneHelper(CaptureSceneHelperCallbacks callbacks, ArSceneView arSceneView, Activity activity) {
		this.callbacks = callbacks;
		this.arSceneView = arSceneView;
		this.activity = activity;
	}

	/**
	 * Takes a snapshot from the @arSceneView and saves it on the device.
	 */
	public void captureScene(Context context) {
		arSceneView.getPlaneRenderer().setVisible(false);

		// Create a bitmap the size of the scene view.
		final Bitmap bitmap = Bitmap.createBitmap(arSceneView.getWidth(), arSceneView.getHeight(),
				Bitmap.Config.ARGB_8888);

		// Create a handler thread to offload the processing of the image.
		final HandlerThread handlerThread = new HandlerThread("PixelCopier");

		new Handler().postDelayed(() -> {
			handlerThread.start();
			activity.runOnUiThread(() -> callbacks.onCaptureStart());

			// Make the request to copy.
			PixelCopy.request(arSceneView, bitmap, (copyResult) -> {
				if (copyResult == PixelCopy.SUCCESS) {
					onPixelCopySuccess(bitmap, context);
				} else {
					activity.runOnUiThread(() -> callbacks.onCaptureFail(getMessageForPixelCopyStatusCode(copyResult)));
				}
				handlerThread.quitSafely();
			}, new Handler(handlerThread.getLooper()));
		}, 500);

	}

	/**
	 * Create a filename, Save the @bitmap on the disk and notify the listeners.
	 *
	 * @param bitmap Bitmap created from the PixelCopy Request.
	 */
	private void onPixelCopySuccess(Bitmap bitmap, Context context) {
		String filename = generateFilename();
		try {
			File savedPhoto = saveBitmapToDisk(bitmap, filename);
			activity.runOnUiThread(() -> {
				updateMediaStore(context, savedPhoto);
				callbacks.onCaptureSuccess(savedPhoto);
			});
		} catch (IOException e) {
			activity.runOnUiThread(() -> callbacks.onCaptureFail("Capture failed with error: " + e.getMessage()));
		}
	}

	/**
	 * Save the @bitmap on the device using the filename.
	 *
	 * @param bitmap   Bitmap to be saved on the disk.
	 * @param filename String filename to be used.
	 * @throws IOException
	 */
	private File saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {
		File out = new File(filename);
		if (!out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}

		FileOutputStream outputStream = new FileOutputStream(filename);
		ByteArrayOutputStream outputData = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
		outputData.writeTo(outputStream);
		outputStream.flush();
		outputStream.close();

		return out;
	}

	/**
	 * Generate a filename using the current datetime in the DCIM/Sceneform.
	 *
	 * @return String unique filename
	 */
	private String generateFilename() {
		String date =
				new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
		return Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DCIM) + File.separator + "Sceneform/" + date + "_screenshot.png";
	}

	private void updateMediaStore(Context context, File file) {
		MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/png"}, null);
	}

	/**
	 * Open an image using an action intent.
	 *
	 * @param context  Context of the activity.
	 * @param photo File of the image to be opened.
	 */
	public void openSavedImage(Context context, File photo) {

		Uri photoURI = FileProvider.getUriForFile(context,
				"com.kmartin.sceneformexample.provider",
				photo);

		Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
		intent.setDataAndType(photoURI, "image/*");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		context.startActivity(intent);
	}

	/**
	 * Converts the pixel copy status code to a readable text.
	 *
	 * @param copyResult int result code retrieved from the PixelCopy Request.
	 * @return String message of the @copyResult
	 */
	private String getMessageForPixelCopyStatusCode(int copyResult) {
		switch (copyResult) {
			case PixelCopy.SUCCESS:
				return "The pixel copy request succeeded";
			case PixelCopy.ERROR_UNKNOWN:
				return "The capture failed with an unknown error.";
			case PixelCopy.ERROR_TIMEOUT:
				return "A timeout occurred while trying to acquire a buffer from the source to copy from.";
			case PixelCopy.ERROR_SOURCE_NO_DATA:
				return "The source has nothing to copy from.";
			case PixelCopy.ERROR_SOURCE_INVALID:
				return "It is not possible to copy from the source. ";
			case PixelCopy.ERROR_DESTINATION_INVALID:
				return "The destination isn't a valid copy target.";
			default:
				return "Unknown status code.";
		}
	}

	public interface CaptureSceneHelperCallbacks {
		void onCaptureSuccess(File file);

		void onCaptureFail(String message);

		void onCaptureStart();
	}

}

