package amqo.com.privaliatmdb.views;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.model.MoviesContext;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapter;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.receivers.ConnectivityNotifier;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseMoviesFragment extends Fragment
    implements MoviesContract.View {

    @Inject protected MoviesAdapter mMoviesAdapter;
    @Inject protected ConnectivityNotifier mConnectivityNotifier;
    @Inject protected ScreenHelper mScreenHelper;
    @Inject protected MoviesContract.Presenter mMoviesPresenter;

    @BindView(R.id.list_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    protected RecyclerView mRecyclerView;

    protected boolean mIsLoading = false;
    protected boolean mIsRefreshing = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean connected = mConnectivityNotifier.isConnected();
        mMoviesPresenter.onNetworkConnectionChanged(connected);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean connected = mConnectivityNotifier.isConnected();
        mMoviesPresenter.onNetworkConnectionChanged(connected);
    }

    // MoviesContract.View methods

    @Override
    public void setLoading(boolean loading) {

        if (mIsLoading == loading) return;
        mIsLoading = loading;
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(mIsLoading);
            }
        });
    }
    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }


    @Override
    public String getCorrectImageSize(MoviesConfiguration moviesConfiguration) {
        return mScreenHelper.getCorrectImageSize(moviesConfiguration);
    }

    @Override
    public void onMovieInteraction(Movie movie) {
        movieInteraction(movie);
    }

    @Override
    public void onMoviesLoaded(Movies movies) {

        if (mIsRefreshing) {
            mIsRefreshing = false;
            mMoviesAdapter.refreshMovies(movies);
        } else mMoviesAdapter.addMovies(movies);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void clearMovies() {
        mMoviesAdapter.refreshMovies(new Movies());
    }

    @Override
    public MoviesContext getMoviesContext() {

        MoviesContext snackData = new MoviesContext();
        snackData.context = getContext();
        snackData.view = getView();
        return snackData;
    }

    // The view here was inflated by the child fragment
    // Here it's used only to bind with ButterKnife the common views
    protected void bindViews(View view) {

        ButterKnife.bind(this, view);

        MoviesApplication.getInstance().getActiveComponent().inject(this);

        initRecyclerView();

        initOtherViews();
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(getCustomLayoutManager());
        mRecyclerView.setAdapter((RecyclerView.Adapter) mMoviesAdapter);
    }

    private void initOtherViews() {

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (!mConnectivityNotifier.isConnected())
                            mSwipeRefreshLayout.setRefreshing(false);
                        else refreshMovies();
                    }
                });
    }

    private RecyclerView.LayoutManager getCustomLayoutManager() {
        int gridColumns = getResources().getInteger(R.integer.grid_columns);

        RecyclerView.LayoutManager layoutManager;
        if (gridColumns <= 1) {
            layoutManager = new LinearLayoutManager(getContext());
            return layoutManager;
        }

        boolean isPortrait = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;

        if (isPortrait) {
            layoutManager = new StaggeredGridLayoutManager(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS,
                    StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new GridLayoutManager(getContext(), gridColumns);
        }
        return layoutManager;
    }

    protected abstract void movieInteraction(Movie movie);
    protected abstract void refreshMovies();
}
