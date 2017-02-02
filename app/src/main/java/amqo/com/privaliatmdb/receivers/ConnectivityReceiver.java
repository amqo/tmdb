package amqo.com.privaliatmdb.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.injection.BaseMoviesComponent;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Inject ConnectivityNotifier mConnectivityNotifier;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        BaseMoviesComponent baseMoviesComponent =
                MoviesApplication.getInstance().getCurrentMoviesComponent();

        if (baseMoviesComponent != null) {

            baseMoviesComponent.inject(this);
            mConnectivityNotifier.notifyConnectivityView(false);
        }
    }

}
