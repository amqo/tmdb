package amqo.com.privaliatmdb.infrastructure.injection.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.ParentApplication;
import amqo.com.privaliatmdb.network.IMoviesController;
import amqo.com.privaliatmdb.network.TMDBController;
import amqo.com.privaliatmdb.network.TMDBEndpointInterface;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TMDBModule {

    @Provides @Singleton
    IMoviesController providesMoviesController(ParentApplication application) {
        return new TMDBController(application);
    }

    @Provides @Singleton
    SharedPreferences providesSharedPreferences(ParentApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides @Singleton
    Cache providesOkHttpCache(ParentApplication application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }


    @Provides @Singleton
    OkHttpClient providesOkHttpClient(Cache cache) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.cache(cache);
        OkHttpClient client = clientBuilder.build();
        return client;
    }

    @Provides @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(TMDBEndpointInterface.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
