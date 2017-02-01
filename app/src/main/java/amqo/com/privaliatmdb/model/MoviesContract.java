package amqo.com.privaliatmdb.model;

public interface MoviesContract {

    interface View {

        void refreshMovies();

        void setLoading(boolean loading);

        void onMovieInteraction(Movie movie);

        int getScreenDensity();

        void onMoviesLoaded(Movies movies);
    }

    interface Presenter {

        void updateMoviesConfiguration();

        String getMovieImagesBaseUrl();
    }

    interface PresenterPopular extends  Presenter{

        void getMovies(int page);
    }

    interface PresenterSearch extends  Presenter{

        void searchMovies(int page, String query);
    }
}
