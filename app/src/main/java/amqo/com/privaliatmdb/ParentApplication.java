package amqo.com.privaliatmdb;

import android.app.Application;

import amqo.com.privaliatmdb.infrastructure.injection.DaggerParentComponent;
import amqo.com.privaliatmdb.infrastructure.injection.ParentComponent;
import amqo.com.privaliatmdb.infrastructure.injection.modules.AppModule;
import amqo.com.privaliatmdb.infrastructure.injection.modules.TMDBModule;

public class ParentApplication extends Application {

    private static ParentApplication INSTANCE;

    private ParentComponent mParentComponent;

    public static ParentApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        mParentComponent = DaggerParentComponent.builder()
                .appModule(new AppModule(this))
                .tMDBModule(new TMDBModule())
                .build();
    }

    public ParentComponent getParentComponent() {
        return mParentComponent;
    }
}
