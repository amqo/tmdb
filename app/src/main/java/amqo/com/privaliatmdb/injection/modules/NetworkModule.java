package amqo.com.privaliatmdb.injection.modules;

import amqo.com.privaliatmdb.ParentApplication;
import amqo.com.privaliatmdb.injection.scopes.PerActivity;
import amqo.com.privaliatmdb.network.IMoviesEndpoint;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides @PerActivity
    Cache providesOkHttpCache(ParentApplication application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }


    @Provides @PerActivity
    OkHttpClient providesOkHttpClient(Cache cache) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.cache(cache);
        OkHttpClient client = clientBuilder.build();
        return client;
    }

    @Provides @PerActivity
    Retrofit providesRetrofit(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(IMoviesEndpoint.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    @Provides @PerActivity
    IMoviesEndpoint providesMoviesEndpoint(Retrofit retrofit) {
        return retrofit.create(IMoviesEndpoint.class);
    }
}
