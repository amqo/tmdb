package amqo.com.privaliatmdb.injection.modules;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.model.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.popular.MoviesRecyclerViewAdapter;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MoviesModule {

    private int mColumnCount = 1;

    @Provides @PerFragment
    MoviesEndpoint providesMoviesEndpoint(Retrofit retrofit) {
        return retrofit.create(MoviesEndpoint.class);
    }

    @Provides @PerFragment
    RecyclerView.LayoutManager providesLayoutManager(MoviesApplication context) {

        RecyclerView.LayoutManager layoutManager;
        if (mColumnCount <= 1)
            layoutManager = new LinearLayoutManager(context);
        else layoutManager = new GridLayoutManager(context, mColumnCount);

        return layoutManager;
    }

    @Provides @PerFragment
    MoviesAdapterContract.View providesMoviesAdapter(
            MoviesContract.View moviesView,
            MoviesContract.Presenter moviesPresenter) {

        return new MoviesRecyclerViewAdapter(moviesView, moviesPresenter);
    }

}
