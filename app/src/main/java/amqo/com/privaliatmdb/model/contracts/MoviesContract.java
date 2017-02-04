package amqo.com.privaliatmdb.model.contracts;

import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;

public interface MoviesContract {

    interface View {

        void refreshMovies();

        void setLoading(boolean loading);

        String getCorrectImageSize(MoviesConfiguration moviesConfiguration);

        void onMovieInteraction(Movie movie);

        void onMoviesLoaded(Movies movies);
    }

    interface ViewSearch extends View {

        void refreshMovies(String query);
    }

    interface Presenter {

        void updateMoviesConfiguration();

        String getMovieImagesBaseUrl();
    }

    interface PresenterPopular extends  Presenter{

        void getMovies(int page);
    }

    interface PresenterSearch extends  Presenter {

        void searchMovies(int page, String query);
    }
}
