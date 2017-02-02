package amqo.com.privaliatmdb.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.ConnectivityReceiverContract;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesScrollContract;
import amqo.com.privaliatmdb.receivers.ConnectivityNotifier;
import amqo.com.privaliatmdb.views.utils.NotificationsHelper;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseMoviesFragment extends Fragment
    implements MoviesScrollContract.View,
        ConnectivityReceiverContract.View,
        MoviesContract.View {

    @Inject protected RecyclerView.LayoutManager mLayoutManager;
    @Inject protected MoviesAdapterContract.View mMoviesAdapter;
    @Inject protected ConnectivityNotifier mConnectivityNotifier;

    @BindView(R.id.list_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    protected RecyclerView mRecyclerView;

    protected boolean mIsLoading = false;
    protected boolean mIsRefreshing = false;
    protected boolean mNeedRefresh = false;

    protected Snackbar mConnectivitySnackbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mNeedRefresh)
            mConnectivitySnackbar = NotificationsHelper
                    .showSnackConnectivity(getContext(), getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean connected = mConnectivityNotifier.isConnected();
        onNetworkConnectionChanged(connected);
    }

    // MoviesContract.View methods

    @Override
    public void refreshMovies() {
        if (!mConnectivityNotifier.isConnected()) return;
        resetMovies();
    }

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
    public int getScreenDensity() {
        return ScreenHelper.getScreenDensity(getActivity());
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

    // MoviesScrollContract.View methods

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void loadMoreMovies() {
        if(!mConnectivityNotifier.isConnected()) return;
        int lastPageLoaded = mMoviesAdapter.getLastPageLoaded();
        loadMoreMoviesInPage(lastPageLoaded + 1);
    }

    @Override
    public boolean isInLastPage() {
        return mMoviesAdapter.isInLastPage();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    // ConnectivityReceiverContract.View methods

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            mNeedRefresh = true;
            mConnectivitySnackbar = NotificationsHelper
                    .showSnackConnectivity(getContext(), getView());
        } else {
            if (mConnectivitySnackbar != null) {
                mConnectivitySnackbar.dismiss();
                mConnectivitySnackbar = null;
            }
            if (mNeedRefresh) {
                mNeedRefresh = false;
                refreshMovies();
            }
        }
    }

    // The view here was inflated by the child fragment
    // Here it's used only to bind with ButterKnife the common views
    protected void bindViews(View view) {

        ButterKnife.bind(this, view);

        MoviesApplication.getInstance().getCurrentMoviesComponent().inject(this);

        initRecyclerView();

        initOtherViews();
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(mLayoutManager);
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

    protected abstract void resetMovies();
    protected abstract void movieInteraction(Movie movie);
    protected abstract void loadMoreMoviesInPage(int page);
}