package amqo.com.privaliatmdb.model.contracts;

import android.support.v7.widget.RecyclerView;

public interface MoviesScrollContract {

    interface View {

        boolean isLoading();

        void loadMoreMovies();

        boolean isInLastPage();

        RecyclerView.LayoutManager getLayoutManager();
    }
}
