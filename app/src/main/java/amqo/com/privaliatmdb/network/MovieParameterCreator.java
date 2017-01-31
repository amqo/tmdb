package amqo.com.privaliatmdb.network;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import amqo.com.privaliatmdb.PrivateConstants;

public abstract class MovieParameterCreator {

    public static Map<String, String> createPopularMoviesParameters(int page) {

        // https://api.themoviedb.org/3/movie/popular?language=es-ES&page=1&&sort_by=popularity.desc&api_key=XXXX

        Map<String, String> parameters = new HashMap<>();
        parameters.put("language", getCountryCode());
        parameters.put("page", Integer.toString(page));
        parameters.put("sort_by", "popularity.desc");
        parameters.put("api_key", PrivateConstants.TMDB_API_KEY);
        return parameters;
    }

    public static Map<String, String> createSearchParameters(int page, String query) {

        // https://api.themoviedb.org/3/search/movie?api_key=XXXX&language=en-US&query=Pets&page=1&include_adult=false

        Map<String, String> parameters = new HashMap<>();
        parameters.put("language", getCountryCode());
        parameters.put("page", Integer.toString(page));
        parameters.put("query", query);
        parameters.put("include_adult", "false");
        parameters.put("api_key", PrivateConstants.TMDB_API_KEY);
        return parameters;
    }

    public static Map<String, String> createMoviesConfigurationParameters() {

        // https://api.themoviedb.org/3/configuration?&api_key=XXXX

        Map<String, String> parameters = new HashMap<>();
        parameters.put("api_key", PrivateConstants.TMDB_API_KEY);
        return parameters;
    }

    private static String getCountryCode() {
        String countryCode = Locale.getDefault().getCountry();
        String languageCode = Locale.getDefault().getLanguage();
        return  languageCode + "-" + countryCode;
    }
}
