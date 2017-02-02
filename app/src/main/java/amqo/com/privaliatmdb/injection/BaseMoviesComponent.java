package amqo.com.privaliatmdb.injection;

import amqo.com.privaliatmdb.receivers.ConnectivityReceiver;
import amqo.com.privaliatmdb.views.BaseMoviesFragment;

public interface BaseMoviesComponent {

    // Common injection for all Movies modules
    void inject(ConnectivityReceiver connectivityReceiver);
    void inject(BaseMoviesFragment baseMoviesFragment);
}
