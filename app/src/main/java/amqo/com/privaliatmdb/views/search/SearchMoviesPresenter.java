package amqo.com.privaliatmdb.views.search;

import android.content.SharedPreferences;
import android.text.TextUtils;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.BaseMoviesPresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchMoviesPresenter extends BaseMoviesPresenter implements MoviesContract.PresenterSearch {

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

        searchMovies(page, query, mMoviesConsumer);
    }

    @Override
    public String getMovieImagesBaseUrl() {
        return super.getMovieImagesBaseUrl();
    }

    @Override
    public void updateMoviesConfiguration() {
        super.updateMoviesConfiguration();
    }

    private void searchMovies(int page, String query, Consumer<Movies> consumer) {
        Observable<Movies> moviesObservable = mMoviesEndpoint.searchMovies(
                MoviesEndpoint.API_VERSION,
                MovieParameterCreator.createSearchParameters(page, query));

        moviesObservable
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mMoviesView.setLoading(false);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }
}
