package amqo.com.privaliatmdb.infrastructure.injection;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.MainActivity;
import amqo.com.privaliatmdb.infrastructure.injection.modules.AppModule;
import amqo.com.privaliatmdb.infrastructure.injection.modules.TMDBModule;
import amqo.com.privaliatmdb.network.TMDBController;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, TMDBModule.class})
public interface ParentComponent {
    void inject(MainActivity activity);
    void inject(TMDBController tmdbController);
}

