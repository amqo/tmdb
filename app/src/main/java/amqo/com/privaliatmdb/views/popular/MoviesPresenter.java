package amqo.com.privaliatmdb.views.popular;

import android.content.SharedPreferences;
import android.text.TextUtils;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.BaseMoviesPresenter;
import io.reactivex.Observable;

public class MoviesPresenter extends BaseMoviesPresenter {

    public MoviesPresenter(
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

        int page = getLastPageLoaded() + 1;

        mLoadingConfiguration = false;

        mMoviesView.setLoading(true);

        if (TextUtils.isEmpty(getMovieImagesBaseUrl())) return;

        loadMoreMoviesInPage(page);
    }

    @Override
    public void refreshMovies() {

        mLoadingConfiguration = false;

        mMoviesView.setLoading(true);

        if (TextUtils.isEmpty(getMovieImagesBaseUrl())) return;

        loadMoreMoviesInPage(1);
    }

    private void loadMoreMoviesInPage(int page) {

        Observable<Movies> moviesObservable = mMoviesEndpoint.getMovies(
                MoviesEndpoint.API_VERSION,
                MovieParameterCreator.createPopularMoviesParameters(page));

        doSubscribe(moviesObservable);
    }
}
