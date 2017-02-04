package amqo.com.privaliatmdb.views.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.views.RatingMovieView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieItemViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

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

    public Movie mItem;

    public MovieItemViewHolder(View view) {
        super(view);
        mView = view;

        ButterKnife.bind(this, view);
    }
}
