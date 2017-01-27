package amqo.com.privaliatmdb.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movies {

    @SerializedName("page")
    private int mPage;

    @SerializedName("total_results")
    private int mTotalResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    @SerializedName("results")
    List<Movie> mMovies = new ArrayList<>();

    public int getPage() {
        return mPage;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }
}
