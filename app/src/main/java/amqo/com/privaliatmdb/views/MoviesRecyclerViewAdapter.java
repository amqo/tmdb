package amqo.com.privaliatmdb.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapter;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;

public class MoviesRecyclerViewAdapter
        extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieItemViewHolder>
        implements MoviesAdapter {

    private final List<Movie> mValues;
    private final MoviesContract.View mMoviesView;
    private final MoviesContract.Presenter mMoviesPresenter;

    public MoviesRecyclerViewAdapter(
            MoviesContract.View moviesView,
            MoviesContract.Presenter presenter) {

        mValues = new ArrayList<>();
        mMoviesView = moviesView;
        mMoviesPresenter = presenter;
    }

    // RecyclerView.Adapter<MovieItemViewHolder> methods

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieItemViewHolder holder, int position) {

        Movie movie = mValues.get(position);

        holder.mView.bindViewHolder(movie, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // MoviesAdapter.View methods

    @Override
    public void refreshMovies(Movies movies) {
        int previousSize = mValues.size();
        mValues.clear();
        if (previousSize > 0) notifyItemRangeRemoved(0, previousSize);
        if (movies == null || movies.getMovies().isEmpty()) return;
        addMovies(movies);
    }

    @Override
    public void addMovies(Movies movies) {
        if (movies == null || movies.getMovies().isEmpty()) return;
        int previousSize = mValues.size();
        mValues.addAll(movies.getMovies());
        notifyItemRangeInserted(previousSize, mValues.size());
    }

    protected class MovieItemViewHolder extends RecyclerView.ViewHolder {

        protected final MovieItemView mView;

        public MovieItemViewHolder(View view) {
            super(view);
            mView = new MovieItemView(view, mMoviesView, mMoviesPresenter);
        }
    }

}
