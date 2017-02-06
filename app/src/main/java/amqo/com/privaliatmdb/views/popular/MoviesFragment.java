package amqo.com.privaliatmdb.views.popular;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesFabUpContract;
import amqo.com.privaliatmdb.views.BaseMoviesFragment;

public class MoviesFragment extends BaseMoviesFragment
        implements MoviesFabUpContract.View {

    @Inject MoviesContract.PresenterPopular mMoviesPresenter;

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject MoviesScrollListener mScrollListener;
    @Inject FabUpPresenter mFabUpPresenter;

    private FloatingActionButton mUpFAB;

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        super.bindViews(view);

        MoviesApplication.getInstance().getMoviesComponent().inject(this);

        mBasePresenter = mMoviesPresenter;

        mUpFAB = (FloatingActionButton) view.findViewById(R.id.up_fab);
        mFabUpPresenter.setUpFab(mUpFAB);

        mScrollListener.setPresenter(mFabUpPresenter);

        mRecyclerView.addOnScrollListener(mScrollListener);

        if(mConnectivityNotifier.isConnected()) {
            mMoviesPresenter.getMovies(1);
        }

        return view;
    }

    // Parent abstract methods

    protected void resetMovies() {
        setLoading(true);
        mIsRefreshing = true;
        mMoviesPresenter.getMovies(1);
    }

    protected void movieInteraction(Movie movie) {

    }

    protected void loadMoreMoviesInPage(int page) {
        mMoviesPresenter.getMovies(page);
    }

    // MoviesFabUpContract.View methods

    @Override
    public void scrollUp() {
        mRecyclerView.scrollToPosition(0);
    }
}
