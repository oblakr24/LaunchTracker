package oblak.r.launchtrackerdev

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import oblak.r.baseapp.main.LaunchesActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.*
import oblak.r.baseapp.main.DisplayableRocket
import oblak.r.launchtrackerdev.TestUtils.withCount
import org.hamcrest.CoreMatchers.*


@RunWith(AndroidJUnit4::class)
@LargeTest
class DisplayLaunchesTest {

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(LaunchesActivity::class.java)

    private fun clickSpinnerItem(pos: Int) {
        onView(withId(R.id.spinner_rockets)).perform(click())
        onData(instanceOf(DisplayableRocket::class.java))
                .atPosition(pos)
                .perform(click())
    }

    private fun checkSpinnerItemCount(expectedCount: Int) {
        onView(withId(R.id.recycler_launches))
                .check(withCount(expectedCount))
    }

    /**
     * Click the spinner items and check the number of recycler view items
     */
    @Test
    fun changeSelectedRocket() {
        clickSpinnerItem(0)
        checkSpinnerItemCount(3)

        clickSpinnerItem(1)
        checkSpinnerItemCount(1)

        clickSpinnerItem(2)
        checkSpinnerItemCount(1)

        clickSpinnerItem(3)
        checkSpinnerItemCount(1)
    }
}
