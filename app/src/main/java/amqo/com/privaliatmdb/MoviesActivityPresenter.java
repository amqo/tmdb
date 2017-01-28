package amqo.com.privaliatmdb;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.network.IMoviesController;
import amqo.com.privaliatmdb.network.IMoviesEndpoint;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivityPresenter implements IMoviesController, Callback<Movies> {

    private IMoviesEndpoint mMoviesEndpoint;
    private MovieParameterCreator mMovieParameterCreator;

    private List<IMoviesListener> mListeners;

    @Inject
    public MoviesActivityPresenter(
            MovieParameterCreator movieParameterCreator,
            IMoviesEndpoint moviesEndpoint) {

        mMoviesEndpoint = moviesEndpoint;
        mMovieParameterCreator = movieParameterCreator;
        mListeners = new ArrayList<>();
    }

    @Override
    public void getMovies(int page, IMoviesListener listener) {
        addListener(listener);

        Call<Movies> call = mMoviesEndpoint.getMovies(
                IMoviesEndpoint.API_VERSION, mMovieParameterCreator.createParameters(page));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        Movies movies = response.body();
        for (IMoviesListener listener : mListeners)
            listener.onMoviesLoaded(movies);
        mListeners.clear();
    }

    @Override
    public void onFailure(Call<Movies> call, Throwable t) {
        Log.e("", t.getMessage());
    }

    private void addListener(IMoviesListener listener) {
        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }
}
