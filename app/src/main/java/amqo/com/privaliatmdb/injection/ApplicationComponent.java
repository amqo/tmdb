package amqo.com.privaliatmdb.injection;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MoviesModule;
import amqo.com.privaliatmdb.injection.modules.SearchMoviesModule;
import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {

    MoviesApplication application();

    MoviesComponent getMoviesComponent(
            MoviesModule moviesModule);

    SearchMoviesComponent getSearchMoviesComponent(
            SearchMoviesModule moviesModule);
}

