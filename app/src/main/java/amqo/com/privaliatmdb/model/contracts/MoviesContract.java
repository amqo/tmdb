package amqo.com.privaliatmdb.model.contracts;

import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;

public interface MoviesContract {

    interface View {

        void setLoading(boolean loading);

        String getCorrectImageSize(MoviesConfiguration moviesConfiguration);

        void onMovieInteraction(Movie movie);

        void onMoviesLoaded(Movies movies);

        void clearMovies();
    }

    interface Presenter {

        String getMovieImagesBaseUrl();

        void updateMoviesConfiguration();

        void loadMoreMovies();

        void refreshMovies();
    }

    interface PresenterSearch extends Presenter {

        void setNewQuery(String query);
    }
}
