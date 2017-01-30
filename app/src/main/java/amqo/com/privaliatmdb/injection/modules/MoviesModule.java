package amqo.com.privaliatmdb.injection.modules;

import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MoviesModule {

    @Provides @PerFragment
    MoviesEndpoint providesMoviesEndpoint(Retrofit retrofit) {
        return retrofit.create(MoviesEndpoint.class);
    }
}
