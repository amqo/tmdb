package amqo.com.privaliatmdb.views.popular;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesFabUp;
import amqo.com.privaliatmdb.views.BaseScrollListener;

public class MoviesScrollListener extends BaseScrollListener {

    private final int TIMER_DELAY = 800;

    private Handler mShowFabHandler = new Handler();
    private Runnable mShowFabRunnable;

    private boolean mScrollingUp = false;

    private MoviesFabUp mMoviesFabUpView;

    @Inject
    public MoviesScrollListener(
            MoviesApplication context,
            MoviesContract.View moviesView,
            MoviesContract.Presenter moviesPresenter) {

        super(context, moviesView, moviesPresenter);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {

        super.onScrolled(recyclerView, dx, dy);

        if (mMoviesView.isLoading())
            return;

        int pastVisibleItems = getPastVisibleItems();
        if (pastVisibleItems <= THRESHOLD) {
            mShowFabHandler.removeCallbacks(mShowFabRunnable);
            mMoviesFabUpView.setShownUpFAB(false);
        } else manageFABVisibility(dy, getPastVisibleItems());
    }

    public void setFabUpView(MoviesFabUp moviesFabUpView) {
        mMoviesFabUpView = moviesFabUpView;
    }

    private void manageFABVisibility(int dy, final int pastItems) {

        boolean wasScrollingUp = mScrollingUp;
        if (dy != 0) mScrollingUp = dy < 0;

        // If it was a change in scrolling direction, then cancel it,
        // to avoid showing and hiding FAB too frecuently
        if (mShowFabRunnable != null && wasScrollingUp != mScrollingUp) {
            mShowFabHandler.removeCallbacks(mShowFabRunnable);
        }

        // If FAB is visible and is scrolling down,
        // or reach a top item position, make FAB gone automatically
        if ((!mScrollingUp || pastItems < THRESHOLD + 1) &&
                mMoviesFabUpView.isUpFABVisible()) {
            mMoviesFabUpView.setShownUpFAB(false);
        }

        // If no change in scroll direction, keep FAB visibility
        if (wasScrollingUp == mScrollingUp) return;

        mShowFabRunnable = new Runnable() {
            @Override
            public void run() {
                // If scrolling up and FAB is not already visible, and not in top item position,
                // then make FAB visible, unless a change in scroll direction occurs before timer delay
                if (mScrollingUp && !mMoviesFabUpView.isUpFABVisible() && pastItems > THRESHOLD) {
                    mMoviesFabUpView.setShownUpFAB(true);
                }
            }
        };
        mShowFabHandler.postDelayed(mShowFabRunnable, TIMER_DELAY);
    }
}
