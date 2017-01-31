package amqo.com.privaliatmdb.model;

import android.support.v7.widget.RecyclerView;

public interface MoviesScrollContract {

    interface View {

        void setUpFABVisibility(int visibility);
        boolean isUpFABVisible();
        RecyclerView.LayoutManager getLayoutManager();
        boolean isLoading();
        void loadMoreMovies();
    }
}
