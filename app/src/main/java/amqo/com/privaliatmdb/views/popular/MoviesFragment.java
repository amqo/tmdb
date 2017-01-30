package amqo.com.privaliatmdb.views.popular;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import amqo.com.privaliatmdb.model.MoviesContract;

public class MoviesFragment extends Fragment implements MoviesContract.View {

    @Inject MoviesContract.Presenter mMoviesPresenter;

    private RecyclerView mRecyclerView;
    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;
    private boolean mIsLoading = false;

    private int mColumnCount = 1;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            mRecyclerView = (RecyclerView) view;

            final RecyclerView.LayoutManager layoutManager;
            if (mColumnCount <= 1) {
                layoutManager = new LinearLayoutManager(context);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(context, mColumnCount);
                mRecyclerView.setLayoutManager(layoutManager);
            }

            RecyclerView.OnScrollListener mScrollListener =
                    new MoviesScrollListener(layoutManager);
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MoviesApplication.getInstance().getMainActivityComponent().inject(this);

        mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(
                this, mMoviesPresenter);
        mRecyclerView.setAdapter(mMoviesRecyclerViewAdapter);
    }

    @Override
    public void refreshMovies() {
        mMoviesRecyclerViewAdapter.refreshMovies();
    }

    @Override
    public void setLoading(boolean loading) {
        // TODO show or hide refresh layout
        mIsLoading = loading;
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

    private class MoviesScrollListener extends RecyclerView.OnScrollListener {

        private RecyclerView.LayoutManager layoutManager;

        public MoviesScrollListener(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mIsLoading)
                return;
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            int pastVisibleItems = 0;
            if (layoutManager instanceof LinearLayoutManager)
                pastVisibleItems = ((LinearLayoutManager)layoutManager)
                        .findFirstVisibleItemPosition();
            if (layoutManager instanceof GridLayoutManager)
                pastVisibleItems = ((GridLayoutManager)layoutManager)
                        .findFirstVisibleItemPosition();

            if (pastVisibleItems + visibleItemCount >= totalItemCount - 2) {
                mMoviesRecyclerViewAdapter.getMoreMovies();
                mIsLoading = true;
            }
        }
    }
}
