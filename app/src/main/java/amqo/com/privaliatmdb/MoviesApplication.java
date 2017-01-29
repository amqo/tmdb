package amqo.com.privaliatmdb;

import android.app.Application;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.MainActivityComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MainActivityModule;

public class MoviesApplication extends Application {

    private static MoviesApplication INSTANCE;

    private ApplicationComponent mApplicationComponent;

    private MainActivityComponent mMainActivityComponent;

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

    public void createMainActivityComponent(MainActivity activity) {
        mMainActivityComponent = mApplicationComponent.getMainActivityComponent(
                new MainActivityModule(activity));
    }

    public MainActivityComponent getMainActivityComponent() {
        return mMainActivityComponent;
    }

    public void releaseMainActivityComponent() {
        mMainActivityComponent = null;
    }
}
