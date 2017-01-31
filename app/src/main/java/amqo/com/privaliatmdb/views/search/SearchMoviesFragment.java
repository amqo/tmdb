package amqo.com.privaliatmdb.views.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.model.MoviesScrollContract;
import amqo.com.privaliatmdb.views.BaseScrollListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchMoviesFragment extends Fragment implements MoviesContract.View, MoviesScrollContract.View {

    @Inject MoviesContract.Presenter mMoviesPresenter;
    @Inject RecyclerView.LayoutManager mLayoutManager;
    @Inject BaseScrollListener mScrollListener;

    public static SearchMoviesFragment newInstance() {
        return new SearchMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MoviesApplication.getInstance().getSearchMoviesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public void setLoading(boolean loading) {

    }

    @Override
    public void onMovieInteraction(Movie movie) {

    }

    @Override
    public int getScreenDensity() {
        return 0;
    }

    @Override
    public void onMoviesLoaded(Movies movies) {

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void loadMoreMovies() {

    }
}
