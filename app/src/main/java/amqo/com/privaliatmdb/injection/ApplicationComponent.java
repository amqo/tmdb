package amqo.com.privaliatmdb.injection;

import javax.inject.Singleton;

import amqo.com.privaliatmdb.ParentApplication;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.injection.modules.MainActivityModule;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    ParentApplication application();

    MainActivityComponent getMainActivityComponent(
            MainActivityModule mainActivityModule);
}

