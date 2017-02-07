package amqo.com.privaliatmdb.injection;

import amqo.com.privaliatmdb.injection.modules.MoviesModule;
import amqo.com.privaliatmdb.injection.modules.NetworkModule;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;
import dagger.Subcomponent;

@PerFragment
@Subcomponent( modules = { NetworkModule.class, MoviesModule.class })
public interface MoviesComponent extends BaseMoviesComponent<MoviesFragment> {

}
