package br.com.dcarv.medicalerta.common.authentication

import br.com.dcarv.medicalerta.common.model.User
import br.com.dcarv.medicalerta.common.network.asCompletable
import br.com.dcarv.medicalerta.common.network.user
import br.com.dcarv.medicalerta.mockFailedTask
import br.com.dcarv.medicalerta.mockSerialization
import br.com.dcarv.medicalerta.mockSuccessfulTask
import br.com.dcarv.medicalerta.mockSuccessfulVoidTask
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

private const val USER_ID = "USER_ID"
private const val USER_EMAIL = "USER_EMAIL"
private const val USER_NAME = "USER_NAME"

class UserRepositoryTest {

    @MockK
    lateinit var firebaseDb: FirebaseFirestore

    @InjectMockKs
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_saveUser_success() {
        // given
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        every { firebaseDb.user(USER_ID).set(user) } returns mockSuccessfulVoidTask()

        // when
        val testObserver = userRepository.saveUser(user).test()

        // then
        verify { firebaseDb.user(USER_ID).set(user).asCompletable() }
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun test_saveUser_fail() {
        // given
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        val error = Exception("SOME ERROR")
        every {
            firebaseDb.user(USER_ID)
                .set(user)
        } returns mockFailedTask(error)

        // when
        val testObserver = userRepository.saveUser(user).test()

        // then
        verify { firebaseDb.user(USER_ID).set(user).asCompletable() }
        testObserver.assertError(error)
    }

    @Test
    fun test_getUser_success() {
        // given
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        val userSnapshot = mockk<DocumentSnapshot>().mockSerialization(user)
        every { firebaseDb.user(USER_ID).get() } returns mockSuccessfulTask(userSnapshot)

        // when
        val testObserver = userRepository.getUser(USER_ID).test()

        // then
        verify { firebaseDb.user(USER_ID).get() }
        testObserver.assertNoErrors()
        testObserver.assertValue(user)
    }

    @Test
    fun test_getUser_fail() {
        // given
        val error = Exception("SOME ERROR")
        every { firebaseDb.user(USER_ID).get() } returns mockFailedTask(error)

        // when
        val testObserver = userRepository.getUser(USER_ID).test()

        // then
        verify { firebaseDb.user(USER_ID).get() }
        testObserver.assertError(error)
    }
}