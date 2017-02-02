package amqo.com.privaliatmdb.model.contracts;

import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;

public interface MoviesContract {

    interface View {

        void refreshMovies();

        void setLoading(boolean loading);

        int getScreenDensity();

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
