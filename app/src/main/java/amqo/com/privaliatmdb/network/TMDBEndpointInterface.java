package amqo.com.privaliatmdb.network;

import java.util.Map;

import amqo.com.privaliatmdb.model.Movies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface TMDBEndpointInterface {

    String BASE_API_URL = "https://api.themoviedb.org/";

    // https://api.themoviedb.org/3/movie/popular?language=es-ES&page=1&sort_by=popularity.desc&api_key=XXXX
    @GET("{version}/movie/popular/")
    Call<Movies> getPopularMovies(
            @Path("version") int versionCode,
            @QueryMap Map<String, String> parameters);
}
