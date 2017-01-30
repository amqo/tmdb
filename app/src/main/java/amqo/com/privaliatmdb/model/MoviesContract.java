package amqo.com.privaliatmdb.model;

import io.reactivex.functions.Consumer;

public interface MoviesContract {

    interface View {
        void refreshMovies();
        void setLoading(boolean loading);
        void onMovieInteraction(Movie movie);
        int getScreenDensity();
    }

    interface Presenter {
        void getMovies(int page, Consumer<Movies> consumer);
        void updateMoviesConfiguration();
        String getMovieImagesBaseUrl();
    }
}
