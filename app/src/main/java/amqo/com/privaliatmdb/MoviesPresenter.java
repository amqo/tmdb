package amqo.com.privaliatmdb;

import javax.inject.Named;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoviesPresenter implements MoviesContract.Presenter {

    private MoviesEndpoint mMoviesEndpoint;
    private MovieParameterCreator mMovieParameterCreator;

    private final MoviesContract.View mMoviesView;

    public MoviesPresenter(
            MovieParameterCreator movieParameterCreator,
            MoviesEndpoint moviesEndpoint,
            @Named("fragment") MoviesContract.View moviesView) {

        mMoviesEndpoint = moviesEndpoint;
        mMovieParameterCreator = movieParameterCreator;
        mMoviesView = moviesView;
    }

    @Override
    public void getMovies(int page, Consumer<Movies> consumer) {

        Observable<Movies> moviesObservable = mMoviesEndpoint.getMovies(
                MoviesEndpoint.API_VERSION, mMovieParameterCreator.createParameters(page));

        mMoviesView.setLoading(true);

        moviesObservable
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mMoviesView.setLoading(false);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }
}
