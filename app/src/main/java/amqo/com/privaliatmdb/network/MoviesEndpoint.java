package amqo.com.privaliatmdb.network;

import java.util.Map;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesConfiguration;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface MoviesEndpoint {

    String BASE_API_URL = "https://api.themoviedb.org/";

    String BASE_IMAGE_API_KEY = "BASE_IMAGE_API_KEY";
    String BASE_IMAGE_SIZE_KEY = "BASE_IMAGE_SIZE_KEY";

    int API_VERSION = 3;

    @GET("{version}/movie/popular/")
    Observable<Movies> getMovies(
            @Path("version") int versionCode,
            @QueryMap Map<String, String> parameters);

    @GET("{version}/configuration")
    Observable<MoviesConfiguration> getMoviesConfiguration(
            @Path("version") int versionCode,
            @QueryMap Map<String, String> parameters);

    @GET("{version}/search/movie")
    Observable<Movies> searchMovies(
            @Path("version") int versionCode,
            @QueryMap Map<String, String> parameters);
}
