package amqo.com.privaliatmdb.injection;

import android.content.SharedPreferences;

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

    SharedPreferences getSharedPreferences();

    MoviesComponent getMoviesComponent(
            MoviesModule moviesModule);

    SearchMoviesComponent getSearchMoviesComponent(
            SearchMoviesModule moviesModule);
}

