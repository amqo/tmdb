package amqo.com.privaliatmdb.views.popular;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MoviesFragment extends Fragment
        implements MoviesContract.View,
        MoviesScrollContract.FabView,
        ConnectivityReceiverContract.View {

    @Inject MoviesContract.PresenterPopular mMoviesPresenter;
    @Inject RecyclerView.LayoutManager mLayoutManager;
    @Inject MoviesAdapterContract.View mMoviesAdapter;

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject MoviesScrollListener mScrollListener;
    @Inject ConnectivityNotifier mConnectivityNotifier;

    @BindView(R.id.list_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.up_fab)
    FloatingActionButton mUpFAB;

    private boolean mIsLoading = false;
    private boolean mIsRefreshing = false;
    private boolean mNeedRefresh = false;

    private Snackbar mConnectivitySnackbar;

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        MoviesApplication.getInstance().getMoviesComponent().inject(this);

        initRecyclerView();

        initOtherViews();

        if(mConnectivityNotifier.isConnected()) {
            mMoviesPresenter.getMovies(1);
        }

        return view;
    }

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

    @Override
    public void refreshMovies() {
        if(!mConnectivityNotifier.isConnected()) return;
        setLoading(true);
        mIsRefreshing = true;
        mMoviesPresenter.getMovies(1);
    }

    @Override
    public void setLoading(boolean loading) {
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
    public void loadMoreMovies() {
        if(!mConnectivityNotifier.isConnected()) return;
        int lastPageLoaded = mMoviesAdapter.getLastPageLoaded();
        mMoviesPresenter.getMovies(lastPageLoaded + 1);
    }

    @Override
    public boolean isInLastPage() {
        return mMoviesAdapter.isInLastPage();
    }

    @Override
    public int getScreenDensity() {
        return ScreenHelper.getScreenDensity(getActivity());
    }

    @Override
    public void onMovieInteraction(Movie movie) {

    }

    @Override
    public void onMoviesLoaded(Movies movies) {
        if (mIsRefreshing) {
            mIsRefreshing = false;
            mMoviesAdapter.refreshMovies(movies);
        } else mMoviesAdapter.addMovies(movies);
    }

    @Override
    public void setShownUpFAB(final boolean show) {
        if (show) mUpFAB.show();
        else mUpFAB.hide();
    }

    @Override
    public boolean isUpFABVisible() {
        return mUpFAB.isShown();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

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

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRecyclerView.setAdapter((RecyclerView.Adapter) mMoviesAdapter);
    }

    private void initOtherViews() {
        mUpFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpFAB.hide();
                mRecyclerView.scrollToPosition(0);
            }
        });

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
}
