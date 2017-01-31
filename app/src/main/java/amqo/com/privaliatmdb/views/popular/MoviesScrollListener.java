package amqo.com.privaliatmdb.views.popular;

import android.support.v7.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.model.MoviesScrollContract;
import amqo.com.privaliatmdb.views.BaseScrollListener;

public class MoviesScrollListener extends BaseScrollListener {

    private final int TIMER_DELAY = 500;

    private Timer mShowFabTimer;

    private boolean mScrollingUp = false;

    private final MoviesScrollContract.FabView mMoviesScrollFabView;

    @Inject
    public MoviesScrollListener(
            MoviesApplication context,
            MoviesScrollContract.FabView moviesScrollView) {

        super(context, moviesScrollView);

        mMoviesScrollFabView = moviesScrollView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {

        super.onScrolled(recyclerView, dx, dy);

        if (mMoviesScrollView.isLoading())
            return;

        manageFABVisibility(dy, getPastVisibleItems());
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
                mMoviesScrollFabView.isUpFABVisible()) {
            mMoviesScrollFabView.setShownUpFAB(false);
        }

        // If no change in scroll direction, keep FAB visibility
        if (wasScrollingUp == mScrollingUp) return;

        mShowFabTimer = new Timer();
        mShowFabTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If scrolling up and FAB is not already visible, and not in top item position,
                // then make FAB visible, unless a change in scroll direction occurs before timer delay
                if (mScrollingUp && !mMoviesScrollFabView.isUpFABVisible() && pastItems > THRESHOLD) {
                    mMoviesScrollFabView.setShownUpFAB(true);
                }
            }
        }, TIMER_DELAY);
    }
}
