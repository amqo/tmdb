package amqo.com.privaliatmdb.views.search;

import android.os.Handler;
import android.support.v7.widget.SearchView;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.MoviesContract;

public class SearchQueryListener implements SearchView.OnQueryTextListener {

    private final int TIMER_DELAY = 600;
    private Handler mQueryHandler = new Handler();
    private Runnable mQueryRunnable;

    private MoviesContract.ViewSearch mMoviesView;

    @Inject
    public SearchQueryListener(MoviesContract.ViewSearch moviesView) {
        mMoviesView = moviesView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (mQueryRunnable != null) {
            mQueryHandler.removeCallbacks(mQueryRunnable);
        }

        mQueryRunnable = new Runnable() {
            @Override
            public void run() {
                mMoviesView.refreshMovies(newText);
            }
        };

        mQueryHandler.postDelayed(mQueryRunnable, TIMER_DELAY);
        return false;
    }
}
