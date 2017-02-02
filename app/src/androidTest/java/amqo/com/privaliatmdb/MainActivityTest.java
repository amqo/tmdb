package amqo.com.privaliatmdb;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.text.TextUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import amqo.com.privaliatmdb.injection.ApplicationComponent;
import amqo.com.privaliatmdb.injection.DaggerApplicationComponent;
import amqo.com.privaliatmdb.injection.modules.ApplicationModule;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.popular.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final String DEFAULT_SEARCH = "Blade Runner";
    protected final int TIMEOUT = 5000;

    private UiDevice mDevice;
    private SharedPreferences mSharedPreferences;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() {
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

    @Test
    public void sharedPreferencesTest() {
        String baseImageUrl = mSharedPreferences.getString(MoviesEndpoint.BASE_IMAGE_API_KEY, "");
        assertThat(TextUtils.isEmpty(baseImageUrl), is(false));

        String baseImageSize = mSharedPreferences.getString(MoviesEndpoint.BASE_IMAGE_SIZE_KEY, "");
        assertThat(TextUtils.isEmpty(baseImageSize), is(false));
    }

    @Test
    public void searchActivityTest() {

        clickOnSearchFab();

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText(DEFAULT_SEARCH), closeSoftKeyboard());

        scrollRecyclerviewDown();

        clickOnRemoveQueryButton();

        clickOnBackButton();
    }

    private void scrollRecyclerviewDown() {
        UiObject recyclerView = getRecyclerView();
        UiScrollable recyclerScrollable = new UiScrollable(recyclerView.getSelector());
        if (recyclerScrollable.exists()) {
            try {
                recyclerScrollable.scrollToEnd(50000);
            } catch (UiObjectNotFoundException e) {
                // Recyclerview not found
                e.printStackTrace();
            }
        }
    }

    private UiObject getRecyclerView() {
        UiObject recyclerView = mDevice.findObject(
                new UiSelector().resourceId("amqo.com.privaliatmdb:id/list"));
        mDevice.wait(Until.hasObject(By.res("amqo.com.privaliatmdb", "amqo.com.privaliatmdb:id/list")), TIMEOUT);
        return recyclerView;
    }

    private void clickOnSearchFab() {
        UiObject searchFabButton = mDevice.findObject(
                new UiSelector().resourceId("amqo.com.privaliatmdb:id/fab"));
        if (searchFabButton.exists()) {
            try {
                searchFabButton.click();
            } catch (UiObjectNotFoundException e) {
                // button not found
            }
        }
    }

    private void clickOnRemoveQueryButton() {
        UiObject removeButton = mDevice.findObject(
                new UiSelector().resourceId("amqo.com.privaliatmdb:id/search_close_btn"));
        if (removeButton.exists()) {
            try {
                removeButton.click();
            } catch (UiObjectNotFoundException e) {
                // button not found
            }
        }
    }

    private void clickOnBackButton() {
        UiObject toolbar = mDevice.findObject(new UiSelector().resourceId("amqo.com.privaliatmdb:id/toolbar"));
        try {
            UiObject backButton = toolbar.getChild(
                    new UiSelector().className("android.widget.ImageButton"));
            backButton.click();
        } catch (UiObjectNotFoundException e) {
            // button not found
        }
    }
}
