package amqo.com.privaliatmdb;


import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest extends BaseActivityTest {

    private final String DEFAULT_SEARCH = "Blade Runner";

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void searchActivityTest() throws InterruptedException {

        // Check fab button to start search, toolbar not collapsed
        onView(withId(R.id.fab)).perform(click());

        // Check search query
        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText(DEFAULT_SEARCH), closeSoftKeyboard());

        Thread.sleep(2 * 1000);

        ViewInteraction recyclerView = onView(withId(R.id.list));

        // Check first element rank
        recyclerView.check(new MainActivityTest.RecyclerViewRankAssertion("1"));

        // Check second element rank
        recyclerView.perform(scrollToPosition(1));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
        recyclerView.check(new MainActivityTest.RecyclerViewRankAssertion("2"));

        clickOnRemoveQueryButton();

        clickOnBackButton();
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
