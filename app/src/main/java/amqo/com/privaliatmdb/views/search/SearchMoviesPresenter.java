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

    public SearchMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            MoviesContract.ViewSearch moviesView,
            SharedPreferences sharedPreferences) {

        mMoviesEndpoint = moviesEndpoint;
        mMoviesView = moviesView;
        mSharedPreferences = sharedPreferences;

        initMoviesConsumer();
        initMoviesConfigurationConsumer();
    }

    @Override
    public void searchMovies(int page, String query) {

        if (TextUtils.isEmpty(query)) return;

        mLoadingConfiguration = false;

        mMoviesView.setLoading(true);

        if (TextUtils.isEmpty(getMovieImagesBaseUrl())) return;

        doSearch(page, query);
    }

    private void doSearch(int page, String query) {

        Observable<Movies> moviesObservable = mMoviesEndpoint.searchMovies(
                MoviesEndpoint.API_VERSION,
                MovieParameterCreator.createSearchParameters(page, query));

        doSearch(moviesObservable);
    }
}
