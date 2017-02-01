package amqo.com.privaliatmdb.views.popular;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import amqo.com.privaliatmdb.model.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.model.MoviesScrollContract;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment
        implements MoviesContract.View, MoviesScrollContract.FabView {

    @Inject MoviesContract.PresenterPopular mMoviesPresenter;
    @Inject RecyclerView.LayoutManager mLayoutManager;
    @Inject MoviesAdapterContract.View mMoviesAdapter;

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject MoviesScrollListener mScrollListener;

    @BindView(R.id.list_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.up_fab)
    FloatingActionButton mUpFAB;

    private boolean mIsLoading = false;
    private boolean mIsRefreshing = false;

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

        mMoviesPresenter.getMovies(1);

        return view;
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
        int lastPageLoaded = mMoviesAdapter.getLastPageLoaded();
        mMoviesPresenter.getMovies(lastPageLoaded + 1);
    }

    @Override
    public boolean isInLastPage() {
        return mMoviesAdapter.isInLastPage();
    }

    @Override
    public void onMovieInteraction(Movie movie) {

    }

    @Override
    public int getScreenDensity() {
        return ScreenHelper.getScreenDensity(getActivity());
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
                        refreshMovies();
                    }
                });
    }
}
