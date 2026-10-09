package nl.kmartin.sceneformshowcase.util;

import android.graphics.Color;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public final class EdgeToEdgeUtils {

    private EdgeToEdgeUtils() {
    }

    /**
     * Enables edge-to-edge for normal application screens.
     */
    public static void enable(ComponentActivity activity) {
        EdgeToEdge.enable(
                activity,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        );
    }

    /**
     * Enables full-bleed edge-to-edge with light system bar icons.
     */
    public static void enableFullBleed(ComponentActivity activity) {
        EdgeToEdge.enable(
                activity,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.dark(Color.TRANSPARENT)
        );
    }

    /**
     * Pads the view so its content stays clear of the system bars and display cutout.
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