package amqo.com.privaliatmdb.model.contracts;

import amqo.com.privaliatmdb.model.Movies;

public interface MoviesAdapterContract {

    interface View {

        void refreshMovies(Movies movies);

        void addMovies(Movies movies);
    }
}
