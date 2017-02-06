package amqo.com.privaliatmdb.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.views.viewHolders.MovieItemViewHolder;

public class MoviesRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieItemViewHolder>
        implements MoviesAdapterContract {

    private final List<Movie> mValues;
    private final MoviesContract.View mMoviesView;
    private final MoviesContract.Presenter mMoviesPresenter;

    private int mImageWidth = 0;
    private int mImageHeight = 0;

    private Map<ImageView, View.OnLayoutChangeListener>
            mCurrentLayoutListeners = new HashMap<>();

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

        holder.mItem = movie;

        holder.mTitleView.setText(movie.getTitle());
        holder.mYearView.setText(movie.getReleaseYear());
        holder.mTitleRankView.setText(Integer.toString(position + 1));
        holder.mOverView.setText(movie.getOverview());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoviesView.onMovieInteraction(holder.mItem);
            }
        });

        loadImageForMovie(holder, movie);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // MoviesAdapterContract.View methods

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

    private void loadImageForMovie(final MovieItemViewHolder holder, final Movie movie) {

        String movieImagesBaseUrl = mMoviesPresenter.getMovieImagesBaseUrl();
        if (TextUtils.isEmpty(movieImagesBaseUrl)) return;

        String moviePosterPath = movie.getPosterPath(movieImagesBaseUrl);
        if (TextUtils.isEmpty(moviePosterPath)) {
            // This can happen when a movie does not have an image associated
            holder.mImageContainerView.setVisibility(View.GONE);
            return;
        }

        RequestListener<String, GlideDrawable> requestListener =
                getDrawableRequestListener(holder, movie);

        setHolderImageLayout(holder);

        holder.mRatingMovieView.setVisibility(View.GONE);

        DrawableRequestBuilder builder = Glide.with(MoviesApplication.getInstance())
                .load(moviePosterPath)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(requestListener);

        builder.placeholder(R.drawable.cinema_placeholder);

        builder.into(holder.mImageView);
    }

    private void setHolderImageLayout(MovieItemViewHolder holder) {
        boolean auto = mImageWidth == 0 || mImageHeight == 0;
        if (auto) {
            // Add layout change listener only if a previous listener didn't fire before
            View.OnLayoutChangeListener layoutChangeListener = getOnLayoutChangeListener();

            mCurrentLayoutListeners.put(holder.mImageView, layoutChangeListener);
            holder.mImageView.addOnLayoutChangeListener(layoutChangeListener);
        } else {
            // If we got the width and height of images, we force the layout to avoid "jumps"
            ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();
            params.height = mImageHeight;
            params.width = mImageWidth;
            holder.mImageView.setLayoutParams(params);
        }
    }

    @NonNull
    private View.OnLayoutChangeListener getOnLayoutChangeListener() {
        return new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int width, int height, int i4, int i5, int i6, int i7) {
                        if (width > 0 && height > 0) {
                            float ratio = (float)height / width;
                            // To understand this constants, please check https://www.themoviedb.org/documentation/editing/images
                            if (ratio < 1.60 && ratio > 1.40) {
                                mImageWidth = width;
                                mImageHeight = height;

                                // Ass we already have what we wanted, we can remove all listeners
                                for (ImageView imageView : mCurrentLayoutListeners.keySet()) {
                                    imageView.removeOnLayoutChangeListener(mCurrentLayoutListeners.get(imageView));
                                }
                                mCurrentLayoutListeners.clear();
                            }
                        }
                    }
                };
    }

    @NonNull
    private RequestListener<String, GlideDrawable> getDrawableRequestListener(
            final MovieItemViewHolder holder, final Movie movie) {

        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(
                    Exception e, String model, Target<GlideDrawable> target,
                    boolean isFirstResource) {
                holder.mImageContainerView.setVisibility(View.GONE);
                // Get configuration again, as this error should occur if configuration changed in the API
                mMoviesPresenter.updateMoviesConfiguration();
                return false;
            }

            @Override
            public boolean onResourceReady(
                    GlideDrawable resource, String model,
                    Target<GlideDrawable> target, boolean isFromMemoryCache,
                    boolean isFirstResource) {
                holder.mImageContainerView.setVisibility(View.VISIBLE);
                float rating = movie.getVoteAverage();
                // Show only movies with rating, 0.0 meaning no ratings (most of the times)
                if (rating != 0.0) {
                    //Show decimals only when they are different than 0
                    String rating_s = rating == Math.ceil(rating) ?
                            Integer.toString((int)rating) : Float.toString(rating);
                    holder.mRatingMovieView.setRating(rating_s);
                    holder.mRatingMovieView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        };
    }
}
