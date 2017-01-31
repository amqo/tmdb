package amqo.com.privaliatmdb.injection.modules;

import android.content.SharedPreferences;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.model.MoviesScrollContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.MoviesPresenter;
import amqo.com.privaliatmdb.views.search.SearchMoviesFragment;
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
        if (gridColumns <= 1)
            layoutManager = new LinearLayoutManager(context);
        else layoutManager = new GridLayoutManager(context, gridColumns);

        return layoutManager;
    }

    @Provides @PerFragment
    MoviesContract.View providesMoviesView() {
        return mMoviesFragment;
    }

    @Provides @PerFragment
    MoviesScrollContract.View providesMoviesScrollView() {
        return mMoviesFragment;
    }

    @Provides @PerFragment
    MoviesContract.Presenter providesMoviesPresenter(
            MoviesEndpoint moviesEndpoint,
            SharedPreferences sharedPreferences,
            MoviesContract.View moviesView) {

        return new MoviesPresenter(moviesEndpoint, moviesView, sharedPreferences);
    }
}
