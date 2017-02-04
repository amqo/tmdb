package amqo.com.privaliatmdb.model;

import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesConfiguration {

    @SerializedName("images")
    private MoviesConfigurationImages mMoviesConfigurationImages;

    public String getBaseUrl() {
        return mMoviesConfigurationImages.getBaseUrl();
    }

    public List<String> getSizes() {
        return mMoviesConfigurationImages.getPosterSizes();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void setSizes(List<String> sizes) {
        if (mMoviesConfigurationImages == null)
            mMoviesConfigurationImages = new MoviesConfigurationImages();
        mMoviesConfigurationImages.setPosterSizes(sizes);
    }
}
