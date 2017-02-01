package amqo.com.privaliatmdb.model;

public interface MoviesAdapterContract {

    interface View {

        void refreshMovies(Movies movies);

        void addMovies(Movies movies);

        int getLastPageLoaded();

        boolean isInLastPage();
    }
}
