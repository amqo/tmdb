package amqo.com.privaliatmdb.model.contracts;

public interface ConnectivityReceiverContract {

    interface View {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
