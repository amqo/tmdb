package amqo.com.privaliatmdb.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesScrollContract;

public class BaseScrollListener extends RecyclerView.OnScrollListener {

    protected final MoviesScrollContract mMoviesScroll;
    protected final MoviesContract.Presenter mMoviesPresenter;
    protected final int THRESHOLD;

    @Inject
    public BaseScrollListener(
            MoviesApplication context,
            MoviesScrollContract moviesScroll,
            MoviesContract.Presenter moviesPresenter) {

        THRESHOLD = context.getResources().getInteger(R.integer.grid_columns) * 2;

        mMoviesScroll = moviesScroll;
        mMoviesPresenter = moviesPresenter;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mMoviesScroll.isLoading())
            return;

        RecyclerView.LayoutManager layoutManager = mMoviesScroll.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        int pastVisibleItems = getPastVisibleItems(layoutManager);

        if (pastVisibleItems + visibleItemCount >= totalItemCount - THRESHOLD) {
            mMoviesPresenter.loadMoreMovies();
        }
    }

    protected int getPastVisibleItems() {
        RecyclerView.LayoutManager layoutManager = mMoviesScroll.getLayoutManager();
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
