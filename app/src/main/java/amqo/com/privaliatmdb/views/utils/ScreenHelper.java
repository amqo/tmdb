package amqo.com.privaliatmdb.views.utils;

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
}
