package amqo.com.privaliatmdb.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("release_date")
    private String mReleaseDate;

    public String getPosterPath(String basePath) {
        return basePath + mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTitleWithYear() {
        return mTitle + " (" + getReleaseYear() + ")";
    }

    private String getReleaseYear() {
        return mReleaseDate.isEmpty() ?
                "" : mReleaseDate.substring(0, 4);
    }
}
