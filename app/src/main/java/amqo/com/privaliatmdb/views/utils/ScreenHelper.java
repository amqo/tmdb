package amqo.com.privaliatmdb.views.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.model.MoviesContract;

public abstract class ScreenHelper {

    public static String getCorrectImageSize(
            MoviesContract.View view,
            MoviesConfiguration moviesConfiguration) {

        String imageSize = "";
        // To avoid near values going to the next too high density
        int screenSize = view.getScreenDensity() - 50;
        for (String size : moviesConfiguration.getSizes()) {
            try {
                int sizeInt = Integer.valueOf(size.substring(1, size.length()));
                if (sizeInt > screenSize) {
                    imageSize = size;
                    break;
                }
            } catch (NumberFormatException e) {
                imageSize = size;
                break;
            }
        }
        return imageSize;
    }

    public static int getScreenDensity(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return (int)metrics.xdpi;
    }
}
