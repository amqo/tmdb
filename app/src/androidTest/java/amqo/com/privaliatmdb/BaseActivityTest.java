package amqo.com.privaliatmdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import org.junit.Rule;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.views.popular.MainActivity;

public class BaseActivityTest {

    protected UiDevice mDevice;
    protected SharedPreferences mSharedPreferences;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);


    protected void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        MoviesApplication application = MoviesApplication.getInstance();
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .build();

        mSharedPreferences = applicationComponent.getSharedPreferences();

        mActivityTestRule.launchActivity(
                new Intent(
                        InstrumentationRegistry.getTargetContext(),
                        MainActivity.class));
    }

    protected String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
