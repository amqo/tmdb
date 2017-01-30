package amqo.com.privaliatmdb.injection.modules;

import amqo.com.privaliatmdb.MainActivity;
import amqo.com.privaliatmdb.fragments.MoviesFragment.OnMoviesInteractionListener;
import amqo.com.privaliatmdb.injection.scopes.PerFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    MainActivity mMainActivity;

    public MainActivityModule(MainActivity activity) {
        mMainActivity = activity;
    }

    @Provides @PerFragment
    OnMoviesInteractionListener providesMainActivity() {
        return mMainActivity;
    }

}
