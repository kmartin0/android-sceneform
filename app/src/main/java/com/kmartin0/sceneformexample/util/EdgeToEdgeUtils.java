package com.kmartin0.sceneformexample.util;

import android.graphics.Color;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;

public final class EdgeToEdgeUtils {

    private EdgeToEdgeUtils() {
    }

    /**
     * Enables edge-to-edge with transparent system bars.
     * Must be called before {@code super.onCreate()}.
     *
     * @param activity the activity to enable edge-to-edge for.
     */
    public static void enable(ComponentActivity activity) {
        EdgeToEdge.enable(
                activity,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        );
    }

    /**
     * Pads the view so its content stays clear of the system bars and display cutout.
     *
     * @param view the view to pad.
     */
    public static void applyPadding(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout()
            );
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
