package amqo.com.privaliatmdb.injection;

import amqo.com.privaliatmdb.MoviesActivityPresenter;
import amqo.com.privaliatmdb.fragments.MoviesFragment;
import amqo.com.privaliatmdb.injection.modules.MainActivityModule;
import amqo.com.privaliatmdb.injection.modules.MoviesModule;
import amqo.com.privaliatmdb.injection.modules.NetworkModule;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import dagger.Subcomponent;

@PerFragment
@Subcomponent( modules = {
        NetworkModule.class, MoviesModule.class, MainActivityModule.class
})
public interface MainActivityComponent {

    MoviesActivityPresenter moviesController();

    void inject(MoviesFragment moviesFragment);
}
