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
        mUpFAB = mFabUpView.getFabUp();

        initUpFab();
    }

    @Override
    public void setShownUpFAB(final boolean show) {
        if (show) mUpFAB.show();
        else mUpFAB.hide();
    }

    @Override
    public boolean isUpFABVisible() {
        return mUpFAB.isShown();
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
