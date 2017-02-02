package amqo.com.privaliatmdb.injection.modules;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
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
    RecyclerView.LayoutManager providesLayoutManager(MoviesApplication context) {

        int gridColumns = context.getResources().getInteger(R.integer.grid_columns);

        RecyclerView.LayoutManager layoutManager;
        if (gridColumns <= 1) {
            layoutManager = new LinearLayoutManager(context);
            return layoutManager;
        }

        boolean isPortrait = context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;

        if (isPortrait) {
            layoutManager = new StaggeredGridLayoutManager(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS,
                    StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new GridLayoutManager(context, gridColumns);
        }

        return layoutManager;
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
