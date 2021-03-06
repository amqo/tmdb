package amqo.com.privaliatmdb.injection.modules;

import android.app.Activity;
import android.content.SharedPreferences;

import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapter;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.BaseMoviesFragment;
import amqo.com.privaliatmdb.views.MoviesRecyclerViewAdapter;
import amqo.com.privaliatmdb.views.search.SearchMoviesFragment;
import amqo.com.privaliatmdb.views.search.SearchMoviesPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class SearchMoviesModule {

    private SearchMoviesFragment mMoviesFragment;

    public SearchMoviesModule(BaseMoviesFragment fragment) {
        mMoviesFragment = (SearchMoviesFragment) fragment;
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

        return new SearchMoviesPresenter(moviesEndpoint, moviesView, sharedPreferences);
    }

    @Provides @PerFragment
    MoviesAdapter providesMoviesAdapterView(
            MoviesContract.View moviesView,
            MoviesContract.Presenter presenter) {
        return new MoviesRecyclerViewAdapter(moviesView, presenter);
    }
}
