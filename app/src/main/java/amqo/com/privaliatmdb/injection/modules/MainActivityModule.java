package amqo.com.privaliatmdb.injection.modules;

import amqo.com.privaliatmdb.MainActivity;
import amqo.com.privaliatmdb.injection.scopes.PerActivity;
import amqo.com.privaliatmdb.network.IMoviesController;
import amqo.com.privaliatmdb.MoviesActivityPresenter;
import amqo.com.privaliatmdb.network.IMoviesEndpoint;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.PopularMoviesParametersCreator;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    MainActivity mMainActivity;

    public MainActivityModule(MainActivity activity) {
        mMainActivity = activity;
    }

    @Provides @PerActivity
    MovieParameterCreator providesParameterCreator() {
        return new PopularMoviesParametersCreator();
    }

    @Provides @PerActivity
    IMoviesController providesMoviesController(
            MovieParameterCreator movieParameterCreator,
            IMoviesEndpoint moviesEndpoint) {

        return new MoviesActivityPresenter(movieParameterCreator, moviesEndpoint);
    }

}
