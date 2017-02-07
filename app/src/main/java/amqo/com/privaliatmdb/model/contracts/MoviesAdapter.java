package amqo.com.privaliatmdb.model.contracts;

import amqo.com.privaliatmdb.model.Movies;

public interface MoviesAdapter {

        void refreshMovies(Movies movies);

        void addMovies(Movies movies);
}
