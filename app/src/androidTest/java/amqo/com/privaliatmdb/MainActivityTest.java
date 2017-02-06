package amqo.com.privaliatmdb;


import android.support.design.widget.AppBarLayout;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import amqo.com.privaliatmdb.network.MoviesEndpoint;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends BaseActivityTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void sharedPreferencesTest() {
        String baseImageUrl = mSharedPreferences.getString(MoviesEndpoint.BASE_IMAGE_API_KEY, "");
        assertThat(TextUtils.isEmpty(baseImageUrl), is(false));

        String baseImageSize = mSharedPreferences.getString(MoviesEndpoint.BASE_IMAGE_SIZE_KEY, "");
        assertThat(TextUtils.isEmpty(baseImageSize), is(false));
    }

    @Test
    public void mainActivity_checkRecyclerView() throws InterruptedException {

        // Check collapse
        onView(withId(R.id.app_bar)).perform(collapseAppBarLayout());

        Thread.sleep(2 * 1000);

        ViewInteraction recyclerView = onView(withId(R.id.list));

        // Check initial load size
        recyclerView.check(new RecyclerViewItemCountAssertion(20));

        // Check first element rank
        recyclerView.check(new RecyclerViewRankAssertion("1"));

        // Check second element rank
        recyclerView.perform(scrollToPosition(1));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
        recyclerView.check(new RecyclerViewRankAssertion("2"));

        // Check collapsed toolbar search available
        String searchTitle = getResourceString(R.string.search_title);
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_search), withContentDescription(searchTitle), isDisplayed()));
        actionMenuItemView.perform(click());

    }

    private ViewAction collapseAppBarLayout() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(AppBarLayout.class);
            }

            @Override
            public String getDescription() {
                return "Collapse App Bar Layout";
            }

            @Override
            public void perform(UiController uiController, View view) {
                AppBarLayout appBarLayout = (AppBarLayout) view;
                appBarLayout.setExpanded(false);
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    private class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }
}
