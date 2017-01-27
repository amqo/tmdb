package amqo.com.privaliatmdb.infrastructure.injection.modules;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.ParentApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    ParentApplication mApplication;

    public AppModule(ParentApplication application) {
        mApplication = application;
    }

    @Provides @Singleton
    ParentApplication providesApplication() {
        return mApplication;
    }
}
