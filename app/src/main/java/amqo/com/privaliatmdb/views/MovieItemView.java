package amqo.com.privaliatmdb.views;

import android.support.annotation.NonNull;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieItemView {

    @BindView(R.id.image)
    public ImageView mImageView;
    @BindView(R.id.image_container)
    public View mImageContainerView;
    @BindView(R.id.title)
    public TextView mTitleView;
    @BindView(R.id.year)
    public TextView mYearView;
    @BindView(R.id.title_rank)
    public TextView mTitleRankView;
    @BindView(R.id.overview)
    public TextView mOverView;
    @BindView(R.id.image_rating)
    public RatingMovieView mRatingMovieView;

    private final View mParentView;
    private final MoviesContract.View mMoviesView;
    private final MoviesContract.Presenter mMoviesPresenter;

    private int mImageWidth = 0;
    private int mImageHeight = 0;

    private Map<ImageView, View.OnLayoutChangeListener>
            mCurrentLayoutListeners = new HashMap<>();

    public Movie mItem;

    public MovieItemView(View view, MoviesContract.View moviesView,
                         MoviesContract.Presenter moviesPresenter) {

        mParentView = view;
        mMoviesView = moviesView;
        mMoviesPresenter = moviesPresenter;

        ButterKnife.bind(this, view);
    }

    public void bindViewHolder(Movie movie, int position) {

        mItem = movie;

        mTitleView.setText(movie.getTitle());
        mYearView.setText(movie.getReleaseYear());
        mTitleRankView.setText(Integer.toString(position + 1));
        mOverView.setText(movie.getOverview());

        mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoviesView.onMovieInteraction(mItem);
            }
        });

        loadImageForMovie();
    }

    private void loadImageForMovie() {

        String movieImagesBaseUrl = mMoviesPresenter.getMovieImagesBaseUrl();
        if (TextUtils.isEmpty(movieImagesBaseUrl)) return;

        String moviePosterPath = mItem.getPosterPath(movieImagesBaseUrl);
        if (TextUtils.isEmpty(moviePosterPath)) {
            // This can happen when a movie does not have an image associated
            mImageContainerView.setVisibility(View.GONE);
            return;
        }

        RequestListener<String, GlideDrawable> requestListener =
                getDrawableRequestListener();

        setHolderImageLayout();

        mRatingMovieView.setVisibility(View.GONE);

        DrawableRequestBuilder builder = Glide.with(MoviesApplication.getInstance())
                .load(moviePosterPath)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(requestListener);

        builder.placeholder(R.drawable.cinema_placeholder);

        builder.into(mImageView);
    }

    private void setHolderImageLayout() {
        boolean auto = mImageWidth == 0 || mImageHeight == 0;
        if (auto) {
            // Add layout change listener only if a previous listener didn't fire before
            View.OnLayoutChangeListener layoutChangeListener = getOnLayoutChangeListener();

            mCurrentLayoutListeners.put(mImageView, layoutChangeListener);
            mImageView.addOnLayoutChangeListener(layoutChangeListener);
        } else {
            // If we got the width and height of images, we force the layout to avoid "jumps"
            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
            params.height = mImageHeight;
            params.width = mImageWidth;
            mImageView.setLayoutParams(params);
        }
    }

    @NonNull
    private View.OnLayoutChangeListener getOnLayoutChangeListener() {
        return new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int width, int height,
                                       int i4, int i5, int i6, int i7) {

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
    private RequestListener<String, GlideDrawable> getDrawableRequestListener() {

        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(
                    Exception e, String model, Target<GlideDrawable> target,
                    boolean isFirstResource) {

                mImageContainerView.setVisibility(View.GONE);
                // Get configuration again, as this error should occur if configuration changed in the API
                mMoviesPresenter.updateMoviesConfiguration();
                return false;
            }

            @Override
            public boolean onResourceReady(
                    GlideDrawable resource, String model,
                    Target<GlideDrawable> target, boolean isFromMemoryCache,
                    boolean isFirstResource) {

                mImageContainerView.setVisibility(View.VISIBLE);
                float rating = mItem.getVoteAverage();
                // Show only movies with rating, 0.0 meaning no ratings (most of the times)
                if (rating != 0.0) {
                    //Show decimals only when they are different than 0
                    String rating_s = rating == Math.ceil(rating) ?
                            Integer.toString((int)rating) : Float.toString(rating);
                    mRatingMovieView.setRating(rating_s);
                    mRatingMovieView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        };
    }
}
