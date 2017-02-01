package amqo.com.privaliatmdb.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.MoviesScrollContract;

public class BaseScrollListener extends RecyclerView.OnScrollListener {

    protected final MoviesScrollContract.View mMoviesScrollView;
    protected final int THRESHOLD;

    @Inject
    public BaseScrollListener(
            MoviesApplication context,
            MoviesScrollContract.View moviesScrollView) {

        THRESHOLD = context.getResources().getInteger(R.integer.grid_columns) * 2;

        mMoviesScrollView = moviesScrollView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mMoviesScrollView.isLoading() || mMoviesScrollView.isInLastPage())
            return;

        RecyclerView.LayoutManager layoutManager = mMoviesScrollView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        int pastVisibleItems = getPastVisibleItems();

        if (pastVisibleItems + visibleItemCount >= totalItemCount - THRESHOLD) {
            mMoviesScrollView.loadMoreMovies();
        }
    }

    protected int getPastVisibleItems() {
        RecyclerView.LayoutManager layoutManager = mMoviesScrollView.getLayoutManager();

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
