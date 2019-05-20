package br.com.dcarv.medicalerta.medlist

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.runner.AndroidJUnit4
import br.com.dcarv.medicalerta.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MedicationListFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<MedicationListFragment>

    @Before
    fun setUp() {
        val args = Bundle()
        fragmentScenario = launchFragmentInContainer(fragmentArgs = args, themeResId = 0) {
            MedicationListFragment()
        }
    }

    @Test
    fun shouldDisplayContent() {
        // TODO: replace real firebase auth with a fake and make sure it returns as logged in
        onView(withId(R.id.medListEmptyMessage))
            .check(matches(withText("Medication List")))
    }

//    @Test
//    fun shouldDisplayError() {
//        // TODO: replace real firebase auth with a fake and make sure it returns error
//        onView(withId(R.id.errorViewContainer))
//            .check(matches(isDisplayed()))
//    }
}