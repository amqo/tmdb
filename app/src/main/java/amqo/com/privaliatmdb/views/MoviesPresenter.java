package amqo.com.privaliatmdb.views;

import android.content.SharedPreferences;
import android.text.TextUtils;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesEndpoint mMoviesEndpoint;
    private final MoviesContract.View mMoviesView;
    private final SharedPreferences mSharedPreferences;

    private Consumer<MoviesConfiguration> mMoviesConfigurationConsumer;
    private Consumer<Movies> mMoviesConsumer;

    private String mImageBaseUrl;

    private boolean mLoadingConfiguration = false;
    private boolean mConfigurationLoaded = false;

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

        if (!TextUtils.isEmpty(mImageBaseUrl))
            return mImageBaseUrl;

        String imageBaseUrl = mSharedPreferences.getString(MoviesEndpoint.BASE_IMAGE_API_KEY, "");
        String imageSize = mSharedPreferences.getString(MoviesEndpoint.BASE_IMAGE_SIZE_KEY, "");
        if (TextUtils.isEmpty(imageBaseUrl) || TextUtils.isEmpty(imageSize)) {
            updateMoviesConfiguration();
            return imageBaseUrl;
        }
        mImageBaseUrl = imageBaseUrl + imageSize;
        return mImageBaseUrl;
    }

    @Override
    public void updateMoviesConfiguration() {

        if (mLoadingConfiguration || mConfigurationLoaded) return;
        mLoadingConfiguration = true;

        Observable<MoviesConfiguration> configurationObservable =
                mMoviesEndpoint.getMoviesConfiguration(
                        MoviesEndpoint.API_VERSION,
                        MovieParameterCreator.createMoviesConfigurationParameters());

        configurationObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMoviesConfigurationConsumer);
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

    private void initMoviesConsumer() {
        mMoviesConsumer = new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception {
                mMoviesView.onMoviesLoaded(movies);
            }
        };
    }

    private void initMoviesConfigurationConsumer() {
        mMoviesConfigurationConsumer = new Consumer<MoviesConfiguration>() {
            @Override
            public void accept(MoviesConfiguration moviesConfiguration) throws Exception {
                SharedPreferences.Editor editor = mSharedPreferences.edit();

                editor.putString(MoviesEndpoint.BASE_IMAGE_API_KEY,
                        moviesConfiguration.getBaseUrl());

                String imageSize = ScreenHelper.getCorrectImageSize(mMoviesView, moviesConfiguration);
                editor.putString(MoviesEndpoint.BASE_IMAGE_SIZE_KEY, imageSize);
                editor.commit();

                // This is to avoid repeated configuration loads when the error was not due to this
                mConfigurationLoaded = true;

                mMoviesView.refreshMovies();
            }
        };
    }
}
