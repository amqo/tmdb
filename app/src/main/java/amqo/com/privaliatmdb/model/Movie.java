package amqo.com.privaliatmdb.model;

import com.google.gson.annotations.SerializedName;

import amqo.com.privaliatmdb.network.MoviesEndpoint;

public class Movie {

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("release_date")
    private String mReleaseDate;

    public String getPosterPath() {

        String posterUrl = MoviesEndpoint.BASE_IMAGE_API_URL;
        posterUrl += "w185/"; //TODO get size depending on screen density
        posterUrl += mPosterPath;

        return posterUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseYear() {
        return mReleaseDate.isEmpty() ?
                "" : mReleaseDate.substring(0, 4);
    }
}
