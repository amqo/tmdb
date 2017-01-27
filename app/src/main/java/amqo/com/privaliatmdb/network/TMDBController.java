package amqo.com.privaliatmdb.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import amqo.com.privaliatmdb.ParentApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movies;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TMDBController implements  IMoviesController, Callback<Movies> {

    @Inject Retrofit retrofit;
    @Inject ParentApplication mContext;

    private List<IMoviesListener> mListeners;

    public TMDBController(ParentApplication application) {
        application.getParentComponent().inject(this);
        mListeners = new ArrayList<>();
    }

    @Override
    public void getMovies(int page, IMoviesListener listener) {
        addListener(listener);

        TMDBEndpointInterface tmdbAPI = retrofit.create(
                TMDBEndpointInterface.class);

        // https://api.themoviedb.org/3/movie/popular?language=es-ES&page=1&&sort_by=popularity.desc&api_key=93aea0c77bc168d8bbce3918cefefa45

        Map<String, String> parameters = new HashMap<>();
        parameters.put("language", Locale.getDefault().getDisplayLanguage());
        parameters.put("page", Integer.toString(page));
        parameters.put("sort_by", "popularity.desc");
        parameters.put("api_key", mContext.getString(R.string.privalia_tmdb_api_key));

        Call<Movies> call = tmdbAPI.getPopularMovies(3, parameters);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        Movies movies = response.body();
        for (IMoviesListener listener : mListeners)
            listener.onMoviesLoaded(movies);
        mListeners.clear();
    }

    @Override
    public void onFailure(Call<Movies> call, Throwable t) {

    }

    private void addListener(IMoviesListener listener) {
        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }
}
