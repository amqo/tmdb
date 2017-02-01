package amqo.com.privaliatmdb.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import amqo.com.privaliatmdb.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingMovieView extends RelativeLayout {

    @BindView(R.id.rating_text)
    TextView mRatingText;

    private LayoutInflater mInflater;


    public RatingMovieView(Context context) {
        super(context);
        initRatingMovieView(context);
    }

    public RatingMovieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRatingMovieViewWithAttrs(context, attrs);
    }


    public RatingMovieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRatingMovieViewWithAttrs(context, attrs);
    }


    public void setRating(String rating) {
        mRatingText.setText(rating);
    }

    private void initRatingMovieViewWithAttrs(Context context, AttributeSet attrs) {

        initRatingMovieView(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingMovieView, 0, 0);
        try {
            float textSize = typedArray.getDimension(R.styleable.RatingMovieView_ratingTextSize, 8.0f);
            float textSize_sp = textSize / getResources().getDisplayMetrics().scaledDensity;
            mRatingText.setTextSize((int)textSize_sp);
        } finally {
            typedArray.recycle();
        }

    }

    private void initRatingMovieView(Context context) {
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.rating_movie_layout, this, true);

        ButterKnife.bind(this, this);
    }
}
