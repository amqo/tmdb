package amqo.com.privaliatmdb.model.contracts;

import android.support.v7.widget.RecyclerView;

public interface MoviesScrollContract {

    interface View {

        RecyclerView.LayoutManager getLayoutManager();

        boolean isLoading();

        void loadMoreMovies();

        boolean isInLastPage();
    }

    interface FabView extends View {

        boolean isUpFABVisible();

        void setShownUpFAB(boolean show);
    }
}
