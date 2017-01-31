package amqo.com.privaliatmdb.views.popular;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.model.MoviesScrollContract;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment
        implements MoviesContract.View, MoviesScrollContract.View {

    @Inject MoviesContract.Presenter mMoviesPresenter;
    @Inject RecyclerView.LayoutManager mLayoutManager;
    @Inject MoviesAdapterContract.View mMoviesAdapter;

    @BindView(R.id.list_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.up_fab)
    FloatingActionButton mUpFAB;

    private boolean mIsLoading = false;
    private boolean mIsRefreshing = false;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MoviesApplication.getInstance().getMainActivityComponent().inject(this);

        initRecyclerView();

        initOtherViews();

        mRecyclerView.setAdapter((RecyclerView.Adapter) mMoviesAdapter);
        mMoviesPresenter.getMovies(1);
    }

    @Override
    public void refreshMovies() {
        setLoading(true);
        mIsRefreshing = true;
        mMoviesPresenter.getMovies(1);
    }

    @Override
    public void setLoading(boolean loading) {
        mIsLoading = loading;
        if (getActivity() == null) return;
        if (getActivity()!= null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(mIsLoading);
                }
            });
        }
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void loadMoreMovies() {
        int lastPageLoaded = mMoviesAdapter.getLastPageLoaded();
        mMoviesPresenter.getMovies(lastPageLoaded + 1);
    }

    @Override
    public void onMovieInteraction(Movie movie) {

    }

    @Override
    public int getScreenDensity() {
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return (int)metrics.xdpi;
    }

    @Override
    public void onMoviesLoaded(Movies movies) {
        if (mIsRefreshing) {
            mIsRefreshing = false;
            mMoviesAdapter.refreshMovies(movies);
        } else mMoviesAdapter.addMovies(movies);
    }

    @Override
    public void setUpFABVisibility(final int visibility) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUpFAB.setVisibility(visibility);
            }
        });
    }

    @Override
    public boolean isUpFABVisible() {
        return mUpFAB.getVisibility() == View.VISIBLE;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.OnScrollListener mScrollListener =
                new MoviesScrollListener(this);
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    private void initOtherViews() {
        mUpFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpFAB.setVisibility(View.GONE);
                mRecyclerView.scrollToPosition(0);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshMovies();
                    }
                });
    }
}
