package amqo.com.privaliatmdb.views.popular;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

import amqo.com.privaliatmdb.model.MoviesScrollContract;

public class MoviesScrollListener extends RecyclerView.OnScrollListener {

    private final int THRESHOLD = 2;
    private final int TIMER_DELAY = 500;

    private Timer mShowFabTimer;

    private boolean mScrollingUp = false;

    private MoviesScrollContract.View mMoviesScrollView;

    public MoviesScrollListener(
            MoviesScrollContract.View moviesScrollView) {
        mMoviesScrollView = moviesScrollView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mMoviesScrollView.isLoading())
            return;

        RecyclerView.LayoutManager layoutManager = mMoviesScrollView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        int pastVisibleItems = 0;
        if (layoutManager instanceof LinearLayoutManager)
            pastVisibleItems = ((LinearLayoutManager)layoutManager)
                    .findFirstVisibleItemPosition();
        if (layoutManager instanceof GridLayoutManager)
            pastVisibleItems = ((GridLayoutManager)layoutManager)
                    .findFirstVisibleItemPosition();

        if (pastVisibleItems + visibleItemCount >= totalItemCount - THRESHOLD) {
            mMoviesScrollView.loadMoreMovies();
        }

        manageFABVisibility(dy, pastVisibleItems);
    }

    private void manageFABVisibility(int dy, final int pastItems) {

        boolean wasScrollingUp = mScrollingUp;
        if (dy != 0) mScrollingUp = dy < 0;

        // If it was a change in scrolling direction, then cancel it,
        // to avoid showing and hiding FAB too frecuently
        if (mShowFabTimer != null && wasScrollingUp != mScrollingUp) {
            mShowFabTimer.cancel();
        }

        // If FAB is visible and is scrolling down,
        // or reach a top item position, make FAB gone automatically
        if ((!mScrollingUp || pastItems < THRESHOLD + 1) &&
                mMoviesScrollView.isUpFABVisible()) {
            mMoviesScrollView.setShownUpFAB(false);
        }

        // If no change in scroll direction, keep FAB visibility
        if (wasScrollingUp == mScrollingUp) return;

        mShowFabTimer = new Timer();
        mShowFabTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If scrolling up and FAB is not already visible, and not in top item position,
                // then make FAB visible, unless a change in scroll direction occurs before timer delay
                if (mScrollingUp && !mMoviesScrollView.isUpFABVisible() && pastItems > THRESHOLD) {
                    mMoviesScrollView.setShownUpFAB(true);
                }
            }
        }, TIMER_DELAY);
    }
}
