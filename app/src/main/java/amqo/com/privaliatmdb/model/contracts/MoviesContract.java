package amqo.com.privaliatmdb.model.contracts;

import android.support.v7.widget.RecyclerView;

import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.model.MoviesContext;

public interface MoviesContract {

    interface View {

        void setLoading(boolean loading);

        boolean isLoading();

        RecyclerView.LayoutManager getLayoutManager();

        String getCorrectImageSize(MoviesConfiguration moviesConfiguration);

        void onMovieInteraction(Movie movie);

        void onMoviesLoaded(Movies movies);

        RecyclerView getRecyclerView();

        void clearMovies();

        MoviesContext getMoviesContext();
    }

    interface Presenter {

        String getMovieImagesBaseUrl();

        void updateMoviesConfiguration();

        void loadMoreMovies();

        void refreshMovies();

        void scrollUp();

        void onNetworkConnectionChanged(boolean isConnected);
    }

    interface PresenterSearch extends Presenter {

        void setNewQuery(String query);
    }
}
