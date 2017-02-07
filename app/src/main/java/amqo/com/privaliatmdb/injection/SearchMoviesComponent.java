package amqo.com.privaliatmdb.injection;

import amqo.com.privaliatmdb.injection.modules.NetworkModule;
import amqo.com.privaliatmdb.injection.modules.SearchMoviesModule;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import amqo.com.privaliatmdb.views.search.SearchMoviesFragment;
import dagger.Subcomponent;

@PerFragment
@Subcomponent( modules = { NetworkModule.class, SearchMoviesModule.class })
public interface SearchMoviesComponent extends BaseMoviesComponent<SearchMoviesFragment> {

}
