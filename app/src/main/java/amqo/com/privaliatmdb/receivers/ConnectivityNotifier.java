package amqo.com.privaliatmdb.receivers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;

public class ConnectivityNotifier {

    private MoviesContract.Presenter mMoviesPresenter;

    private static boolean isConnected = false;

    private static boolean needConnectivityInit = true;

    @Inject
    public ConnectivityNotifier(MoviesContract.Presenter moviesPresenter) {

        needConnectivityInit = true;
        this.mMoviesPresenter = moviesPresenter;
        notifyConnectivityView(true);
    }

    public boolean isConnected() {

        if (needConnectivityInit) {
            needConnectivityInit = false;
            ConnectivityManager cm = (ConnectivityManager)
                    MoviesApplication.getInstance().getApplicationContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();
        }

        return isConnected;
    }

    public void notifyConnectivityView(boolean firstNotify) {

        needConnectivityInit = true;
        boolean isConnected = isConnected();

        if (firstNotify && isConnected) return;

        if (mMoviesPresenter != null) {
            mMoviesPresenter.onNetworkConnectionChanged(isConnected);
        }
    }
}
