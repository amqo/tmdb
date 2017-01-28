package amqo.com.privaliatmdb.injection.modules;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.ParentApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    ParentApplication mApplication;

    public ApplicationModule(ParentApplication application) {
        mApplication = application;
    }

    @Provides @Singleton
    ParentApplication providesApplication() {
        return mApplication;
    }
}
