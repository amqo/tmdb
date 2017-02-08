package amqo.com.privaliatmdb;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.BaseMoviesComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MoviesModule;
import amqo.com.privaliatmdb.injection.modules.SearchMoviesModule;
import amqo.com.privaliatmdb.views.BaseMoviesFragment;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;
import amqo.com.privaliatmdb.views.search.SearchMoviesFragment;
import io.fabric.sdk.android.Fabric;

public class MoviesApplication extends Application {

    private static MoviesApplication INSTANCE;

    private ApplicationComponent mApplicationComponent;

    private BaseMoviesComponent mActiveComponent;

    public static MoviesApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        INSTANCE = this;

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public void createActiveComponent(BaseMoviesFragment baseMoviesFragment) {

        if (baseMoviesFragment instanceof MoviesFragment) {
            mActiveComponent = mApplicationComponent.getMoviesComponent(
                    new MoviesModule(baseMoviesFragment));

        } else if (baseMoviesFragment instanceof SearchMoviesFragment) {
            mActiveComponent = mApplicationComponent.getSearchMoviesComponent(
                    new SearchMoviesModule(baseMoviesFragment));
        }
    }

    public void releaseActiveComponent() {
        mActiveComponent = null;
    }

    public BaseMoviesComponent getActiveComponent() {
        return mActiveComponent;
    }
}
