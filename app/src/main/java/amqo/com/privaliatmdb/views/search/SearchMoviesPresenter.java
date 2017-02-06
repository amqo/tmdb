package amqo.com.privaliatmdb.views.search;

import android.content.SharedPreferences;
import android.text.TextUtils;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.BaseMoviesPresenter;
import io.reactivex.Observable;

public class SearchMoviesPresenter extends BaseMoviesPresenter
    implements MoviesContract.PresenterSearch {

    private String mCurrentQuery = "";

    public SearchMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            MoviesContract.View moviesView,
            SharedPreferences sharedPreferences) {

        mMoviesEndpoint = moviesEndpoint;
        mMoviesView = moviesView;
        mSharedPreferences = sharedPreferences;

        initMoviesConsumer();
        initMoviesConfigurationConsumer();
    }

    // MoviesContract.Presenter methods

    @Override
    public void loadMoreMovies() {

        if (isInLastPage()) return;

        if (TextUtils.isEmpty(mCurrentQuery)) {
            mMoviesView.clearMovies();
            return;
        }

        mLoadingConfiguration = false;

        mMoviesView.setLoading(true);

        if (TextUtils.isEmpty(getMovieImagesBaseUrl())) return;

        int page = getLastPageLoaded() + 1;

        searchMoviesInPage(page);
    }

    @Override
    public void refreshMovies() {

        if (TextUtils.isEmpty(mCurrentQuery)) {
            mMoviesView.clearMovies();
            return;
        }

        mLoadingConfiguration = false;

        mMoviesView.setLoading(true);

        if (TextUtils.isEmpty(getMovieImagesBaseUrl())) return;

        searchMoviesInPage(1);
    }

    // MoviesContract.PresenterSearch methods

    @Override
    public void setNewQuery(String query) {
        mCurrentQuery = query;
    }

    private void searchMoviesInPage(int page) {

        Observable<Movies> moviesObservable = mMoviesEndpoint.searchMovies(
                MoviesEndpoint.API_VERSION,
                MovieParameterCreator.createSearchParameters(page, mCurrentQuery));

        doSubscribe(moviesObservable);
    }
}
