package amqo.com.privaliatmdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.junit.Rule;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.views.popular.MainActivity;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.is;

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

    protected class RecyclerViewRankAssertion implements ViewAssertion {
        private final String expectedRank;

        public RecyclerViewRankAssertion(String expectedRank) {
            this.expectedRank = expectedRank;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            View viewHolder = recyclerView.getLayoutManager().getChildAt(0);
            TextView rankText = (TextView) viewHolder.findViewById(R.id.title_rank);
            assertThat(rankText.getText().toString(), is(expectedRank));
        }
    }
}
