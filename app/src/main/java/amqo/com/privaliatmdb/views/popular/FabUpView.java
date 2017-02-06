package amqo.com.privaliatmdb.views.popular;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.contracts.MoviesFabUpContract;

public class FabUpView implements MoviesFabUpContract.View {

    private FloatingActionButton mUpFAB;
    private MoviesFabUpContract.Presenter mFabUpPresenter;

    @Inject
    public FabUpView(MoviesFabUpContract.Presenter fabUpPresenter) {
        mFabUpPresenter = fabUpPresenter;
    }

    //  MoviesFabUpContract.View methods

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
                mFabUpPresenter.scrollUp();
            }
        });
    }
}
