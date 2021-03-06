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
import amqo.com.privaliatmdb.views.BaseMoviesFragment;

public class MoviesFragment extends BaseMoviesFragment {

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject MoviesScrollListener mScrollListener;
    @Inject FabUpView mFabUpView;

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

        MoviesApplication.getInstance().getActiveComponent().inject(this);

        mUpFAB = (FloatingActionButton) view.findViewById(R.id.up_fab);
        mFabUpView.setUpFab(mUpFAB);

        mScrollListener.setFabUpView(mFabUpView);

        mRecyclerView.addOnScrollListener(mScrollListener);

        refreshMovies();

        return view;
    }

    // Parent abstract methods

    @Override
    protected void movieInteraction(Movie movie) {

    }

    @Override
    protected void refreshMovies() {
        if (mConnectivityNotifier.isConnected()) {
            mMoviesPresenter.refreshMovies();
        }
    }
}
