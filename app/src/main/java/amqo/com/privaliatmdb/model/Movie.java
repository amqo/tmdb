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

    @SerializedName("vote_average")
    private float mVoteAverage;

    public String getPosterPath(String basePath) {
        String posterUrl = basePath + mPosterPath;
        return posterUrl;
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

    public float getVoteAverage() {
        return mVoteAverage;
    }
}
