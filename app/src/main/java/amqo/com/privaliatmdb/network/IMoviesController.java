package amqo.com.privaliatmdb.network;

import amqo.com.privaliatmdb.model.Movies;

public interface IMoviesController {

    interface IMoviesListener {
        void onMoviesLoaded(Movies movies);
    }

    void getMovies(int page, IMoviesListener listener);
}
