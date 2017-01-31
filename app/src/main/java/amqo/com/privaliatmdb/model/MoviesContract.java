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
        void getMovies(int page);
        void updateMoviesConfiguration();
        String getMovieImagesBaseUrl();
    }
}
