package br.com.dcarv.medicalerta.common.authentication

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.dcarv.medicalerta.common.model.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val USER_ID = "USER_ID"
private const val USER_EMAIL = "USER_EMAIL"
private const val USER_NAME = "USER_NAME"

class AuthenticationManagerTest {

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var firebaseUi: AuthUI

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var authManager: AuthenticationManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_authenticateIfNecessary_activity_loggedIn() {
        // given
        val firebaseUser = mockk<FirebaseUser>()
        every { firebaseUser.uid } returns USER_ID
        every { firebaseUser.email } returns USER_EMAIL
        every { firebaseUser.displayName } returns USER_NAME
        every { firebaseAuth.currentUser } returns firebaseUser

        val activity = mockk<Activity>(relaxed = true)

        // when
        val testObserver = authManager.authenticateIfNecessary(activity).test()

        // then
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        testObserver.assertNoErrors()
        testObserver.assertValue(user)
    }

    @Test
    fun test_authenticateIfNecessary_activity_notLoggedIn_success() {
        // given
        val firebaseUser = mockk<FirebaseUser>()
        every { firebaseUser.uid } returns USER_ID
        every { firebaseUser.email } returns USER_EMAIL
        every { firebaseUser.displayName } returns USER_NAME
        every { firebaseAuth.currentUser } returnsMany listOf(null, firebaseUser)
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        every { userRepository.saveUser(user) } returns Completable.complete()

        val activity = mockk<Activity>(relaxed = true)

        val intent = Intent()
        every {
            firebaseUi.createSignInIntentBuilder()
                .setAvailableProviders(any())
                .build()
        } returns intent

        // when
        val testObserver = authManager.authenticateIfNecessary(activity).test()
        authManager.onAuthenticationActivityResult(801, Activity.RESULT_OK, null)

        // then

        testObserver.assertNoErrors()
        testObserver.assertValue(user)
    }

    @Test
    fun test_authenticateIfNecessary_fragment_loggedIn() {
        // given
        val firebaseUser = mockk<FirebaseUser>()
        every { firebaseUser.uid } returns USER_ID
        every { firebaseUser.email } returns USER_EMAIL
        every { firebaseUser.displayName } returns USER_NAME
        every { firebaseAuth.currentUser } returns firebaseUser

        val fragment = mockk<Fragment>(relaxed = true)

        // when
        val testObserver = authManager.authenticateIfNecessary(fragment).test()

        // then
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        testObserver.assertNoErrors()
        testObserver.assertValue(user)
    }

    @Test
    fun onAuthenticationActivityResult() {
        // given
        val firebaseUser = mockk<FirebaseUser>()
        every { firebaseUser.uid } returns USER_ID
        every { firebaseUser.email } returns USER_EMAIL
        every { firebaseUser.displayName } returns USER_NAME
        every { firebaseAuth.currentUser } returnsMany listOf(null, firebaseUser)
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        every { userRepository.saveUser(user) } returns Completable.complete()

        val fragment = mockk<Fragment>(relaxed = true)

        val intent = Intent()
        every {
            firebaseUi.createSignInIntentBuilder()
                .setAvailableProviders(any())
                .build()
        } returns intent

        // when
        val testObserver = authManager.authenticateIfNecessary(fragment).test()
        authManager.onAuthenticationActivityResult(801, Activity.RESULT_OK, null)

        // then
        testObserver.assertNoErrors()
        testObserver.assertValue(user)
    }
}