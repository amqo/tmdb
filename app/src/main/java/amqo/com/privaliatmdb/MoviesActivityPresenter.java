package amqo.com.privaliatmdb;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.network.IMoviesController;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoviesActivityPresenter implements IMoviesController {

    private MoviesEndpoint mMoviesEndpoint;
    private MovieParameterCreator mMovieParameterCreator;

    @Inject
    public MoviesActivityPresenter(
            MovieParameterCreator movieParameterCreator,
            MoviesEndpoint moviesEndpoint) {

        mMoviesEndpoint = moviesEndpoint;
        mMovieParameterCreator = movieParameterCreator;
    }

    @Override
    public void getMovies(int page, Consumer<Movies> consumer) {

        Observable<Movies> moviesObservable = mMoviesEndpoint.getMovies(
                MoviesEndpoint.API_VERSION, mMovieParameterCreator.createParameters(page));

        moviesObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }
}
