package amqo.com.privaliatmdb.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {

    private Consumer<Movies> mMoviesConsumer;

    private final List<Movie> mValues;
    private final MoviesContract.View mMoviesView;
    private final MoviesContract.Presenter mMoviesPresenter;

    private Movies mLastReceivedMovies;

    public MoviesRecyclerViewAdapter(
            MoviesContract.View moviesView,
            MoviesContract.Presenter presenter) {

        mValues = new ArrayList<>();
        mMoviesView = moviesView;
        mMoviesPresenter = presenter;

        mMoviesConsumer = new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception {
                mLastReceivedMovies = movies;
                addMovies(movies.getMovies());
            }
        };

        mMoviesPresenter.getMovies(1, mMoviesConsumer);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Movie movie = mValues.get(position);

        holder.mItem = movie;

        holder.mTitleView.setText(movie.getTitleWithYear());
        holder.mOverView.setText(movie.getOverview());

        Glide.with(MoviesApplication.getInstance()).load(movie.getPosterPath())
                .centerCrop().crossFade().into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoviesView.onMovieInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addMovies(List<Movie> movies) {
        int previousSize = mValues.size();
        mValues.addAll(movies);
        notifyItemRangeInserted(previousSize, mValues.size());
    }

    public void getMoreMovies() {
        mMoviesPresenter.getMovies(
                mLastReceivedMovies.getPage() + 1, mMoviesConsumer);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        @BindView(R.id.image)
        public ImageView mImageView;
        @BindView(R.id.title)
        public TextView mTitleView;
        @BindView(R.id.overview)
        public TextView mOverView;

        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mOverView.getText() + "'";
        }
    }
}
