package amqo.com.privaliatmdb;

import android.app.Application;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.MoviesComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MoviesModule;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;

public class MoviesApplication extends Application {

    private static MoviesApplication INSTANCE;

    private ApplicationComponent mApplicationComponent;

    private MoviesComponent mMoviesComponent;

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

    public void createMainActivityComponent(MoviesFragment fragment) {
        mMoviesComponent = mApplicationComponent.getMoviesComponent(
                new MoviesModule(fragment));
    }

    public MoviesComponent getMainActivityComponent() {
        return mMoviesComponent;
    }

    public void releaseMainActivityComponent() {
        mMoviesComponent = null;
    }
}
