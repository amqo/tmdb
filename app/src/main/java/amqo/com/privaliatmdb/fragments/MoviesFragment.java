package amqo.com.privaliatmdb.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesActivityPresenter;
import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import io.reactivex.functions.Consumer;

public class MoviesFragment extends Fragment {

    @Inject
    MoviesActivityPresenter mMoviesController;

    private RecyclerView mRecyclerView;
    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;
    private boolean mIsLoading = false;

    private int mColumnCount = 1;
    private OnMoviesInteractionListener mListener;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            mRecyclerView = (RecyclerView) view;

            final RecyclerView.LayoutManager layoutManager;
            if (mColumnCount <= 1) {
                layoutManager = new LinearLayoutManager(context);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(context, mColumnCount);
                mRecyclerView.setLayoutManager(layoutManager);
            }

            RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mIsLoading)
                        return;
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();

                    int pastVisibleItems = 0;
                    if (layoutManager instanceof LinearLayoutManager)
                        pastVisibleItems = ((LinearLayoutManager)layoutManager)
                                .findFirstVisibleItemPosition();
                    if (layoutManager instanceof GridLayoutManager)
                        pastVisibleItems = ((GridLayoutManager)layoutManager)
                                .findFirstVisibleItemPosition();

                    if (pastVisibleItems + visibleItemCount >= totalItemCount - 2) {
                        mMoviesRecyclerViewAdapter.getMoreMovies();
                        mIsLoading = true;
                    }
                }
            };
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MoviesApplication.getInstance().getMainActivityComponent().inject(this);

        mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(new ArrayList<Movie>(), mListener,
                new MoviesRecyclerViewAdapter.OnConsumeMoviesListener() {
                    @Override
                    public void getMovies(int page, Consumer<Movies> consumer) {
                        mMoviesController.getMovies(page, consumer);
                    }
                    @Override
                    public void onMoviesReceived() {
                        mIsLoading = false;
                    }
                });
        mRecyclerView.setAdapter(mMoviesRecyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoviesInteractionListener) {
            mListener = (OnMoviesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMoviesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMoviesInteractionListener {
        void onMovieInteraction(Movie item);
    }
}
