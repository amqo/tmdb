package amqo.com.privaliatmdb;

import android.app.Application;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.BaseMoviesComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.MoviesComponent;
import amqo.com.privaliatmdb.injection.SearchMoviesComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MoviesModule;
import amqo.com.privaliatmdb.injection.modules.SearchMoviesModule;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;
import amqo.com.privaliatmdb.views.search.SearchMoviesFragment;

public class MoviesApplication extends Application {

    private static MoviesApplication INSTANCE;

    private ApplicationComponent mApplicationComponent;

    private MoviesComponent mMoviesComponent;
    private SearchMoviesComponent mSearchMoviesComponent;

    public static MoviesApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public void createMoviesComponent(MoviesFragment fragment) {
        mMoviesComponent = mApplicationComponent.getMoviesComponent(
                new MoviesModule(fragment));
    }

    public MoviesComponent getMoviesComponent() {
        return mMoviesComponent;
    }

    public void releaseMoviesComponent() {
        mMoviesComponent = null;
    }

    public void createSearchMoviesComponent(SearchMoviesFragment fragment) {
        mSearchMoviesComponent = mApplicationComponent.getSearchMoviesComponent(
                new SearchMoviesModule(fragment));
    }

    public SearchMoviesComponent getSearchMoviesComponent() {
        return mSearchMoviesComponent;
    }

    public void releaseSearchMoviesComponent() {
        mSearchMoviesComponent = null;
    }

    public BaseMoviesComponent getCurrentMoviesComponent() {
        if (mSearchMoviesComponent != null) return mSearchMoviesComponent;
        else return mMoviesComponent;
    }
}
