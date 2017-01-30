package amqo.com.privaliatmdb.injection.modules;

import android.content.SharedPreferences;

import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.MainActivity;
import amqo.com.privaliatmdb.views.MoviesPresenter;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    MainActivity mMainActivity;

    public MainActivityModule(MainActivity activity) {
        mMainActivity = activity;
    }

    @Provides @PerFragment
    MoviesContract.View providesMoviesView() {
        MoviesFragment moviesFragment = (MoviesFragment)
                mMainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment);
        return moviesFragment;
    }
    @Provides @PerFragment
    MoviesContract.Presenter provicesMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            SharedPreferences sharedPreferences,
            MoviesContract.View moviesView) {

        return new MoviesPresenter(moviesEndpoint, moviesView, sharedPreferences);
    }



}
