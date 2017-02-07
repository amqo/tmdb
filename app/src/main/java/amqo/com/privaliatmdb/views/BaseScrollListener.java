package amqo.com.privaliatmdb.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;

public class BaseScrollListener extends RecyclerView.OnScrollListener {

    protected final MoviesContract.View mMoviesView;
    protected final MoviesContract.Presenter mMoviesPresenter;
    protected final int THRESHOLD;

    @Inject
    public BaseScrollListener(
            MoviesApplication context,
            MoviesContract.View moviesView,
            MoviesContract.Presenter moviesPresenter) {

        THRESHOLD = context.getResources().getInteger(R.integer.grid_columns) * 2;

        mMoviesView = moviesView;
        mMoviesPresenter = moviesPresenter;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mMoviesView.isLoading())
            return;

        RecyclerView.LayoutManager layoutManager = mMoviesView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        int pastVisibleItems = getPastVisibleItems(layoutManager);

        if (pastVisibleItems + visibleItemCount >= totalItemCount - THRESHOLD) {
            mMoviesPresenter.loadMoreMovies();
        }
    }

    protected int getPastVisibleItems() {
        RecyclerView.LayoutManager layoutManager = mMoviesView.getLayoutManager();
        return getPastVisibleItems(layoutManager);
    }

    private int getPastVisibleItems(RecyclerView.LayoutManager layoutManager) {

        int pastVisibleItems = 0;

        if (layoutManager instanceof LinearLayoutManager)
            pastVisibleItems = ((LinearLayoutManager)layoutManager)
                    .findFirstVisibleItemPosition();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] visiblePositions = new int[2];
            ((StaggeredGridLayoutManager)layoutManager)
                    .findFirstVisibleItemPositions(visiblePositions);
            pastVisibleItems = visiblePositions[0];
        }

        if (layoutManager instanceof GridLayoutManager)
            pastVisibleItems = ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition();

        return pastVisibleItems;
    }
}
