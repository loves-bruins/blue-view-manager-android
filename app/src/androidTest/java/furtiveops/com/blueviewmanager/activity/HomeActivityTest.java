package furtiveops.com.blueviewmanager.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import furtiveops.com.blueviewmanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void homeActivityTest() {
        ViewInteraction appCompatEditText = onView(
                withId(R.id.username));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.username));
        appCompatEditText2.perform(scrollTo(), replaceText("blueviewaquatics@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.password));
        appCompatEditText3.perform(scrollTo(), replaceText("password"), closeSoftKeyboard());

        ViewInteraction fButton = onView(
                allOf(withId(R.id.login), withText("Sign In")));
        fButton.perform(scrollTo(), click());
    }
}
