package amqo.com.privaliatmdb.injection;

import amqo.com.privaliatmdb.MainActivity;
import amqo.com.privaliatmdb.injection.modules.MainActivityModule;
import amqo.com.privaliatmdb.injection.modules.NetworkModule;
import amqo.com.privaliatmdb.injection.scopes.PerActivity;
import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = { MainActivityModule.class, NetworkModule.class })
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);
}
