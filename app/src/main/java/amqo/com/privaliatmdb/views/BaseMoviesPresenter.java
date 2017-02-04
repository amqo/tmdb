package amqo.com.privaliatmdb.views;

import android.content.SharedPreferences;
import android.text.TextUtils;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseMoviesPresenter {

    protected MoviesEndpoint mMoviesEndpoint;
    protected MoviesContract.View mMoviesView;
    protected SharedPreferences mSharedPreferences;

    protected Consumer<MoviesConfiguration> mMoviesConfigurationConsumer;
    protected Consumer<Movies> mMoviesConsumer;

    protected String mImageBaseUrl;

    protected boolean mLoadingConfiguration = false;
    protected boolean mConfigurationLoaded = false;

    protected String getMovieImagesBaseUrl() {

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

    protected void updateMoviesConfiguration() {

        if (mLoadingConfiguration || mConfigurationLoaded) return;
        mLoadingConfiguration = true;

        Observable<MoviesConfiguration> configurationObservable =
                mMoviesEndpoint.getMoviesConfiguration(
                        MoviesEndpoint.API_VERSION,
                        MovieParameterCreator.createMoviesConfigurationParameters());

        configurationObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturnItem(new MoviesConfiguration())
                .subscribe(mMoviesConfigurationConsumer);
    }

    protected void initMoviesConsumer() {
        mMoviesConsumer = new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception {
                if (movies == null || movies.getMovies().isEmpty()) return;
                mMoviesView.onMoviesLoaded(movies);
            }
        };
    }

    protected void initMoviesConfigurationConsumer() {
        mMoviesConfigurationConsumer = new Consumer<MoviesConfiguration>() {
            @Override
            public void accept(MoviesConfiguration moviesConfiguration) throws Exception {

                if (moviesConfiguration.getSizes().isEmpty()) return;

                SharedPreferences.Editor editor = mSharedPreferences.edit();

                editor.putString(MoviesEndpoint.BASE_IMAGE_API_KEY,
                        moviesConfiguration.getBaseUrl());

                String imageSize = mMoviesView.getCorrectImageSize(moviesConfiguration);
                editor.putString(MoviesEndpoint.BASE_IMAGE_SIZE_KEY, imageSize);
                editor.commit();

                // This is to avoid repeated configuration loads when the error was not due to this
                mConfigurationLoaded = true;

                mMoviesView.refreshMovies();
            }
        };
    }
}
