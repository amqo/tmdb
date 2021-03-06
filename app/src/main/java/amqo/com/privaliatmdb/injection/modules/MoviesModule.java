package amqo.com.privaliatmdb.injection.modules;

import android.app.Activity;
import android.content.SharedPreferences;

import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapter;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.BaseMoviesFragment;
import amqo.com.privaliatmdb.views.MoviesRecyclerViewAdapter;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;
import amqo.com.privaliatmdb.views.popular.MoviesPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class MoviesModule {

    private MoviesFragment mMoviesFragment;

    public MoviesModule(BaseMoviesFragment fragment) {
        mMoviesFragment = (MoviesFragment) fragment;
    }

    @Provides @PerFragment
    Activity providesActivity() {
        return mMoviesFragment.getActivity();
    }

    @Provides @PerFragment
    MoviesContract.View providesMoviesView() {
        return mMoviesFragment;
    }

    @Provides @PerFragment
    MoviesContract.Presenter providesMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            SharedPreferences sharedPreferences,
            MoviesContract.View moviesView) {

        return new MoviesPresenter(moviesEndpoint, moviesView, sharedPreferences);
    }

    @Provides @PerFragment
    MoviesAdapter providesMoviesAdapterView(
            MoviesContract.View moviesView,
            MoviesContract.Presenter presenter) {

        return new MoviesRecyclerViewAdapter(moviesView, presenter);
    }
}
