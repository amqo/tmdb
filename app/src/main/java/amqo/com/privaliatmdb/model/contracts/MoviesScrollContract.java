package amqo.com.privaliatmdb.model.contracts;

import android.support.v7.widget.RecyclerView;

public interface MoviesScrollContract {

        boolean isLoading();

        RecyclerView.LayoutManager getLayoutManager();
}
