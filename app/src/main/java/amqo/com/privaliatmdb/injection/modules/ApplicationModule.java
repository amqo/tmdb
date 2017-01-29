package amqo.com.privaliatmdb.injection.modules;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.MoviesApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    MoviesApplication mApplication;

    public ApplicationModule(MoviesApplication application) {
        mApplication = application;
    }

    @Provides @Singleton
    MoviesApplication providesApplication() {
        return mApplication;
    }
}
