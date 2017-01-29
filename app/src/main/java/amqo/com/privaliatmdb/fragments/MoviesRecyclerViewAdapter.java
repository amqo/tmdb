package amqo.com.privaliatmdb.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.fragments.MoviesFragment.OnMoviesInteractionListener;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {

    private Consumer<Movies> mMoviesConsumer;

    private final List<Movie> mValues;
    private final OnMoviesInteractionListener mListener;
    private final OnConsumeMoviesListener mConsumerListener;

    private Movies mLastReceivedMovies;

    public MoviesRecyclerViewAdapter(
            List<Movie> items,
            OnMoviesInteractionListener listener,
            OnConsumeMoviesListener consumerListener) {

        mValues = items;
        mListener = listener;
        mConsumerListener = consumerListener;

        mMoviesConsumer = new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception {
                mLastReceivedMovies = movies;
                mConsumerListener.onMoviesReceived();
                addMovies(movies.getMovies());
            }
        };

        mConsumerListener.getMovies(1, mMoviesConsumer);
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

        holder.mTitleView.setText(movie.getTitle());
        holder.mOverView.setText(movie.getOverview());
        holder.mYearView.setText(movie.getReleaseYear());

        Glide.with(MoviesApplication.getInstance()).load(movie.getPosterPath())
                .centerCrop().crossFade().into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onMovieInteraction(holder.mItem);
                }
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
        mConsumerListener.getMovies(
                mLastReceivedMovies.getPage() + 1, mMoviesConsumer);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        @BindView(R.id.image)
        public ImageView mImageView;
        @BindView(R.id.title)
        public TextView mTitleView;
        @BindView(R.id.year)
        public TextView mYearView;
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

    public interface OnConsumeMoviesListener {
        void getMovies(int page, Consumer<Movies> consumer);
        void onMoviesReceived();
    }
}
