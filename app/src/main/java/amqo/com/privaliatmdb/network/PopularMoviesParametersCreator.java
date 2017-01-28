package amqo.com.privaliatmdb.network;

import java.util.HashMap;
import java.util.Map;

import amqo.com.privaliatmdb.PrivateConstants;

public class PopularMoviesParametersCreator extends MovieParameterCreator {

    @Override
    public Map<String, String> createParameters(int page) {

        // https://api.themoviedb.org/3/movie/popular?language=es-ES&page=1&&sort_by=popularity.desc&api_key=XXXX

        Map<String, String> parameters = new HashMap<>();
        parameters.put("language", getCountryCode());
        parameters.put("page", Integer.toString(page));
        parameters.put("sort_by", "popularity.desc");
        parameters.put("api_key", PrivateConstants.TMDB_API_KEY);
        return parameters;
    }
}
