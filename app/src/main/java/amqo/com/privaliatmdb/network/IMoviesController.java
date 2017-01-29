package amqo.com.privaliatmdb.network;

import amqo.com.privaliatmdb.model.Movies;
import io.reactivex.functions.Consumer;

public interface IMoviesController {

    void getMovies(int page, Consumer<Movies> consumer);
}
