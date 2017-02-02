package amqo.com.privaliatmdb.views.popular;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.contracts.MoviesFabUpContract;

public class FabUpPresenter implements MoviesFabUpContract.Presenter {

    private FloatingActionButton mUpFAB;
    private MoviesFabUpContract.View mFabUpView;

    @Inject
    public FabUpPresenter(MoviesFabUpContract.View fabUpView) {
        mFabUpView = fabUpView;
    }

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
                mFabUpView.scrollUp();
            }
        });
    }
}
