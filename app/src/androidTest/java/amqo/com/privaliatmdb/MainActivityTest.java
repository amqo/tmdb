package amqo.com.privaliatmdb;


import android.support.design.widget.AppBarLayout;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
    public void mainActivity_RecyclerSize() {

        onView(withId(R.id.list)).check(new RecyclerViewItemCountAssertion(20));
    }

    @Test
    public void mainActivity_collapseAndClickSearch() {

        onView(withId(R.id.app_bar)).perform(collapseAppBarLayout());

        ViewInteraction recyclerView = onView(withId(R.id.list));

        recyclerView.perform(scrollToPosition(1));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        clickOnSearch();
    }

    @Test
    public void mainActivity_rankText() {

        waitForRecyclerView();

        ViewInteraction recyclerView = onView(withId(R.id.list));

        recyclerView.check(new RecyclerViewRankAssertion("1"));

        recyclerView.perform(scrollToPosition(1));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
        recyclerView.check(new RecyclerViewRankAssertion("2"));
    }

    private void waitForRecyclerView() {

        mDevice.wait(Until.hasObject(By.res("amqo.com.privaliatmdb",
                "amqo.com.privaliatmdb:id/list")), 2000);
        mDevice.findObject(new UiSelector().resourceId("amqo.com.privaliatmdb:id/list"));
    }

    private void clickOnSearch() {

        mDevice.wait(Until.hasObject(By.res("amqo.com.privaliatmdb",
                "amqo.com.privaliatmdb:id/action_search")), 2000);
        UiObject searchItem = mDevice.findObject(
                new UiSelector().resourceId("amqo.com.privaliatmdb:id/action_search"));
        try {
            searchItem.click();
        } catch (UiObjectNotFoundException e) {
            // button not found
        }
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

    public class RecyclerViewRankAssertion implements ViewAssertion {
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
