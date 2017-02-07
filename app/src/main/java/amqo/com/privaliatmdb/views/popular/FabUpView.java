package amqo.com.privaliatmdb.views.popular;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesFabUp;

public class FabUpView implements MoviesFabUp {

    private FloatingActionButton mUpFAB;
    private MoviesContract.Presenter mMoviesPresenter;

    @Inject
    public FabUpView(MoviesContract.Presenter moviesPresenter) {
        mMoviesPresenter = moviesPresenter;
    }

    //  MoviesFabUp.View methods

    @Override
    public boolean isUpFABVisible() {
        return mUpFAB == null ? false : mUpFAB.isShown();
    }

    @Override
    public void setShownUpFAB(final boolean show) {
        if (mUpFAB == null) return;
        if (show) mUpFAB.show();
        else mUpFAB.hide();
    }

    @Override
    public void setUpFab(FloatingActionButton upFab) {
        mUpFAB = upFab;
        initUpFab();
    }

    private void initUpFab() {
        mUpFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpFAB.hide();
                mMoviesPresenter.scrollUp();
            }
        });
    }
}
