package amqo.com.privaliatmdb.injection.modules;

import javax.inject.Named;

import amqo.com.privaliatmdb.MainActivity;
import amqo.com.privaliatmdb.MoviesPresenter;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.fragments.MoviesFragment;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    MainActivity mMainActivity;

    public MainActivityModule(MainActivity activity) {
        mMainActivity = activity;
    }

    @Provides @PerFragment @Named("activity")
    MoviesContract.View providesMainActivity() {
        return mMainActivity;
    }

    @Provides @PerFragment @Named("fragment")
    MoviesContract.View providesMoviesView() {
        MoviesFragment moviesFragment = (MoviesFragment)
                mMainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment);
        return moviesFragment;
    }

    @Provides @PerFragment
    MoviesContract.Presenter provicesMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            MovieParameterCreator parameterCreator,
            @Named("fragment") MoviesContract.View moviesView) {

         return new MoviesPresenter(parameterCreator, moviesEndpoint, moviesView);
    }


}
