package amqo.com.privaliatmdb.views.popular;

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

public class MoviesPresenter extends BaseMoviesPresenter implements MoviesContract.PresenterPopular {

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

    @Override
    public void getMovies(int page) {

        mLoadingConfiguration = false;

        mMoviesView.setLoading(true);

        if (TextUtils.isEmpty(getMovieImagesBaseUrl())) return;

        getMoviesFromPage(page, mMoviesConsumer);
    }

    @Override
    public String getMovieImagesBaseUrl() {
        return super.getMovieImagesBaseUrl();
    }

    @Override
    public void updateMoviesConfiguration() {
        super.updateMoviesConfiguration();
    }

    private void getMoviesFromPage(int page, Consumer<Movies> consumer) {
        Observable<Movies> moviesObservable = mMoviesEndpoint.getMovies(
                MoviesEndpoint.API_VERSION,
                MovieParameterCreator.createPopularMoviesParameters(page));

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
