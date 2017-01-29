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

//    private MoviesScrollListener mScrollListener;
    private RecyclerView mRecyclerView;
    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;

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

            RecyclerView.LayoutManager layoutManager;
            if (mColumnCount <= 1) {
                layoutManager = new LinearLayoutManager(context);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(context, mColumnCount);
                mRecyclerView.setLayoutManager(layoutManager);
            }

            mRecyclerView.setNestedScrollingEnabled(false);
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
