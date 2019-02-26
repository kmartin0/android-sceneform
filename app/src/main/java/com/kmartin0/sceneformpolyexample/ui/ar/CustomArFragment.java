package com.kmartin0.sceneformpolyexample.ui.ar;

import android.Manifest;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {

	@Override
	public void onResume() {
		super.onResume();
		setFocusMode();
	}

	private void setFocusMode() {
		Session session = getArSceneView().getSession();

		if (session != null && session.getConfig().getFocusMode() != Config.FocusMode.AUTO) {
			Config config = session.getConfig();
			config.setFocusMode(Config.FocusMode.AUTO);
			session.configure(config);
			getArSceneView().setupSession(session);
		}
	}

	@Override
	public String[] getAdditionalPermissions() {
		String[] additionalPermissions = super.getAdditionalPermissions();
		int permissionLength = additionalPermissions != null ? additionalPermissions.length : 0;
		String[] permissions = new String[permissionLength + 1];
		permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
		if (permissionLength > 0) {
			System.arraycopy(additionalPermissions, 0, permissions, 1, additionalPermissions.length);
		}
		return permissions;
	}
}
