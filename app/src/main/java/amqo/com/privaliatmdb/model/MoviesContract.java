package amqo.com.privaliatmdb.model;

import io.reactivex.functions.Consumer;

public interface MoviesContract {

    interface View {
        void setLoading(boolean loading);
        void onMovieInteraction(Movie movie);
    }

    interface Presenter {
        void getMovies(int page, Consumer<Movies> consumer);
    }
}
