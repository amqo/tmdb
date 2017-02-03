package amqo.com.privaliatmdb.injection.modules;

import android.content.SharedPreferences;

import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.contracts.ConnectivityReceiverContract;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesScrollContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.MoviesRecyclerViewAdapter;
import amqo.com.privaliatmdb.views.search.SearchMoviesFragment;
import amqo.com.privaliatmdb.views.search.SearchMoviesPresenter;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SearchMoviesModule {

    private SearchMoviesFragment mMoviesFragment;

    public SearchMoviesModule(SearchMoviesFragment fragment) {
        mMoviesFragment = fragment;
    }

    @Provides @PerFragment
    MoviesEndpoint providesMoviesEndpoint(Retrofit retrofit) {
        return retrofit.create(MoviesEndpoint.class);
    }

    @Provides @PerFragment
    MoviesContract.ViewSearch providesMoviesView() {
        return mMoviesFragment;
    }

    @Provides @PerFragment
    MoviesScrollContract.View providesMoviesScrollView() {
        return mMoviesFragment;
    }

    @Provides @PerFragment
    ConnectivityReceiverContract.View providesConnectivityView() {
        return mMoviesFragment;
    }

    @Provides @PerFragment
    MoviesContract.PresenterSearch providesMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            SharedPreferences sharedPreferences,
            MoviesContract.ViewSearch moviesView) {

        return new SearchMoviesPresenter(moviesEndpoint, moviesView, sharedPreferences);
    }

    @Provides @PerFragment
    MoviesAdapterContract.View providesMoviesAdapterView(
            MoviesContract.ViewSearch moviesView,
            MoviesContract.PresenterSearch presenter) {
        return new MoviesRecyclerViewAdapter(moviesView, presenter);
    }
}
