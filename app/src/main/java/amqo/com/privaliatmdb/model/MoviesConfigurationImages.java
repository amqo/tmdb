package amqo.com.privaliatmdb.model;

import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesConfigurationImages {

    @SerializedName("secure_base_url")
    private String mBaseUrl;

    @SerializedName("poster_sizes")
    private List<String> mPosterSizes;

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public List<String> getPosterSizes() {
        return mPosterSizes;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public void setPosterSizes(List<String> sizes) {
        mPosterSizes = sizes;
    }
}
