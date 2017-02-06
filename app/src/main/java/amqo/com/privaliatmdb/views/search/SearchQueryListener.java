package amqo.com.privaliatmdb.views.search;

import android.os.Handler;
import android.support.v7.widget.SearchView;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.contracts.MoviesContract;

public class SearchQueryListener implements SearchView.OnQueryTextListener {

    private final int TIMER_DELAY = 800;
    private Handler mQueryHandler = new Handler();
    private Runnable mQueryRunnable;

    private MoviesContract.PresenterSearch mMoviesPresenter;

    @Inject
    public SearchQueryListener(MoviesContract.Presenter moviesPresenter) {
        mMoviesPresenter = (MoviesContract.PresenterSearch) moviesPresenter;
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
                mMoviesPresenter.setNewQuery(newText);
                mMoviesPresenter.refreshMovies();
            }
        };

        mQueryHandler.postDelayed(mQueryRunnable, TIMER_DELAY);
        return false;
    }
}
