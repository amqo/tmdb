package amqo.com.privaliatmdb.views;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.MoviesContract;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesRecyclerViewAdapter
        extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>
        implements MoviesAdapterContract.View {

    private final List<Movie> mValues;
    private final MoviesContract.View mMoviesView;
    private final MoviesContract.Presenter mMoviesPresenter;

    private int mImageWidth = 0;
    private int mImageHeight = 0;

    private Movies mLastReceivedMovies;

    public MoviesRecyclerViewAdapter(
            MoviesContract.View moviesView,
            MoviesContract.Presenter presenter) {

        mValues = new ArrayList<>();
        mMoviesView = moviesView;
        mMoviesPresenter = presenter;
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
        int previousSize = mValues.size();
        mValues.addAll(movies.getMovies());
        notifyItemRangeInserted(previousSize, mValues.size());
        mLastReceivedMovies = movies;
    }

    @Override
    public int getLastPageLoaded() {
        return mLastReceivedMovies.getPage();
    }

    private void loadImageForMovie(final ViewHolder holder, Movie movie) {
        String movieImagesBaseUrl = mMoviesPresenter.getMovieImagesBaseUrl();
        if (TextUtils.isEmpty(movieImagesBaseUrl)) return;

        RequestListener<String, GlideDrawable> requestListener =
                new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(
                            Exception e, String model, Target<GlideDrawable> target,
                            boolean isFirstResource) {
                        holder.mImageView.setVisibility(View.GONE);
                        // Get configuration again, as this error should occur if configuration changed in the API
                        mMoviesPresenter.updateMoviesConfiguration();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            GlideDrawable resource, String model,
                            Target<GlideDrawable> target, boolean isFromMemoryCache,
                            boolean isFirstResource) {
                        return false;
                    }
                };

        boolean auto = mImageWidth == 0 || mImageHeight == 0;
        if (auto) {
            // Add layout change listener only if a previous listener didn't fire before
            holder.mImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int width, int height, int i4, int i5, int i6, int i7) {
                    if (width > 0 && height > 0) {
                        float ratio = (float)height / width;
                        // To understand this constants, please check https://www.themoviedb.org/documentation/editing/images
                        if (ratio < 1.55 && ratio > 1.45) {
                            mImageWidth = width;
                            mImageHeight = height;
                        }
                        // Ass we already have what we wanted, we can remove this listener
                        holder.mImageView.removeOnLayoutChangeListener(this);
                    }
                }
            });
        } else {
            // If we got the width and height of images, we force the layout to avoid "jumps"
            ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();
            params.height = mImageHeight;
            params.width = mImageWidth;
            holder.mImageView.setLayoutParams(params);
        }

        DrawableRequestBuilder builder = Glide.with(MoviesApplication.getInstance())
                .load(movie.getPosterPath(movieImagesBaseUrl))
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(requestListener);

        // Use placeholder only if we are forcing the layout
        if (!auto)
            builder.placeholder(R.drawable.cinema_placeholder);

        builder.into(holder.mImageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        @BindView(R.id.image)
        public ImageView mImageView;
        @BindView(R.id.title)
        public TextView mTitleView;
        @BindView(R.id.title_rank)
        public TextView mTitleRankView;
        @BindView(R.id.overview)
        public TextView mOverView;

        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
        }
    }
}
