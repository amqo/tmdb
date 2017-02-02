package amqo.com.privaliatmdb.injection;

import amqo.com.privaliatmdb.receivers.ConnectivityReceiver;

public interface BaseMoviesComponent {

    void inject(ConnectivityReceiver connectivityReceiver);
}
