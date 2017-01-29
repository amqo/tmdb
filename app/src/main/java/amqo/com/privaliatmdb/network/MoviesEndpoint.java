package amqo.com.privaliatmdb.network;

import java.util.Map;

import amqo.com.privaliatmdb.model.Movies;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface MoviesEndpoint {

    String BASE_API_URL = "https://api.themoviedb.org/";
    String BASE_IMAGE_API_URL = "https://image.tmdb.org/t/p/";

    int API_VERSION = 3;

    @GET("{version}/movie/popular/")
    Observable<Movies> getMovies(
            @Path("version") int versionCode,
            @QueryMap Map<String, String> parameters);
}
