package amqo.com.privaliatmdb.model.contracts;

public interface ConnectivityReceiverContract {

    interface Listener {

        void onNetworkConnectionChanged(boolean isConnected);
    }
}
