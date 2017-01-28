package amqo.com.privaliatmdb;

import android.app.Application;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.MainActivityComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MainActivityModule;

public class ParentApplication extends Application {

    private static ParentApplication INSTANCE;

    private ApplicationComponent mApplicationComponent;

    private MainActivityComponent mMainActivityComponent;

    public static ParentApplication getInstance() {
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

    public MainActivityComponent getMainActivityComponent(MainActivity activity) {
        mMainActivityComponent = mApplicationComponent.getMainActivityComponent(
                new MainActivityModule(activity));
        return mMainActivityComponent;
    }

    public void releaseMainActivityComponent() {
        mMainActivityComponent = null;
    }
}
