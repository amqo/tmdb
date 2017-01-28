package amqo.com.privaliatmdb.network;

import java.util.Map;

import amqo.com.privaliatmdb.model.Movies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IMoviesEndpoint {

    String BASE_API_URL = "https://api.themoviedb.org/";

    int API_VERSION = 3;

    @GET("{version}/movie/popular/")
    Call<Movies> getMovies(
            @Path("version") int versionCode,
            @QueryMap Map<String, String> parameters);
}
