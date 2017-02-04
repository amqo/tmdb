package amqo.com.privaliatmdb.views.utils;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.List;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.MoviesConfiguration;

public class ScreenHelper {

    private Activity mActivity;

    @Inject
    public ScreenHelper(Activity activity) {
        mActivity = activity;
    }

    public String getCorrectImageSize(MoviesConfiguration moviesConfiguration) {

        String imageSize = "";
        // To avoid near values going to the next too high density
        int screenSize = getScreenDensity() - 50;
        List<String> sizes = moviesConfiguration.getSizes();
        for (String size : sizes) {
            try {
                int sizeInt = Integer.valueOf(size.substring(1, size.length()));
                if (sizeInt > screenSize) {
                    imageSize = size;
                    break;
                }
            } catch (NumberFormatException e) {
                imageSize = sizes.get(sizes.size() - 1);
                break;
            }
        }
        return imageSize;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public int getScreenDensity() {

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return (int)metrics.xdpi;
    }
}
